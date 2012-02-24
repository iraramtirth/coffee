package org.coffee.widget;

import org.coffee.widget.service.WidgetService;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import org.coffee.R;

public class AppWidget extends AppWidgetProvider {
	private static RemoteViews remoteViews;
	private static ComponentName thisWidget;

	public static String ACTION_WIDGET_PRE = "ACTION_WIDGET_PRE";
	public static String ACTION_WIDGET_NEXT = "ACTION_WIDGET_NEXT";
	
	private static PendingIntent pendingIntentNext;
	private static PendingIntent pendingIntentPre;
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		//??为啥会null
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_contact);
		thisWidget = new ComponentName(context, AppWidget.class);
		//初始化第一个元素
		WidgetService.Info info = new WidgetService.Info(null, "first" ,1111111);
		remoteViews.setTextViewText(R.id.widget_contact_name, info.getName());
		remoteViews.setTextViewText(R.id.widget_contact_phone, info.getPhone()+"");  
		//next
//		Intent service = new Intent();
//		service.setClass(context, WidgetService.class);
//		context.startService(service);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_contact);
		thisWidget = new ComponentName(context, AppWidget.class);
		Intent intentNext = new Intent(context ,WidgetService. class );
		intentNext.setAction(ACTION_WIDGET_NEXT);
		pendingIntentNext = PendingIntent.getService(context, 0, intentNext, 0);
		//pre
		Intent intentPre = new Intent(context ,WidgetService. class );
		intentPre.setAction(ACTION_WIDGET_PRE);
		pendingIntentPre = PendingIntent.getService(context, 0, intentPre, 0);
		//上一条记录
		remoteViews.setOnClickPendingIntent(R.id.widget_contact_btn_next, pendingIntentPre);
		//下一条记录
		remoteViews.setOnClickPendingIntent(R.id.widget_contact_btn_pre, pendingIntentNext);
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		
//		Intent service = new Intent();
//		service.setClass(context, WidgetService.class);
//		context.startService(service);
	}
}