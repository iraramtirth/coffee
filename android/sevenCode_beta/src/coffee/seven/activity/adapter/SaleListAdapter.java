package coffee.seven.activity.adapter;

import static coffee.seven.action.SaleUtils.saleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.droid.util.AsyncLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.SaleService.ImageType;
import coffee.seven.activity.SaleDetailsActivity;
import coffee.seven.activity.base.IActivity;
import coffee.seven.activity.base.TabFlipperActivity;
import coffee.seven.bean.SaleBean;
import coffee.seven.service.SubRemindService;

/**
 * sale列表
 * @author wangtao
 * 注意：LinkedHashMap中key（saleID）的顺序要与saleList中的保持一致
 */
public class SaleListAdapter extends BaseAdapter implements OnTouchListener{
	
	private static boolean isLoading = false; //加载状态。当正在加载sale的时候不允许点击其他sale 
	/**
	 * 缓存listItem
	 */
	private Map<Integer, View> cacheConvertView = new HashMap<Integer, View>();

	private List<SaleBean> saleList = new ArrayList<SaleBean>();
	/**
	 * k : sale的ID
	 * v : remainTimeView
	 */
	public static Map<Integer,TextView[]> remainTimeViewMap = new HashMap<Integer, TextView[]>();
	
	public static Map<Integer,TextView> remainCountViewMap = new HashMap<Integer, TextView>();
	
	private Activity context;
	
	private Map<Integer, Boolean> subMap;
	//true 正在抢购， false 下期预告
	private boolean isNow;
	//
	public SaleListAdapter(Activity context, boolean isNow){
		this.context = context;
		this.isNow = isNow;
		if(isNow){
			this.saleList = saleService.getSaleBaseListNow();
		}else{
			this.saleList = saleService.getSaleBaseListNext();
			subMap = new HashMap<Integer, Boolean>();
			//订阅的活动
			List<SaleBean> subSales = saleService.getSubSaleList();
			for(SaleBean sale : subSales){
				subMap.put(sale.getId(), true);
			}
		}
	}
	
	@Override
	public int getCount() {
		return saleList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return position;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SaleBean sale = saleList.get(position);
		convertView = cacheConvertView.get(sale.getId());
		//从缓存中去
		if(convertView != null){
			return convertView;
		}
		//注意：remainTimeViewMap.size() < getCount() 这个条件保证 每个item的holder.remainTimeView 都是新创建的，而不是缓存的
		convertView = context.getLayoutInflater()
						.inflate(R.layout.sale_home_list_item, parent, false);
		ViewHolder holder = new ViewHolder();
		holder.saleIdView = (TextView) convertView.findViewById(R.id.sale_id);
		holder.remainCountView = (TextView) convertView.findViewById(R.id.sale_home_remain_count);
		holder.remainTimeTag = (TextView) convertView.findViewById(R.id.sale_home_remain_time_tag);
		holder.remainTimeView[0] = (TextView) convertView.findViewById(R.id.sale_home_remain_time_0);
		holder.remainTimeView[1] = (TextView) convertView.findViewById(R.id.sale_home_remain_time_1);
		holder.remainTimeView[2] = (TextView) convertView.findViewById(R.id.sale_home_remain_time_2);
		holder.remainTimeView[3] = (TextView) convertView.findViewById(R.id.sale_home_remain_time_3);
	 
		//缓存Item
		cacheConvertView.put(sale.getId(), convertView);
		//剩余数量
		remainCountViewMap.put(sale.getId(), holder.remainCountView);
		//时间
		remainTimeViewMap.put(sale.getId(), holder.remainTimeView);

		try {
			//设置ID
			holder.saleIdView.setText(sale.getId() + "");
			//设置剩余数量
			holder.remainCountView.setText(saleService.getRemainCountText(sale));
			
			long[] times = saleService.getRemainTimeArray(sale);
			if(times != null){
				for(int i = 0; i<times.length; i++){
					holder.remainTimeView[i].setText(times[i]+"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final ViewSwitcher switcher = (ViewSwitcher) convertView.findViewById(R.id.sale_list_buy_switcher);
		//抢购
		final Button buyAction = (Button) convertView.findViewById(R.id.sale_list_buy_btn);
		//活动未开始
		if(sale.getStartTime() > SysConfig.getNowTime()){
			//单击抢购
			buyAction.setOnClickListener(null);
			//单击item
			convertView.setOnClickListener(null);
			holder.remainTimeTag.setText(context.getResources().getString(R.string.sale_home_remain_time_tag_1));
			buyAction.setBackgroundResource(R.drawable.sale_list_sub_action_selector);
			//未开始
			View.OnClickListener clickListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//订阅
					if(!"true".equals(subMap.get(sale.getId()) + "")){
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("订阅提示");
						builder.setMessage(context.getResources().getString(R.string.info_sub_success));
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								saleService.addSub(sale.getId());
								subMap.put(sale.getId(), true);
								//重新启动服务。以便重新加载最新数据
								context.startService(new Intent(context, SubRemindService.class));
							}
						});
						builder.setNegativeButton("取消", null);
						builder.show();
					}else{// 提示用户已经订阅
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("订阅提示");
						builder.setMessage(context.getResources().getString(R.string.info_sub_already));
						builder.setPositiveButton("确定", null);
						builder.setNegativeButton("取消订阅", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								saleService.cancelSub(saleList.get(position).getId());
								subMap.remove(sale.getId());
								//重新启动服务。以便重新加载最新数据
								context.startService(new Intent(context, SubRemindService.class));
							}
						});
						builder.show();
					}
				}
			};
			//单击抢购
			buyAction.setOnClickListener(clickListener);
		}else{// 活动已开始
			buyAction.setBackgroundResource(R.drawable.sale_list_buy_action_selector);
			holder.remainTimeTag.setText(context.getResources().getString(R.string.sale_home_remain_time_tag_0));
			//活动已开始， 设置活动单击事件
			View.OnClickListener clickListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(isLoading){
						return;
					}
					isLoading = true; //设置状态
					switcher.showNext();
					ImageView progress = (ImageView) switcher.findViewById(R.id.sale_list_buy_progress);
					final AnimationDrawable anim = (AnimationDrawable) progress.getBackground();
					anim.start();
					
					new AsyncTask<Void, Void, Integer>() {
						private Intent intent;
						@Override
						protected Integer doInBackground(Void... params) {
							intent = new Intent();
							intent.setClass(context, SaleDetailsActivity.class);
							intent.putExtra(IActivity.KEY_EXTRA_SALE, saleList.get(position));
							//获取最新的数据记录
							SaleBean lastSale = saleService.getSaleBase(sale.getId());
							intent.putExtra("REMAIN_COUNT", saleService.getRemainCountText(lastSale));
							//判断是否需要重新加载
							int saleId = saleList.get(position).getId();
							return saleId;
						}
						@Override
						protected void onPostExecute(Integer saleId) {
							TabFlipperActivity.saleNowActivity.show(IActivity.LEVEL_2, intent, "ROOT", true);
							switcher.showPrevious();
							anim.stop();
							isLoading = false;
						}
					}.execute();
				}
			};
			//单击抢购
			buyAction.setOnClickListener(clickListener);
			//单击item
			convertView.setOnClickListener(clickListener);
		}
		//异步加载广告图片
		final String imgNetUrl = this.saleList.get(position).getPic();
		try{
			//广告背景图
			View imageView = convertView.findViewById(R.id.sale_home_image_rLayout);
			//异步[远程加载]加载
			new AsyncLoader(){}.start(imageView, imgNetUrl, ImageType.SALE_BASE_AD);
		}catch(Exception e){
			e.printStackTrace();
		}
		//设置触屏事件
		convertView.setOnTouchListener(this);
		return convertView;
	}

	public void removeAllItem(){
		this.cacheConvertView.clear();
	}
	/**
	 * 返回  所有的 (now + next)剩余[时间]view 
	 */
	public Map<Integer,TextView[]> getRemainTimeViewMap(){
		return remainTimeViewMap;
	}
	/**
	 * 返回  所有的 (now + next)剩余[数量]view 
	 */
	public Map<Integer,TextView> getRemainCountViewMap(){
		return remainCountViewMap;
	}
	
	//从缓存中删除指定salIsaleId的view
	public void removeItemFromCache(int saleId){
		this.cacheConvertView.remove(saleId);
	}
	
	@Override
	public void notifyDataSetChanged() {
		//清空view缓存
		this.cacheConvertView.clear();
		//重新获取数据
		if(isNow){
			this.saleList = saleService.getSaleBaseListNow();
		}else{
			this.saleList = saleService.getSaleBaseListNext();
		}
		super.notifyDataSetChanged();
	}
	
	static class ViewHolder{
		TextView saleIdView;			//隐藏域
		TextView remainCountView;		//剩余数量
		TextView remainTimeTag;			//时间标签
		TextView[] remainTimeView = new TextView[4];	//天
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			SysConfig.ENABLE_TIMER = false;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			SysConfig.ENABLE_TIMER = true;
		}
		return false;
	}
}
