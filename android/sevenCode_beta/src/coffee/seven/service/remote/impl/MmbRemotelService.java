package coffee.seven.service.remote.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.droid.util.http.HttpClient;
import org.droid.util.lang.StringUtils;
import org.droid.util.xml.parser.XmlParser;

import coffee.seven.App;
import coffee.seven.SysConfig;
import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;
import coffee.seven.bean.Sales;
import coffee.seven.service.remote.IMmbRemoteService;
import coffee.seven.service.remote.IMmbService;

/**
 * :mmb远程服务的本地实现
 * 利用HttpClient从MMBserver端获取数据
 * @author wangtao
 */
public class MmbRemotelService implements IMmbRemoteService {

	private String linkUrl;
	
	public MmbRemotelService(){
		this.linkUrl = SysConfig.SERVER_URL;
	}
	
	@Override
	public long getServerTime() throws ParseException {
		String query = "?action=" + GET_SERVER_TIME;
		String serverTime = new HttpClient().get(linkUrl + query) + "";
		try{
			if("".equals(serverTime)){
				serverTime = System.currentTimeMillis() + "";
			}
			long time = Long.valueOf(serverTime).longValue();
			return time;
		}catch(Exception e){
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	/**
	 * http://localhost/mmb_server_android/android?action=9&code=D11092260799&phone=13421272187
	 * http://localhost/mmb_server_android/android?action=2&groupRateId=375&productCode=40122330
	 * &name=李北金&address=测试测试测试测试测试&phone=13521262171
	 */
	@Override
	public OrderBean submitOrder(OrderBean order) {
		//返回的tip值：如果status是0，tip代表订单编号；如果status是1，tip代表错误提示。
		
		Map<String, String> map =  new HashMap<String, String>();
		map.put("action", SUBMIT_ORDER + "");
		map.put("groupRateId", order.getSaleId() + "");
		map.put("productCode",  order.getGoodsCode() + "");
		map.put("name", order.getCustomerName());
		map.put("phone", order.getCustomerPhone());
		map.put("address", order.getCustomerAddress());
		map.put("sim", App.context.SIM);	//sim卡	
		map.put("imei", App.context.IMEI);	//
		map.put("from", App.context.FROM);
		
		String xmlResult = new HttpClient().post(linkUrl , map);
		
		OrderBean bean = new XmlParser(xmlResult).pullerT(OrderBean.class);
		if(bean != null){
			order.setOrderStatus(bean.getOrderStatus());
			//成功
			if("0".equals(order.getOrderStatus())){
				order.setOrderId(bean.getOrderInfo());
			}else{//失败
				order.setOrderInfo(bean.getOrderInfo());
			}
		}
		return order;
	}

	/**
	 * /**
	 * 查询当前所有应该展示的限时抢购活动
	 * @return 数据数据 ：  XML
	  <sales nowTime="">
	  	<saleList>
		    <sale id="1" lastUpdateTime="2011-09-06 00:00:00"/>
		    <sale id="2" lastUpdateTime="2011-09-07 00:00:00"/>
			<sale id="3" lastUpdateTime="2011-09-08 00:00:00"/>
			<sale id="4" lastUpdateTime="2011-09-08 00:00:00"/>
			<sale id="5" lastUpdateTime="2011-09-08 00:00:00"/>
		</saleList>
	 * </sales>
	 */
	@Override
	public Sales getSaleUpdateTimeList(int tryCount){
		if(tryCount == 0){
			return null;
		}
		String query = "?action=" + GET_SALE_UPDATETIME_LIST 
				+ "&" + App.context.getQueryArgs();
		String xmlResult = new HttpClient().get(linkUrl + query) + "";
		if(StringUtils.isNotEmpty(xmlResult)){
			Sales sales = new XmlParser(xmlResult).pullerT(Sales.class);
			return sales;
		}else{
			tryCount--;
			return getSaleUpdateTimeList(tryCount);
		}
	}
	/**
	 * 查询sale的基本信息
	 */
	@Override
	public List<SaleBean> getSaleBaseList(String ids){
		String query = "?action=" + GET_SALE_BASE_LIST + "&" + SALE_IDS + "=" + ids
				+ "&" + App.context.getQueryArgs();
		String xmlResult = new HttpClient().get(linkUrl + query) + "";
		List<SaleBean> lst = new XmlParser(xmlResult).pullerList(SaleBean.class);
		return lst;
	}
	
	@Override
	public SaleBean getSaleDetail(int id) {
		String query = "?action=" + GET_SALE_DETAIL + "&" + SALE_ID + "=" + id
			+ "&" + App.context.getQueryArgs();
		String xmlResult = new HttpClient().get(linkUrl + query) + "";
		if(xmlResult != null && xmlResult.trim().length() > 0){
			SaleBean sale = new XmlParser(xmlResult).pullerT(SaleBean.class);
			return sale;
		}else{
			return null;
		}
	}

	/**
	 * 获取所有sale的【商品剩余数量】最新数据
	 */
	@Override
	public List<SaleBean> getSaleRemainCount() {
		String query = "?action=" + GET_SALE_REMAIN_COUNT
			+ "&" + App.context.getQueryArgs();
		String xmlResult = new HttpClient().get(linkUrl + query) + "";
		List<SaleBean> saleList = new XmlParser(xmlResult).pullerList(SaleBean.class);
		return saleList;
	}

	@Override
	public List<OrderBean> getOrderList(List<Integer> orderIds) {
		StringBuilder sb = new StringBuilder();
		for (Integer orderId : orderIds) {
			sb.append(orderId);
		}
		sb.deleteCharAt(sb.length() - 1);
		String query = "?action=" + GET_ORDER_LIST;
		query += "&ids="+sb.toString() + "&" + App.context.getQueryArgs();
		String xmlResult = new HttpClient().get(linkUrl + query) + "";
		List<OrderBean> orderList = new XmlParser(xmlResult).pullerList(OrderBean.class);
		return orderList;
	}
	
	/**
	 * 查询订单状态
	 * @param phone
	 * @param orderCode
	 */
	public String getOrderStatus(String phone, String orderCode){
		String query = "?action="+IMmbService.GET_ORDER_STATUS
			+"&phone=" + phone +"&order="+orderCode + "&" + App.context.getQueryArgs();
		String xmlResult = new HttpClient().get(linkUrl + query) + "";
		return xmlResult;
	}
	
}