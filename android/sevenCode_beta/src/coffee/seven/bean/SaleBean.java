package coffee.seven.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.droid.util.sqlite.annotation.Bean;
import org.droid.util.sqlite.annotation.Column;
import org.droid.util.sqlite.annotation.Id;
import org.droid.util.sqlite.annotation.Transient;
import org.droid.util.xml.parser.annotation.GenericType;
import org.droid.util.xml.parser.annotation.XmlRootElement;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 活动
 * @author wangtao
 */
@XmlRootElement(name="sale")
@Bean(name="mmb_sale")
public class SaleBean extends Observable implements Parcelable{
	@Id(isAuto=false)
	private int id;				//活动的主键[非自增-从服务器端获取]
	
	private String name;		//活动名称
	
	@Column(length=30)
	private String goodsName;	//商品名称
	private long startTime;		//促销的起始时间 ：注意返回long
	private long endTime;
	//不限制长度
	private String pic;			//活动广告图片
	private String picLocal;	//图片加载完成以后保存的本地图片地址 
	private float price;		//现价
	private float oriPrice;		//原价
	@Column(length=30)
	private String lastUpdateTime;//最后更新时间
	/**
	 * 默认不需要。 该字段标志着当前ID的【details】数据是否需要刷新， 每次更新完成以后重置该字段为0 
	 * 该字段为1 代表从saleHome跳转到saleDetail的时候需要重新加载数据
	 */
	private int refresh = 0;
	private int isSub = 0; //是否需要
	
	@Transient
	private boolean isEnd = false; //
	
	@GenericType(type=GoodsBean.class)
	private List<GoodsBean> goodsList = new ArrayList<GoodsBean>();
	//商品图片
	@GenericType(type=GoodsImageBean.class)
	private List<GoodsImageBean> imageList = new ArrayList<GoodsImageBean>();
	//商品规格
	@GenericType(type=GoodsInfoBean.class)
	private List<GoodsInfoBean> infoList = new ArrayList<GoodsInfoBean>();
	
	
	public SaleBean(){
	}
	
	public SaleBean(String name){
		this.name = name;
	}
	
	////////
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPicLocal() {
		return picLocal;
	}
	public void setPicLocal(String picLocal) {
		this.picLocal = picLocal;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getOriPrice() {
		return oriPrice;
	}
	public void setOriPrice(float oriPrice) {
		this.oriPrice = oriPrice;
	}
	public List<GoodsBean> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsBean> goodsList) {
		this.goodsList = goodsList;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public int getRefresh() {
		return refresh;
	}
	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}
	public List<GoodsImageBean> getImageList() {
		return imageList;
	}
	public void setImageList(List<GoodsImageBean> imageList) {
		this.imageList = imageList;
	}
	public List<GoodsInfoBean> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<GoodsInfoBean> infoList) {
		this.infoList = infoList;
	}
	public int getIsSub() {
		return isSub;
	}
	public void setIsSub(int isSub) {
		this.isSub = isSub;
	}
	
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	//
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleBean other = (SaleBean) obj;
		if (id != other.id)
			return false;
		return true;
	}
	//重写， 扩大可见性
	@Override
	public void setChanged(){
		super.setChanged();
	}
}
