package coffee.code.activity;



import android.os.Bundle;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.bean.OrderBean;
import coffee.util.view.ViewUtils;

public class OrderListDetailActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle mbundle) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.order_details);
		bundle.putInt(KEY_TITLE_DRAWABLE, R.drawable.common_title_2);
		super.onCreate(bundle);
		
		OrderBean order = this.getIntent().getParcelableExtra(KEY_EXTRA_ORDER);
		
		ViewUtils.setText(this, R.id.order_id, order.getOrderId() + "");
		ViewUtils.setText(this, R.id.order_total_price, order.getOrderPrice() + " 元");
		ViewUtils.setText(this, R.id.order_create_time, order.getOrderCreateTime());
		TextView statusView = (TextView) this.findViewById(R.id.order_status);
		statusView.setText(order.getOrderStatus());
//		if(order.getOrderStatus()){
//			statusView.setTextColor(R.color.font_order_status_ok);
//		}
		//ViewUtils.setText(this, R.id.order_status, order.getOrderStatus());
		String goodsName = order.getGoodsName();
		if(!"立即订购".equals(order.getLinkName())){
			goodsName += "(" + order.getLinkName() +")";
		}
		ViewUtils.setText(this, R.id.order_goods_name, goodsName);
		ViewUtils.setText(this, R.id.order_goods_count, "1");
		ViewUtils.setText(this, R.id.order_goods_total_price, order.getGoodsPrice() + " 元");
		ViewUtils.setText(this, R.id.order_service_price, "0.0 元");
		ViewUtils.setText(this, R.id.order_total_price_2, order.getOrderPrice() + " 元");
		
		ViewUtils.setText(this, R.id.order_customer_name, order.getCustomerName());
		ViewUtils.setText(this, R.id.order_customer_phone, order.getCustomerPhone());
		ViewUtils.setText(this, R.id.order_customer_address, order.getCustomerAddress());
	}
	
}
