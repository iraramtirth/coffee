package coffee.util.view;

import android.app.Activity;
import android.app.ProgressDialog;


public class ProgressUtils {
	private static ProgressDialog pd;
	public static void showDialog(Activity context){
		pd = new ProgressDialog(context);
		//pd.setProgressStyle(ProgressDialog.);
		pd.setIcon(android.R.drawable.dialog_frame);
		pd.setMessage("加载中");
		pd.setCancelable(true);
		pd.show();
	}
	
//	public static void hiddenDialog(){
//		pd.cancel();
//	}
}
