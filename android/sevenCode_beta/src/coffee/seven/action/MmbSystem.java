package coffee.seven.action;

import org.droid.util.RUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import coffee.seven.R;
import coffee.seven.activity.MainTabActivity;
import coffee.seven.activity.StartupActivity;

public class MmbSystem {
	/**
	 * 退出系统
	 */
	public static void exit(Activity context){
		int index = 3; 
		int titleResid =  RUtils.getResId(context.getPackageName() + ".R$string", "menu_item_"+index);
		int contentResid = RUtils.getResId(context.getPackageName() + ".R$string", "menu_content_"+index);
		
		String title = context.getString(titleResid);
		String content1 = context.getString(contentResid);
		
//		List<SaleBean> salesNext = SaleUtils.saleService.getSaleBaseListNext();
//		String content2 = null;
//		if(salesNext.size() > 0){
////			SaleBean sale = salesNext.get(0);
//			String str = "下期抢购将在[#时间#]开始，[#商品名#]仅需[#价格#]元，记得到时来看看哦";
////			str = str.replace("#时间#", "<font color=\"#FF2200\">" + DateUtils.format(sale.getStartTime()) + "</font>");
////			str = str.replace("#商品名#", "<font color=\"#FF2200\">" + sale.getGoodsName() + "</font>");
////			str = str.replace("#价格#", "<font color=\"#FF2200\">" + sale.getPrice() + "</font>");
//			content2 = str;
//		}
		Integer[] drawableResid = new Integer[]{
				R.drawable.alert_dialog_sure_selector,
				null,
				R.drawable.alert_dialog_cancel_selector};
		View.OnClickListener[] clickListeners = new View.OnClickListener[]{new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				exit();
			}
		},null,null};
		
		String[] content = null;
//		if(content2 != null){
//			content = new String[]{content1, content2};
//		}else{
//			content = new String[]{content1};
//		}
		Alert.show(context, title, content, clickListeners, drawableResid);
	}

	
	public static void exit(){
		StartupActivity.context.finish();
		MainTabActivity.context.finish();
		//退出系统
		//android.os.Process.killProcess(android.os.Process.myPid());
		//System.exit(0);
	}


	public static void retry(Activity context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("获取服务器时间失败,请重新登录");
		builder.setCancelable(false);
		builder.setPositiveButton("重试", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			}
		});
		builder.setNegativeButton("退出", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MmbSystem.exit();
			}
		});
		builder.show();
	}
}
