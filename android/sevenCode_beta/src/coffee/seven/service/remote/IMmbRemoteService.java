package coffee.seven.service.remote;

import java.util.List;

import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;
import coffee.seven.bean.Sales;

public interface IMmbRemoteService extends IMmbService {
	
	//method
	public long getServerTime() throws Exception;
	/**
	 * 查询所有的sale列表的ID以及最后更新时间
	 */
	public Sales getSaleUpdateTimeList(int tryCount);
	/**
	 * 查询指定（多个）ID的【基本】信息
	 */
	public List<SaleBean> getSaleBaseList(String ids);
	/**
	 * 查询指定（多个）ID的【详细】信息
	 */
	public SaleBean getSaleDetail(int id);
	
	public List<SaleBean> getSaleRemainCount();
	
	/**
	 * @param order : 传入一个bean对象， 
	 * @return ： 返回orderbean ， 比传入的order多了orderId， orderStatus 两个属性
	 */
	public OrderBean submitOrder(OrderBean order);
	
	public List<OrderBean> getOrderList(List<Integer> orderIds) ;
}
