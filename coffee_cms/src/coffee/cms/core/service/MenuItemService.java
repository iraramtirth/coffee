package coffee.cms.core.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import coffee.Config;
import coffee.cms.core.bean.MenuItemBean;

public class MenuItemService {

	/**
	 * @param key
	 * @param Values
	 * @return
	 */
	public MenuItemBean praser(ResourceBundle res, String key, String values) {
		try {
			values = new String(values.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MenuItemBean item = new MenuItemBean();
		// 主键
		item.setId(key);
		String[] valuesItem = values.split(",");
		// 菜单名
		item.setName(valuesItem[0]);
		// 菜单URL
		if ("0".equals(valuesItem[1])) {
			valuesItem[1] = null;
		}
		item.setUrl(valuesItem[1]);
		// 父节点
		if ("0".equals(valuesItem[2])) {
			valuesItem[2] = null;
		}
		item.setPid(valuesItem[2]);
		// 有子节点
		if (!"0".equals(valuesItem[3])) {
			String[] children = valuesItem[3].split("-");
			for (String childKey : children) {
				MenuItemBean child = praser(res, childKey,
						res.getString(childKey));
				item.getChildren().add(child);
			}
		}
		return item;
	}

	/**
	 * @param args
	 */
	public List<MenuItemBean> getMenuItems() {

		ResourceBundle res = ResourceBundle.getBundle(Config.CMS_MENU_PROPS,Locale.getDefault());

		List<MenuItemBean> menuItems = new ArrayList<MenuItemBean>();
		for (String key : res.keySet()) {
			if (key.startsWith("_") == false) {
				continue;
			}
			String id = key.substring(1);
			MenuItemBean item = praser(res, id, res.getString(key));
			menuItems.add(item);
		}

		System.out.println(menuItems);
		return menuItems;
	}

}
