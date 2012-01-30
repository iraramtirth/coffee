package coffee.seven.activity;

import static coffee.seven.action.SaleUtils.saleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.droid.util.sqlite.TSqliteUtils;
import org.droid.util.sys.VersionUpdateUtils;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.widget.TextView;
import coffee.seven.Intents;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.SaleService;
import coffee.seven.action.SaleUtils;
import coffee.seven.activity.adapter.SaleListAdapter;
import coffee.seven.bean.GoodsBean;
import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;

/**
 * 正在抢购
 * @author wangtao
 */
public class SaleNowActivity extends SaleActivity {
	public static SaleNowActivity context;
	
	private RemainCountHandler remainCountHandler;
	/**
	 * K:sale.getId()
	 * V:TextView
	 */
	private Map<Integer, TextView> remainCountMap = new HashMap<Integer, TextView>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;

		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.sale_now);
		bundle.putInt(KEY_TITLE_DRAWABLE, R.drawable.common_title_0);
		super.onCreate(bundle);

		final ProgressDialog pd = new ProgressDialog(this);
		// 子类中实例化
		mRunnable = new Runnable() {
			@Override
			public void run() {
				// 查询商品数据
				saleNowAdapter = new SaleListAdapter(context, true);
				saleListView.setAdapter(saleNowAdapter);
				// 查询活动通知
				saleService.getSaleNotifyList();
				// 开启服务
				startService();
				// 加载失败
				if (saleList.size() == 0) {
					mHandler.sendEmptyMessage(0);
				}
				pd.cancel();
			}
		};
		//
		mHandler.postDelayed(mRunnable, 1000 * 1 / 3);
		pd.setMessage("    正在载入...");
		pd.show();
	}

	protected void startService() {
		super.startRemainTimeService();
		this.startRemainCountService();
		super.refreshScrollText();
		if (SysConfig.ENABLE_VERSION_UPDATE) {
			// 检查版本更新
			if (saleService.requireCheckVersion()) {
				return;
			}
		}
		VersionUpdateUtils.start(this, false, SysConfig.FROM);
	}

	private void startRemainCountService() {
		remainCountHandler = new RemainCountHandler();
		timerCount.schedule(new TimerTask() {
			@Override
			public void run() {
				Message msg = remainCountHandler.obtainMessage();
				//远程获取最新的剩余活动数量列表
				msg.obj = saleService.getLastSaleRemainCount();
				remainCountHandler.sendMessage(msg);
			}
		}, 1000 * 10, SysConfig.REMAIN_COUNT_PERIOD); // 每10分钟执行一次
	}

	///////////////////////// 活动商品剩余数量 \\\\\\\\\\\\\\\\\\\\\\\\
	Timer timerCount = new Timer();

	class RemainCountHandler extends Handler {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			//saleList从远程获取的sale列表
			List<SaleBean> saleList = (List<SaleBean>) msg.obj;
			for (SaleBean sale : saleList) {
				//更新本地的
				saleService.updateSaleRemainCount(sale);
				String remainCount = saleService.getRemainCountText(sale);
				remainCountMap = saleNowAdapter.getRemainCountViewMap();
				// 已售完
				TextView remainCountView = remainCountMap.get(sale.getId());
				if (remainCountView != null) {
					remainCountView.setText(Html.fromHtml(remainCount + ""), 
							TextView.BufferType.SPANNABLE);
				}
			}
		}
	}
	/////////////////////////////// END \\\\\\\\\\\\\\\\\\\\\
	
	/**
	 * 表单提交 -- 剩余数量减少1
	 */
	public static class RemainCountReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//减1
			if(intent.getAction().equalsIgnoreCase(Intents.ACTION_SALE_REMAIN_COUNT_REDUCE)){
				OrderBean order = intent.getParcelableExtra(KEY_EXTRA_ORDER);
				if(order != null){
					String  tableName = TSqliteUtils.getTableName(GoodsBean.class);
					String sql = "update " + tableName + " set remainCount = remainCount - 1" 
						 + " where id = " + order.getGoodsCode();
					
					SaleService saleService = SaleUtils.saleService;
					saleService.executeSql(sql);
					
					Map<Integer, TextView> remainCountMap = 
						saleNowAdapter.getRemainCountViewMap();
					// 已售完
					TextView remainCountView = remainCountMap.get(order.getSaleId());
					SaleBean sale = saleService.getSaleBase(order.getSaleId());
					String remainCount = saleService.getRemainCountText(sale);
					
					if (remainCountView != null) {
						remainCountView.setText(Html.fromHtml(remainCount + ""), 
								TextView.BufferType.SPANNABLE);
					}
				}
			}
		}
		
	}
}
