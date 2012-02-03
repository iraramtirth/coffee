package coffee.seven.activity;

import java.util.Date;

import org.droid.util.lang.StringUtils;
import org.droid.util.sqlite.DateUtils;
import org.droid.util.sqlite.DbHelper;
import org.droid.util.sys.SystemUtils;
import org.droid.util.view.ViewUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import coffee.seven.Intents;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.Alert;
import coffee.seven.action.SaleService;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.IActivity;
import coffee.seven.activity.base.TabFlipperActivity;
import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;
import coffee.seven.service.remote.IRemoteService;
import coffee.seven.service.remote.impl.MmbRemotelService;
/**
 * 订单提交 
 * @author wangtao
 */
public class OrderSubmitActivity extends BaseActivity {
	
	private OrderSubmitActivity context = this;
	
	private String activityTitle ;
	
	private SaleService saleService;
	
	//
	private SaleBean saleBase;
	private String[] input;
	private EditText phoneEdit;
	private EditText nameEdit;
	private EditText addressEdit;
	private String goodsCode;
	private String linkName;
	@Override
	protected void onCreate(Bundle mbundle) {
		saleService = new SaleService();
		//sale的基本信息
		saleBase = this.getIntent().getParcelableExtra(KEY_EXTRA_SALE);
		goodsCode = this.getIntent().getStringExtra(KEY_EXTRA_GOODS_CODE);
		if(goodsCode == null){
			goodsCode = "0";
		}
		linkName =  new SaleService().getLinkNameByCode(goodsCode);
		
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.order_submit);
		
		activityTitle = saleBase.getGoodsName();
		if(StringUtils.isNotEmpty(linkName) && saleBase.getGoodsList().size() > 1){
			activityTitle += "(" + linkName + ")";
		}
		bundle.putString(KEY_TITLE_TEXT, activityTitle);
		super.onCreate(bundle);
		//商品价格
		ViewUtils.setText(this, R.id.order_goods_price, saleBase.getPrice()+"");
		//商品总价格
		ViewUtils.setText(this, R.id.order_goods_total_price, saleBase.getPrice()+"");
		//订单总价格
		ViewUtils.setText(this, R.id.order_total_price, saleBase.getPrice()+"");
		
		Button orderSubmit = (Button) this.findViewById(R.id.order_submit_action);
		
		//表单信息
		input = saleService.getOrderInputInfo();
		
		phoneEdit = (EditText) this.findViewById(R.id.order_input_phone);
		nameEdit = (EditText) this.findViewById(R.id.order_input_name);
		addressEdit = (EditText) this.findViewById(R.id.order_input_address);
		
		if(input != null){
			phoneEdit.setText(input[0]);
			nameEdit.setText(input[1]);
			addressEdit.setText(input[2]);
		}
		if(input[0] == null){
			phoneEdit.setText(SystemUtils.getPhoneNumber());
		}
		
		//提交表单
		orderSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				input[0] = phoneEdit.getText().toString();
				input[1] = nameEdit.getText().toString();
				input[2] = addressEdit.getText().toString();
				
				String checkInfo = checkOrder(input[0], input[1], input[2], saleBase.getId());
				//表单数据不规范
				if(checkInfo != null){
					Alert.show(context, "温馨提示", new String[]{checkInfo},  new View.OnClickListener[]{null,null},
							new Integer[]{null, R.drawable.alert_dialog_return_selector});
				}else{
					pd = new ProgressDialog(context);
					pd.setMessage("正在提交订单,请稍候...");
					pd.setCancelable(false);
					pd.show();
					new OrderAsyncTask().execute();
				}
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	private ProgressDialog pd;
	
	private class OrderAsyncTask extends AsyncTask<Void, Void, OrderBean>{

		@Override
		protected OrderBean doInBackground(Void... params) {
			//发送表单
			IRemoteService remoteService = new MmbRemotelService();
			OrderBean order = new OrderBean(input[0], input[1], input[2], Integer.valueOf(goodsCode));
			order.setSaleId(saleBase.getId());
			order.setGoodsCode(StringUtils.toInt(goodsCode,0));
			//从远程返回的order
			OrderBean newOrder = remoteService.submitOrder(order);
			newOrder.setOrderCreateTime(DateUtils.format(new Date(SysConfig.getNowTime())));
			newOrder.setGoodsPrice(saleBase.getPrice());
			newOrder.setOrderPrice(saleBase.getPrice());
			newOrder.setGoodsName(saleBase.getGoodsName());
			newOrder.setLinkName(linkName);
			return newOrder;
		}
		
		@Override
		protected void onPostExecute(OrderBean newOrder) {
			//success表单提交成功
			if(newOrder.getOrderId() != null){
				//表单提交成功，此时order.getOrderStatus() == "0"
				//设置默认状态
				newOrder.setOrderStatus(context.getResources().getString(R.string.order_status_default)); //
				//插入到数据库
				DbHelper helper = new DbHelper();
				helper.insert(newOrder);
				helper.close();
				//
				Intent intent = new Intent();
				intent.setClass(context, OrderSuccessActivity.class);
				intent.putExtra(KEY_EXTRA_SALE, saleBase);
				intent.putExtra(KEY_EXTRA_ORDER, newOrder);
				TabFlipperActivity.saleNowActivity.show(LEVEL_4, intent, LEVEL_3, true);
				//广播--商品数量减1
				intent = new Intent();
				intent.setAction(Intents.ACTION_SALE_REMAIN_COUNT_REDUCE);
				intent.putExtra(IActivity.KEY_EXTRA_ORDER, newOrder);
				context.sendBroadcast(intent);
			}else{
				String info = newOrder.getOrderInfo();
				if(info == null){
					info = "网络异常, 请检查网络设置";
				}
				Alert.show(context, "温馨提示", new String[]{info}, new View.OnClickListener[]{null,null},
						new Integer[]{null, R.drawable.alert_dialog_return_selector});
			}
			//隐藏ProgressDialog
			pd.dismiss();
		}
	}
	
	/**
	 * 检查表单数据的规范性
	 * @return null : 返回null说明客户端数据校验成功
	 * 				否则返回失败信息
	 */
	public String checkOrder(String phone, String name, String address, int saleId){
		if(StringUtils.isEmpty(phone)){
			return "手机号不能为空";
		}
		if(StringUtils.isEmpty(name)){
			return "收货人姓名不能为空";
		}
		if(StringUtils.isEmpty(address)){
			return "收货地址不能为空";
		}
		if(phone.length() != 11 || !phone.matches("\\d{11}")){
			return "手机号码格式错误";
		}
		//检测该商品是否已经抢购过
		if(saleService.checkSaleUniqueInOrder(saleId) == false){
			return "你已经抢购过该商品";
		}	
		return null;
	}
}
