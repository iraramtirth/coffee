package coffee.seven.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import org.droid.util.AsyncLoader;
import org.droid.util.lang.StringUtils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.SaleService;
import coffee.seven.action.SaleService.ImageType;
import coffee.seven.bean.OrderBean;
import coffee.seven.service.remote.IService;

/**
 * order列表
 * @author wangtao
 */
public class OrderHomeListAdapter extends BaseAdapter {

	private List<OrderBean> orderList = new ArrayList<OrderBean>();
	 
	private Activity context;
	private SaleService saleService;
	
	public OrderHomeListAdapter(Activity context){
		this.context = context;
		saleService = new SaleService(context);
		orderList = saleService.getSaleOrdeList();
	}
	
	@Override
	public int getCount() {
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//
		ViewHolder holder;
		if(convertView == null){
			convertView = context.getLayoutInflater()
					.inflate(R.layout.order_home_list_item, parent, false);
			holder = new ViewHolder();
			holder.goodsName = (TextView) convertView.findViewById(R.id.order_list_goods_name);
			holder.orderId = (TextView) convertView.findViewById(R.id.order_list_order_id);
			holder.orderTotalPrice = (TextView) convertView.findViewById(R.id.order_list_total_price);
			holder.orderCreateTime = (TextView) convertView.findViewById(R.id.order_list_order_create_time);
			holder.orderStatus = (TextView) convertView.findViewById(R.id.order_list_order_status);
			holder.packId = (TextView) convertView.findViewById(R.id.order_list_packId);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final OrderBean order = orderList.get(position);
		
		holder.goodsName.setText(order.getGoodsName());
		holder.orderId.setText(order.getOrderId() + "");
		holder.orderTotalPrice.setText(order.getOrderPrice() + " 元");
		holder.orderCreateTime.setText(order.getOrderCreateTime());
		holder.orderStatus.setText("状态查询中");//默认
		
		if(order.getPackId() != null){
			convertView.findViewById(R.id.order_list_pack_layout).setVisibility(View.VISIBLE);
			holder.packId.setVisibility(View.VISIBLE);
			holder.packId.setText(order.getPackId());
		}else{
			convertView.findViewById(R.id.order_list_pack_layout).setVisibility(View.GONE);
		}
		//表单ID != 0 && phone不为空
		if(StringUtils.isNotEmpty(order.getCustomerPhone()) && order.getOrderId() != null){
			String resUrl = SysConfig.SERVER_URL + "?action="+IService.GET_ORDER_STATUS
				+"&phone="+order.getCustomerPhone()+"&code="+order.getOrderId();
			new AsyncLoader() {
				@Override
				protected void onPostExecute(Object result) {
					if(result != null && result.toString().trim().length() > 0){
						super.onPostExecute(result);
						//保存到本地数据库
						saleService.updateOrderStatus(order.getOrderId(),result.toString());
					}
				}
			}.start(holder.orderStatus, resUrl, ImageType.NONE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView goodsName;			// 商品名称
		TextView orderId;			// 订单号
		TextView orderTotalPrice;	// 订单总价
		TextView orderCreateTime;	// 创建时间
		TextView orderStatus;		// 订单状态
		TextView packId;			// 包裹单号
	}
}
