package coffee.seven.activity;

import static coffee.seven.action.SaleUtils.saleService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.droid.util.lang.DateUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.MmbSystem;
import coffee.seven.action.SaleObserver;
import coffee.seven.activity.adapter.SaleListAdapter;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.bean.SaleBean;
/**
 * 正在抢购 ： 下期预告
 * 父类
 * @author wangtao
 */
public abstract class SaleActivity extends BaseActivity implements OnScrollListener, OnTouchListener{
	
	protected Map<Integer, TextView[]> remainTimeViewMap = new HashMap<Integer, TextView[]>();
	
	/**
	 * 正在抢购、下期预告的活动。由子类赋值
	 */
	protected List<SaleBean> saleList = new ArrayList<SaleBean>();
	
	
	protected ListView saleListView;
	protected static SaleListAdapter saleNowAdapter, saleNextAdapter;
	
	protected ImageView animImage;
	//顶部滚动通知
	protected ViewFlipper viewFlipper;
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewFlipper = (ViewFlipper) this.findViewById(R.id.notify_view_flipper);
		animImage = (ImageView) this.findViewById(R.id.notify_broad);
		saleListView = (ListView) this.findViewById(R.id.sale_list);
		saleListView.setOnScrollListener(this);
		saleListView.setOnTouchListener(this);
		saleList = saleService.getSaleBaseListAll();
		
		remainTimeHandler = new RemainTimeHandler();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				//MmbSystem.retry(MainTabActivity.context);
			}
		};
	};
	//需要在父类中实例化
	protected Runnable mRunnable = null;
	
	public void refreshScrollText(){
		viewFlipper.removeAllViews();
		
		TextView view = (TextView)View.inflate(SaleActivity.this, R.layout.common_nofity_item, null);
		Calendar cal = Calendar.getInstance();
		Date nowDate = new Date(SysConfig.getNowTime());
		cal.setTime(nowDate);
		view.setText("今天是" + DateUtils.format(nowDate) + "  星期" + DateUtils.getWeek(cal.get(Calendar.DAY_OF_WEEK)));
		viewFlipper.addView(view);
		
		for(Integer key : SaleObserver.scrollNotifyMap.keySet()){
			view = (TextView)View.inflate(this, R.layout.common_nofity_item, null);
			view.setText(SaleObserver.scrollNotifyMap.get(key));
			viewFlipper.addView(view);
		}
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_up_out));
		viewFlipper.startFlipping();
	}
	
	//倒计时
	protected static Timer timer;
	protected static RemainTimeHandler remainTimeHandler;
	
	protected void startRemainTimeService() {
		if(timer == null){
			timer = new Timer();
		}
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (SysConfig.ENABLE_TIMER) {
					Message msg = remainTimeHandler.obtainMessage();
					msg.obj = SysConfig.getNowTime();
					remainTimeHandler.sendMessage(msg);
				}
			}
		}, 1000, 1000);
	}
	///////////////////////// 活动剩余时间 \\\\\\\\\\\\\\\\\\\\\\\\
 	protected class RemainTimeHandler extends Handler{
		private long[] times;
		private TextView[] remainTimeView;
		private int tag =0;
		
		
		@Override // 1 秒钟执行一次
		public void handleMessage(Message msg) {
			if(tag >= SysConfig.NOTITY_PERIOD * saleList.size()){
				tag = 0;
			}
			//注意：remainTimeViewMap中key（saleID）的顺序要与saleList中的保持一致
			for(Iterator<SaleBean> it = saleList.iterator(); it.hasNext();){
				SaleBean sale = it.next();
				if(saleNowAdapter == null){
					continue;
				}
				tag ++;
				remainTimeViewMap = saleNowAdapter.getRemainTimeViewMap();
				remainTimeView = remainTimeViewMap.get(sale.getId());
				times = saleService.getRemainTimeArray(sale);
				
				if(times != null && times[0] > 1000){//时间错误
					MmbSystem.retry(MainTabActivity.context);
					timer.cancel();
					break;
				}
				//时间到。 活动结束
  				if(SysConfig.getNowTime() > sale.getEndTime()){
					changeSale(sale, true);
					it.remove(); //注意 一定要删除该记录
				}else{//活动 ： 未开始 -->开始
					if(Math.abs(SysConfig.getNowTime() - sale.getStartTime()) < 1001){
						try {
							Thread.sleep(1000 * 3);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						changeSale(sale, false);
						if(saleNextAdapter != null){
							saleNextAdapter.notifyDataSetChanged();
						}
					}
					if(remainTimeView != null && times != null){
						for(int j=0; j<times.length; j++){
							remainTimeView[j].setText(String.valueOf(times[j]));
						}
					}
				}
			}
		}
	}
 	
 	/**
 	 * 活动结束进行的操作
 	 * @param i
 	 */
 	protected void changeSale(SaleBean sale, boolean isEnd) {
 		if(isEnd){
 			sale.setEnd(true);
 			saleService.deleteSaleAll(sale.getId());
 		}
 		sale.setChanged();
 		sale.notifyObservers();
		saleNowAdapter.notifyDataSetChanged();
		refreshScrollText(); //刷新顶部滚动
		it = null;//重置
	}
	
	protected Iterator<Integer> it;
	protected String scrollString = "";

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	/**
	 * 触摸saleListView的时候计时器停止
	 */
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

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
			SysConfig.ENABLE_TIMER = true;
		}
		if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
			SysConfig.ENABLE_TIMER = false;
		}
	}

}
