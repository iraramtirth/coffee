package com.android.game;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Alert {
	public enum AlertType{
		Toast,
		AlertDialog
	}
	public static void show(Context context,String msg, AlertType type){
		switch(type){
		case Toast:
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			break;
		case AlertDialog:
			AlertDialog.Builder builder = new AlertDialog.Builder(context); 
			builder.setMessage(msg);
			builder.show();
			break;
		}
	}
}
