package coffee.code.activity;

import static coffee.seven.activity.base.TabFlipperActivity.saleHelpActivity;
import static coffee.seven.activity.base.TabFlipperActivity.saleNextActivity;
import static coffee.seven.activity.base.TabFlipperActivity.saleNowActivity;
import static coffee.seven.activity.base.TabFlipperActivity.saleOrderActivity;

import java.lang.reflect.Field;



import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.NotificationManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.Alert;
import coffee.seven.action.MmbSystem;
import coffee.seven.activity.base.IActivity;
import coffee.seven.activity.base.TabFlipperActivity;
import coffee.util.RUtils;
import coffee.util.sys.ShortCutUtils;
import coffee.util.view.ViewUtils;

/**
 * 注意：如果不继承TabActivity 则需要执行tabHost.setup(); 若是tagSpec中 tagSpec.setContent(new
 * Intent()) 则需要继承ActivityGroup tabHost.setup(this.getLocalActivityManager());
 * 
 * @author wangtao
 */
public class MainTabActivity extends TabActivity implements IActivity,
		OnTabChangeListener {

	private final String TAB0_NAME = SearchMainActivity.class.getName();
	private final String TAB1_NAME = ScanMainActivity.class.getName();
	private final String TAB2_NAME = SearchMainActivity.class.getName();
	private final String TAB3_NAME = HelpActivity.class.getName();

	private final String TAB_PRE = "tab_"; // tab名前缀
	private final String TAB_NOT_SELECTED = "0";// tab选中
	private final String TAB_SELECTED = "1"; // tab未选中

	public static MainTabActivity context;
	private TabWidget tabWidget;

	public static TabHost tabHost;
	public static LocalActivityManager mActivityManager;

	private String[] tabs = new String[4];
	{
		for (int i = 0; i < tabs.length; i++) {
			tabs[i] = TAB_PRE + i;
		}
	}

	protected int getPx(float dip) {
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		float px = dip * metrics.density;
		return (int) px;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.setContentView(R.layout.tabhost);

		tabHost = (TabHost) this.findViewById(android.R.id.tabhost);

		mActivityManager = this.getLocalActivityManager();
		tabHost.setup(this.getLocalActivityManager());
		this.tabWidget = tabHost.getTabWidget();
		// 创建选项卡
		this.createTab();
		// 添加TAB切换事件
		tabHost.setOnTabChangedListener(this);
		// 去掉底部白线 - 将tabHost向下移动7个单位
		clearBottomWhiteLine();
		this.setTabStyle();

		// 查看是否是第一次登陆
		if (isFirstLogin()) {
			showHelpDialog();
			if (ShortCutUtils.isExistShortcut(context) == false) {
				ShortCutUtils.createShortcut(context, StartupActivity.class,
						R.string.app_label, R.drawable.icon);
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		SysConfig.setLastVisitTime();
		NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notiManager.cancel(IActivity.ID_NOTI_REMIND_LOGIN);
	}

	@Override
	protected void onStop() {
		super.onStop();
		SysConfig.setLastVisitTime();
	}

	/**
	 * 创建TAB
	 */
	private void createTab() {
		// 正在抢购
		Intent intent = new Intent(this, TabFlipperActivity.class);
		intent.putExtra(ACTIVITY_CLASS_NAME, TAB0_NAME);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabHost.addTab(tabHost
				.newTabSpec(tabs[0])
				.setContent(intent)
				.setIndicator("",
						this.getResources().getDrawable(R.drawable.tab_00)));
		// 下期抢购
		intent = new Intent(this, TabFlipperActivity.class);
		intent.putExtra(ACTIVITY_CLASS_NAME, TAB1_NAME);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabHost.addTab(tabHost
				.newTabSpec(tabs[1])
				.setContent(intent)
				.setIndicator("",
						this.getResources().getDrawable(R.drawable.tab_10)));
		// 我的订单
		intent = new Intent(this, TabFlipperActivity.class);
		intent.putExtra(ACTIVITY_CLASS_NAME, TAB2_NAME);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabHost.addTab(tabHost
				.newTabSpec(tabs[2])
				.setContent(intent)
				.setIndicator("",
						this.getResources().getDrawable(R.drawable.tab_20)));
		// 更多
		intent = new Intent(this, TabFlipperActivity.class);
		intent.putExtra(ACTIVITY_CLASS_NAME, TAB3_NAME);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		tabHost.addTab(tabHost
				.newTabSpec(tabs[3])
				.setContent(intent)
				.setIndicator("",
						this.getResources().getDrawable(R.drawable.tab_30)));
	}

	/**
	 * 判断软件是不是第一次加载
	 */
	private boolean isFirstLogin() {
		SharedPreferences prefs = getSharedPreferences("mmb",
				MODE_WORLD_WRITEABLE);
		boolean isFirst = prefs.getBoolean(IActivity.PREF_IS_FIRST_LOGIN, true);
		if (isFirst == true) {
			prefs.edit().putBoolean(IActivity.PREF_IS_FIRST_LOGIN, false)
					.commit();
			return true;
		}
		return false;
	}

	/**
	 * 显示帮助
	 */
	private void showHelpDialog() {
		final View view = View.inflate(context, R.layout.help_dialog_1, null);
		final Dialog dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(view);
		dialog.show();
		Button next = (Button) view.findViewById(R.id.help_btn_next);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewUtils.setText(view, R.id.help_content_title,
						context.getString(R.string.help_dialog_2_title));
				ViewUtils.setText(view, R.id.help_content_2,
						context.getString(R.string.help_dialog_20));
				view.findViewById(R.id.help_content_1).setVisibility(View.GONE);
				Button btn = (Button) view.findViewById(R.id.help_btn_next);
				btn.setBackgroundResource(R.drawable.help_sure_action_selector);
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			}
		});
	}

	

	/**
	 * 设置Tab中的ImageView的
	 */
	private void setTabStyle() {
		try {
			// 设置tab_widget背景
			tabWidget.setBackgroundResource(R.drawable.tabwidget_bg);
			// 设置tabWidget
			tabWidget.removeAllViews();
			LinearLayout.LayoutParams lLayoutParams = new LinearLayout.LayoutParams(
					-1, -2);
			lLayoutParams.weight = 1;
			for (int i = 0; i < tabs.length; i++) {
				final ImageButton tab = (ImageButton) View.inflate(this,
						R.layout.tab, null);
				int resid = R.drawable.class.getField(
						TAB_PRE + i + TAB_NOT_SELECTED).getInt(null);
				if (i == 0) {
					resid = R.drawable.class.getField(
							TAB_PRE + i + TAB_SELECTED).getInt(null);
				}
				tab.setBackgroundResource(resid);
				tab.setLayoutParams(lLayoutParams);
				tabWidget.addView(tab, i);
				// tab添加单击事件
				tab.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						for (int i = 0; i < tabs.length; i++) {
							if (v == tabWidget.getChildAt(i)) {
								tabHost.setCurrentTab(i);
								break;
							}
						}// for end
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * TAB跳转【完成以后】执行改方法
	 */
	@Override
	public void onTabChanged(String tabId) {
		// 设置TAB的背景样式
		try {
			for (int i = 0; i < tabs.length; i++) {
				int resid = R.drawable.class.getField(
						TAB_PRE + i + TAB_NOT_SELECTED).getInt(null);
				tabWidget.getChildAt(i).setBackgroundResource(resid);
			}
			int resid = R.drawable.class.getField(tabId + TAB_SELECTED).getInt(
					null);
			// 帮助：显示菜单
			if (popMenu != null) {
				popMenu.dismiss();// 隐藏menu
			}
			tabHost.getCurrentTabView().setBackgroundResource(resid);
			if (tabId.equals(tabs[0])) { // 正在抢购
				saleNowActivity.show(ROOT);
			} else if (tabId.equals(tabs[1])) {// 下期预告
				saleNextActivity.show(ROOT);
			} else if (tabId.equals(tabs[2])) {// 我的订单{
				saleOrderActivity.show(ROOT);
			} else if (tabId.equals(tabs[3])) {// 购物须知
				saleHelpActivity.show(ROOT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @ 注意先调用该类 然后调用MmbFlipperActivity
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (popMenu != null && popMenu.isShowing()) {
				popMenu.dismiss();
				return true;
			}
			break;
		// 菜单键
		case KeyEvent.KEYCODE_MENU:
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				if (popMenu == null) {
					createPopMenu();
					popMenu.showAsDropDown(tabWidget, 0, 0);
				} else {
					if (popMenu.isShowing()) {
						popMenu.dismiss();
					} else {
						popMenu.showAsDropDown(tabWidget, 0, 0);
					}
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 创建弹出式菜单
	 */
	private PopupWindow popMenu;

	private void createPopMenu() {
		View view = View.inflate(this, R.layout.menu, null);
		// 注意只创建一次
		if (popMenu == null) {
			popMenu = new PopupWindow(view, getPx(320), getPx(134));
			for (int i = 0; i < 4; i++) {
				final int resid = RUtils.getResId(this.getPackageName()
						+ ".R$id", "menu_item_" + i);
				View menuItem = view.findViewById(resid);
				final int index = i;
				menuItem.setOnClickListener(new View.OnClickListener() {
					@SuppressWarnings("unused")
					String title = "";

					@Override
					public void onClick(View v) {
						int titleResid = RUtils.getResId(
								context.getPackageName() + ".R$string",
								"menu_item_" + index);
						String title = context.getResources().getString(
								titleResid);
						Integer[] drawableResid = new Integer[3];
						View.OnClickListener[] clickListeners = new View.OnClickListener[3];
						switch (index) { // 退出
						case 3:
							MmbSystem.exit(context);
							break;
						case 0: // 购物咨询
							Intent intent = new Intent();
							// intent.setClass(context,
							// MmbBrowserActivity.class);
							// intent.putExtra(IActivity.KEY_WEBVIEW_URL,
							// SysConfig.HELP);
							// startActivityForResult(intent,
							// SysConfig.REQUEST_CODE_MMBSITE);
							intent.setAction(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(SysConfig.HELP));
							startActivity(intent);
							popMenu.dismiss();
							break;
						case 1:// 版本
							try {
								PackageManager pManager = context
										.getPackageManager();
								PackageInfo info = pManager.getPackageInfo(
										context.getPackageName(), 0);
								drawableResid = new Integer[] { null,
										R.drawable.alert_dialog_sure_selector,
										null };
								clickListeners = new View.OnClickListener[] {
										null, null, null };
								Alert.show(context, title,
										new String[] { info.versionName },
										clickListeners, drawableResid);
							} catch (NameNotFoundException e) {
								e.printStackTrace();
							}
							break;
						case 2: // mmb主站
							intent = new Intent();
							// intent.setClass(context,
							// MmbBrowserActivity.class);
							// intent.putExtra(IActivity.KEY_WEBVIEW_URL,
							// SysConfig.MMB_HOST);
							// startActivityForResult(intent,
							// SysConfig.REQUEST_CODE_MMBSITE);
							intent.setAction(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(SysConfig.MMB_HOST));
							startActivity(intent);
							popMenu.dismiss();
							break;
						}
					}
				});
			}
		} else {
			if (popMenu.isShowing()) {
				popMenu.dismiss();
			} else {
				popMenu.showAsDropDown(tabWidget, 0, 0);
			}
		}
	}

	/**
	 * 删除底部白线
	 */
	private void clearBottomWhiteLine() {
		if (Build.VERSION.SDK_INT <= 7) {
			try {
				Field mBottomLeftStrip = tabWidget.getClass().getDeclaredField(
						"mBottomLeftStrip");
				Field mBottomRightStrip = tabWidget.getClass()
						.getDeclaredField("mBottomRightStrip");
				mBottomLeftStrip.setAccessible(true);
				mBottomRightStrip.setAccessible(true);
				mBottomLeftStrip.set(tabWidget,
						getResources().getDrawable(R.drawable.no));
				mBottomRightStrip.set(tabWidget,
						getResources().getDrawable(R.drawable.no));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			tabHost.setPadding(tabHost.getPaddingLeft(),
					tabHost.getPaddingTop(), tabHost.getPaddingRight(),
					tabHost.getPaddingBottom() - 5);
		}
	}

}
