package coffee.seven.bean;

import org.droid.util.sqlite.annotation.Bean;
import org.droid.util.sqlite.annotation.Id;

/**
 * 活动
 * @author wangtao
 */
@Bean(name="coffee_sale")
public class SaleBean{
	@Id(isAuto=false)	
	private int id;				//活动的主键[非自增-从服务器端获取]
	
	private String name;		//活动名称

	public SaleBean(){
	}
	public SaleBean(String name){
		this.name = name;
	}
	
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
}
