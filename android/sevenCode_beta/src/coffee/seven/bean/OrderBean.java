package coffee.seven.bean;

import org.droid.util.sqlite.annotation.Bean;
import org.droid.util.sqlite.annotation.Id;
import org.droid.util.xml.parser.annotation.XmlElement;
import org.droid.util.xml.parser.annotation.XmlRootElement;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单信息 
 * @author wangtao
 */
@XmlRootElement(name="order")
@Bean(name="mmb_order")
public class OrderBean implements Parcelable{
	@Id(isAuto=true)
	private int id;
	//
	private String deviceId;		//设备编号[唯一]
	//customer保存于本地
	private String customerName;	//顾客-姓名
	private String customerAddress;	//地址
	private String customerPhone;	//电话
	//
	private int saleId;				//活动ID
	private int goodsCode;			//商品编码
	private int goodsCount;			//订购数量
	private float goodsPrice;		//商品价格
	private String goodsName;		//商品名称
	private String linkName;		//编码-显示名称
	//以下是从服务器端获取
	private String orderId;			//订单编号
	//返回的tip值：如果status是0，tip代表订单编号；如果status是1，tip代表错误提示。
	@XmlElement(name="tip")
	private String orderInfo;		//订单信息：成功失败》等
	@XmlElement(name="status")
	private String orderStatus;		//订单状态：提交成功以后整个交易的流程状态
	private float orderPrice;		//订单价格
	private String orderCreateTime;	//订单生成时间
	private String packId;			//包裹编号
	///
	
	public OrderBean(){
		
	}
	public OrderBean(String customerPhone, String customerName,
				String customerAddress,	 int saleId){
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerPhone = customerPhone;
		this.saleId = saleId;
	}
	
	public OrderBean(String customerName, String customerAddress,
			String customerPhone, int saleId, int goodsCode, float goodsPrice, int goodsCount) {
		super();
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerPhone = customerPhone;
		this.saleId = saleId;
		this.goodsCode = goodsCode;
		this.goodsPrice = goodsPrice;
		this.goodsCount = goodsCount;
	}

	public String getDeviceId() {
		return deviceId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	public int getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(int goodsCode) {
		this.goodsCode = goodsCode;
	}
	public float getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getGoodsCount() {
		return goodsCount;
	}
	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(String orderInfo) {
		this.orderInfo = orderInfo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public float getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(float orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getPackId() {
		return packId;
	}
	public void setPackId(String packId) {
		this.packId = packId;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	public static final Parcelable.Creator<OrderBean> CREATOR  = new Creator<OrderBean>() {
		@Override
		public OrderBean createFromParcel(Parcel source) {
			OrderBean order = new OrderBean();
			order.goodsCode = source.readInt();
			order.saleId = source.readInt();
			return order;
		}

		@Override
		public OrderBean[] newArray(int size) {
			return new OrderBean[size];
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.goodsCode);
		dest.writeInt(this.saleId);
	}
	
}
