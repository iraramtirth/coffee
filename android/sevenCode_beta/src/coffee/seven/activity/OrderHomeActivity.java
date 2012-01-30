package coffee.seven.activity;

import static coffee.seven.activity.base.TabFlipperActivity.saleOrderActivity;

import org.droid.util.view.ViewUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import coffee.seven.R;
import coffee.seven.activity.adapter.OrderHomeListAdapter;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.bean.OrderBean;

/**
 * 我的订单：订单列表 
 * @author wangtao
 */
public class OrderHomeActivity extends BaseActivity{
	
	private OrderHomeActivity context = this;
	
	@Override
	protected void onCreate(Bundle mbundle) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.order_home);
		bundle.putInt(KEY_TITLE_DRAWABLE, R.drawable.common_title_2);
		super.onCreate(bundle);
		
		OrderHomeListAdapter orderListAdapter = new OrderHomeListAdapter(this);
		ListView listView = (ListView) this.findViewById(R.id.order_home_list);
		listView.setAdapter(orderListAdapter);
		listView.setFooterDividersEnabled(false);	//去掉底部分隔符
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OrderBean order = (OrderBean) parent.getItemAtPosition(position);
				Intent intent = new Intent();
				intent.setClass(context, OrderListDetailActivity.class);
				//查询订单状态
				String orderStatus = ViewUtils.getText(view, R.id.order_list_order_status);
				order.setOrderStatus(orderStatus);
				intent.putExtra(KEY_EXTRA_ORDER, order);
				saleOrderActivity.show(LEVEL_2+order.getId(), intent, ROOT, true);
			}
		});
	}
	
}
