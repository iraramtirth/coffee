package coffee.seven.bean;

import org.droid.util.sqlite.annotation.Bean;
import org.droid.util.sqlite.annotation.Id;
import org.droid.util.xml.parser.annotation.XmlRootElement;

@XmlRootElement(name="info")
@Bean(name="mmb_goods_info")
public class GoodsInfoBean {
	@Id(isAuto=true)
	private int id;
	private int saleId;	
	private String title;	//详细介绍
	private String alt;		//商品明细
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSaleId() {
		return saleId;
	}
	public void setSaleId(int saleId) {
		this.saleId = saleId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
}
