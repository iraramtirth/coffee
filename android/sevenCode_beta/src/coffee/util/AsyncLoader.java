package coffee.util;



import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import coffee.seven.action.BitmapManager;
import coffee.seven.bean.ImageType;
import coffee.util.http.HttpClient;
import coffee.util.view.BitmapUtils;
/**
 * 异步加载
 * @author wangtao
 */
public abstract class AsyncLoader extends AsyncTask<Object, Void, Object>{
	
	/**
	 * {@link #execute(View, String)}
	 */
	private View view;
	
	protected String imgLocalUrl = null;
	
	/**
	 * param_0 : 远程资源的url
	 * param_1  ：  ImageType 如果为null
	 */
	@Override
	protected Object doInBackground(Object... params) {
		String resUrl = params[0] + "";
		Object result = null;
		try{
			if(params[1] != ImageType.NONE){
				String fileName= resUrl.substring(resUrl.lastIndexOf("/")+1);
				String localPath = BitmapUtils.getCachePath(fileName);
				boolean isCache = false;
				if(params[1] != ImageType.SALE_BASE_AD){
					isCache = true;
				}
				//从本地加载
				Bitmap bmp = BitmapUtils.loadBitmapFromLocal(localPath, isCache);
				if(bmp == null){
					//从远程加载
					bmp = new BitmapUtils().loadBitmapFromNet(resUrl);
					//缓存到本地
					if(bmp != null){
						imgLocalUrl = BitmapUtils.getCachePath(fileName);
						if(isCache){
							BitmapManager.put(localPath, bmp);
						}
						result = bmp;
						//保存到数据库
						//SaleUtils.saleService.setLocalImage(resUrl, imgLocalUrl, (ImageType)params[1]);
					}
				}else{
					result = bmp;
				}
			}else{
				String doc = new HttpClient().get(resUrl) + "";
				result = doc;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 从网络加载图片
	 */
	@Override
	protected void onPostExecute(Object result) {
		try{
			if(result != null){
				if(result instanceof Bitmap){
					Bitmap bmp = (Bitmap) result;
					if(view instanceof ImageView){
						((ImageView)view).setImageBitmap(bmp);
					}else{
						BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
						view.setBackgroundDrawable(bitmapDrawable);
					}
				}//文本 
				else if(result instanceof String){
					if(view instanceof TextView){
						((TextView)view).setText(result.toString());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 异步加载图片
	 * @param view ： 远程图片加载完成后所附的对象, 
	 * @param imgNetUrl : 远程图片的链接地址
	 * @param attch : 
	 * @return : 返回图片加载完成后保存在本地的图片地址
	 */
	public void start(View view, String imgNetUrl, ImageType type){
		this.view = view;
		this.execute(imgNetUrl, type);
	}
	
}
