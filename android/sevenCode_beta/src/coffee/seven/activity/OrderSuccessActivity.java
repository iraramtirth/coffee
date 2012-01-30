package coffee.seven.activity;

import org.droid.util.view.ViewUtils;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.TabFlipperActivity;
import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;
/**
 * 表单提交成功 
 * @author wangtao
 */
public class OrderSuccessActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle mBundle) {
		//sale的基本信息
		final OrderBean order = this.getIntent().getParcelableExtra(KEY_EXTRA_ORDER);
		final SaleBean saleBase = this.getIntent().getParcelableExtra(KEY_EXTRA_SALE);
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.order_submit_success);
		bundle.putString(KEY_TITLE_TEXT,	saleBase.getGoodsName());
		super.onCreate(bundle);
		//订单号
		ViewUtils.setText(this, R.id.order_id, order.getOrderId() + "");
		String linkName = order.getLinkName();
		if(linkName != null && linkName.trim().length() > 0 && saleBase.getGoodsList().size() > 1){
			linkName = "(" + linkName + ")";
		}else{
			linkName = "";
		}
		String goodsName = "【商品】" +  saleBase.getGoodsName() + linkName;
		//商品名称
		ViewUtils.setText(this, R.id.order_success_goods_name, goodsName);
		//商品数量
		ViewUtils.setText(this, R.id.order_goods_count, 1 + "");
		//商品总金额
		ViewUtils.setText(this, R.id.order_goods_total_price, order.getGoodsPrice() + "元");
		//订单总金额
		ViewUtils.setText(this, R.id.order_total_price, order.getOrderPrice() + "元");
		//提示信息
		TextView successInfo = (TextView) this.findViewById(R.id.order_success_info_1);
//		successInfo.setText(Html.fromHtml());
		String info = getString(R.string.order_success_info_1);
		Spannable span = new SpannableString(info);        
		span.setSpan(new ForegroundColorSpan(Color.parseColor("#FF7200")), info.length()-13, info.length(), 
	                                               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		successInfo.setText(span);
		
		//抢购其他商品
		Button buyOtherAction = (Button) this.findViewById(R.id.order_buy_other_action);
		buyOtherAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TabFlipperActivity.saleNextActivity.show(null);
			}
		});
	}
}
