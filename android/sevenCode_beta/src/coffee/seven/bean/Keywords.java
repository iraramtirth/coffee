package coffee.seven.bean;

import org.droid.util.sqlite.annotation.Bean;
import org.droid.util.sqlite.annotation.Id;

/***
 * 热门关键字
 * @author coffee
 */
@Bean(name="coffee_keywords")
public class Keywords {
	@Id(isAuto=true)
	private int id;
	private String name;
	
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
