package coffee.seven.activity.adapter;

import java.util.List;

import org.droid.util.AsyncLoader;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.action.SaleService.ImageType;
import coffee.seven.bean.GoodsImageBean;
import coffee.seven.bean.SaleBean;

public class SaleDetailsGallerySmallAdapter extends BaseAdapter {

	private Activity context;
	
	private List<GoodsImageBean> imageList;

	//缓存item
//	private Map<Integer, View> cacheItemMap = new HashMap<Integer, View>();
	
	public SaleDetailsGallerySmallAdapter(Activity context, SaleBean sale){
		if(sale != null){
			this.imageList = sale.getImageList();
		}
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return this.imageList.size();
	}

	/**
	 * 返回大图的url
	 */
	@Override
	public Object getItem(int position) {
		
		return imageList.get(position).getUrlBig();
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
//		if(cacheItemMap.get(position) != null){
//			return cacheItemMap.get(position);
//		}
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			//加载小图
			convertView = context.getLayoutInflater().inflate(R.layout.sale_detail_gallery_item_small, parent, false);
			holder.img = (ImageView) convertView.findViewById(R.id.gallery_image_url);
			holder.alt = (TextView) convertView.findViewById(R.id.gallery_image_alt);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//缓存item
		//cacheItemMap.put(position, convertView);
		
		holder.alt.setText(this.imageList.get(position).getAlt());
		//加载远程图片
		final String imgNetUrl = this.imageList.get(position).getUrl();
//		Intent intent = new Intent();
//		intent.setAction(Intents.ACTION_LOADING_ONE);
//		//缓存该view
//		ViewCacher.add(holder.img);
//		intent.putExtra(IActivity.KEY_EXTRA_IMAGE, 
//				new Image(imgNetUrl, holder.img.toString(), ImageType.SALE_DETAIL_SMALL));
//		context.sendBroadcast(intent);
		
		try{
			//异步远程加载
			new AsyncLoader() {
			}.start(holder.img, imgNetUrl, ImageType.SALE_DETAIL_SMALL);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		ImageView img;	// 商品图片
		TextView alt;	// 图片描述
	}

}
