package coffee.seven.service.remote;

/**
 * 该接口只定义了一系列
 * 常量 
 * @author wangtao
 */
public interface IMmbService {
	
	//以下是request.getParameter的参数
	public final String ACTION = "action";
	public final String SALE_ID = "sale_id";	//
	public final String SALE_IDS = "sale_ids";	//查询多个sale
	
	//以下是ACTION的值
	public final int GET_SERVER_TIME = 1;		//查询当前时间
	public final int SUBMIT_ORDER = 2;			//提交表单
	
	public final int GET_SALE = 3;				//查询当前活动 sale
	public final int GET_SALE_UPDATETIME_LIST = 4;	//获取活动更新时间列表
	public final int GET_SALE_BASE_LIST = 5;	//查询需要更新的sale【基本（列表页）】信息
	public final int GET_SALE_DETAIL = 6;		//查询需要更新的sale【详细（详情页）】信息
	
	public final int GET_SALE_REMAIN_COUNT = 7;	//查询活动剩余商品数量
	public final int GET_ORDER_LIST = 8;		//查询交易成功的订单记录
	public final int GET_ORDER_STATUS = 9;		//查询交易成功的订单记录
}
