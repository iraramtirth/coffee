package coffee.servlet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import coffee.database.core.TUtils;

/**
 * 参数反射 该类是一个工具类； 在使用的过程中应该先执行 invoke方法，传入俩参数： 将从request获取的参数map进行解析 并进行参数值的反射
 * 
 * @author coffee
 */
public class ParameterReflect extends TUtils {
	Logger log = Logger.getLogger(this.toString());

	/**
	 * 重载方法：主要是在 action 中用
	 * 
	 * @param request
	 * @param actionOrModel
	 * @return
	 */
	public <T> T invoke(HttpServletRequest request, Class<T> actionOrModel) {
		// string - string[]
		Map<String, String[]> params = request.getParameterMap();
		Map<String, Object> newParams = new HashMap<String, Object>();
		for (String key : params.keySet()) {
			String[] values = (String[]) params.get(key);
			String value = "";
			for (int i = 0; i < values.length; i++) {
				value += values[i];
				if ((i + 1) < values.length) {
					value += ",";
				}
			}
			newParams.put(key, value);
		}
		T obj = null;
		try {
			obj = actionOrModel.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.invoke(newParams, obj);
	}

	/**
	 * 执行参数反射： 将action中的属性赋于从form/url取得的值
	 * 
	 * @param action
	 *            ：当前正在执行的action，或者带有setter | getter 的对象
	 */
	public <T> T invoke(Map<String, Object> parameterMap, T model) {
		if (parameterMap.size() == 0 || model == null) {
			return null;
		}
		for (String key : parameterMap.keySet()) {
			// primitive属性
			if (key.lastIndexOf(".") == -1) {
				setValue(model, key, parameterMap.get(key));
			} else {// 非private属性; 处理user.username之类的属性
				try {
					String[] params = key.split("\\.");
					Object tmpModel = model;
					Object fieldValue = null;
					for (int i = 0; i < params.length - 1; i++) {
						String fieldName = params[i];
						fieldValue = getValue(tmpModel, fieldName);
						Field field = tmpModel.getClass().getDeclaredField(
								fieldName);
						field.setAccessible(true);
						fieldValue = field.get(tmpModel);
						if (fieldValue == null) {
							fieldValue = field.getType().newInstance();
							field.setAccessible(true);
							setValue(tmpModel, fieldName, fieldValue);
						}
						tmpModel = fieldValue;
					}

					setValue(tmpModel, params[params.length - 1],
							parameterMap.get(key));

				} catch (NoSuchFieldException e) {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return model;
	}

	public static void main(String[] args) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", "111");
		map.put("user.username", "222");
		// map.put("user.child.username", "333");
		System.out.println();
		ParameterReflect pr = new ParameterReflect();
		User user = new User();
		user = pr.invoke(map, user);
		System.out.println("......." + user.getUsername()
				+ user.getUser().getUsername());
	}

}

class User {

	private String username;
	private User user;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return username;
	}
}