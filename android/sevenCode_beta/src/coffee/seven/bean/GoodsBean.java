package coffee.seven.bean;

import org.droid.util.sqlite.annotation.Bean;
import org.droid.util.sqlite.annotation.Id;
import org.droid.util.xml.parser.annotation.XmlElement;
import org.droid.util.xml.parser.annotation.XmlRootElement;

/**
 * 活动商品 
 * @author wangtao
 */
@XmlRootElement(name="goods")
@Bean(name="mmb_goods")
public class GoodsBean {
	@Id(isAuto=false)
	@XmlElement(name="code")
	private long id;
	private int saleId;			//活动ID
	private String linkName;	//链接name
	private int remainCount;	//剩余数量
	
	private SaleBean sale;	
	
	/////
	public long getId() {
		return id;
	}
	public void setId(long code) {
		this.id = code;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	public SaleBean getSale() {
		return sale;
	}
	public void setSale(SaleBean sale) {
		this.sale = sale;
	}
	 
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public int getRemainCount() {
		return remainCount;
	}
	public void setRemainCount(int remainCount) {
		this.remainCount = remainCount;
	}
}
