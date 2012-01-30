package coffee.seven.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import coffee.seven.R;
import coffee.seven.activity.adapter.SaleListAdapter;
import coffee.seven.service.SubRemindService;

/**
 * 下期预告
 * @author wangtao
 */
public class SaleNextActivity extends SaleActivity implements OnScrollListener{
	public static SaleNextActivity context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.sale_next);
		bundle.putInt(KEY_TITLE_DRAWABLE, R.drawable.common_title_1);
		super.onCreate(bundle);
		
		saleListView = (ListView) this.findViewById(R.id.sale_list);
		saleListView.setOnScrollListener(this);
		
		final ProgressDialog pd = new ProgressDialog(this);
		mRunnable = new Runnable() {
			@Override
			public void run() {
				//查询商品数据
				saleNextAdapter = new SaleListAdapter(context, false);
				saleListView.setAdapter(saleNextAdapter);
				//开启服务
				startService();
				pd.cancel();
			}
		};
		mHandler.postDelayed(mRunnable, 1000 * 1/3);
		pd.setMessage("    正在载入...");
		pd.show();
	}
	 
	public void startService() {
		//注意该方法在SaleNowActivity中调用
		//super.startRemainTimeService();
		//新增 订阅提醒服务
		Intent subIntent = new Intent();
		subIntent.setClass(this, SubRemindService.class);
		this.getApplicationContext().startService(subIntent);
		super.refreshScrollText();
	}
}
