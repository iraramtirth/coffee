package org.coffee.seven.activity.base;

/**
 * 该接口主要定义一些常量 
 * @author wangtao
 */
public interface IActivity {
	//flipper相关
	public final String ACTIVITY_CLASS_NAME = "ACTIVITY_CLASS_NAME";
	public static final String ROOT_ACTIVITY_ID = "ROOT_ACTIVITY_ID";
	
	public final String ROOT = "ROOT";
	
	public final String LEVEL_2 = "LEVEL_2"; 
	public final String LEVEL_3 = "LEVEL_3"; 
	public final String LEVEL_4 = "LEVEL_4";
	
	public final String LEVEL_2_ = "LEVEL_2_"; 
	public final String LEVEL_3_ = "LEVEL_3_"; 
	public final String LEVEL_4_ = "LEVEL_4_";

	//标题
	public String ACTIVITY_TITLE = "ACTIVITY_TITLE";
	//intent传参相关
	public final String KEY_EXTRA_SALE = "SALE";
	public final String KEY_EXTRA_SALE_ID = "SALE_ID";
	public final String KEY_EXTRA_SALE_STARTTIME = "SALE_STARTTIME";
	public final String KEY_EXTRA_GOODS_CODE= "GOODS_CODE";
	public final String KEY_EXTRA_ORDER = "ORDER";		//表单
	public final String KEY_EXTRA_REMAIN_COUNT = "REMAIN_COUNT";//商品剩余量
	public final String KEY_EXTRA_VIEW = "VIEW";
	public final String KEY_EXTRA_VIEW_KEY = "VIEW_KEY";
	public final String KEY_EXTRA_URL = "URL";
	public final String KEY_EXTRA_IMAGE = "IMAGE";
	public String KEY_LAYOUT_RES = "LAYOUT_RES";
	public String KEY_TITLE_DRAWABLE = "TITLE_DRAWABLE";
	public String KEY_TITLE_TEXT = "TITLE_TEXT";
	public String KEY_WEBVIEW_URL = "URL";
	
	public String SERVER_TIME = ""; // 服务器端的时间
	public final String PREF_IS_FIRST_LOGIN = "IS_FIRST_LOGIN"; //是否首次登录
	public final String PREF_LAST_VISIT_TIME = "PREF_LAST_VISIT_TIME";//最后访问时间
	public final int ID_NOTI_REMIND_LOGIN = 0x0000001;
	public final int ID_NOTI_REMIND_SUB = 0x0000002;
	public final int ID_NOTI_PRE_LOADING = 0x0000003;
		
	
}
