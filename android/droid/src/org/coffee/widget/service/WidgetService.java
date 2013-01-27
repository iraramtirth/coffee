package org.coffee.widget.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.coffee.widget.AppWidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;
import org.coffee.R;

public class WidgetService extends Service {

	private static int location = 0; 
	private List<Info> infos;
	private RemoteViews remoteViews; 
	private ComponentName componentName;
	private AppWidgetManager appManager;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	//
	@Override
	public void onCreate() {
		super.onCreate();
		infos = new ArrayList<WidgetService.Info>();
		infos.add(new Info(null,"抓匪",110));
		infos.add(new Info(null,"救火",119));
		infos.add(new Info(null,"救命啊",120));
		infos.add(new Info(null,"出车祸啦",122));
		remoteViews = new  RemoteViews( this .getPackageName(), R.layout.widget_contact);  
		componentName = new  ComponentName( this , AppWidget.class );  
        appManager = AppWidgetManager.getInstance(this);  
//        this.update(infos.get(location));
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent == null){
			return;
		}
		//上
		if(intent.getAction().equals(AppWidget.ACTION_WIDGET_PRE)){
			location++;
			if(location >= infos.size()){
				location = 0;
			}
			 this.update(infos.get(location));
		}
		//下
		if(intent.getAction().equals(AppWidget.ACTION_WIDGET_NEXT)){
			location --;
			if(location < 0){
				location = infos.size()-1;
			}
			 this.update(infos.get(location));
		}
	}
	
	private void update(Info info){
		if(info.getPhoto() != null){
			remoteViews.setImageViewUri(R.id.widget_contact_photo, Uri.fromFile(new File(info.getPhoto())));
		}
		remoteViews.setTextViewText(R.id.widget_contact_name, info.getName());
		remoteViews.setTextViewText(R.id.widget_contact_phone, info.getPhone()+"");  
		appManager.updateAppWidget(componentName, remoteViews);  
	}
	static public class Info{
		private String photo;
		private int phone;
		private String name;

		public Info(){
		}
		
		public Info(String photo, String name, int phone){
			this.photo = photo;
			this.name = name;
			this.phone = phone;
		}
		
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		public int getPhone() {
			return phone;
		}
		public void setPhone(int phone) {
			this.phone = phone;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
//	@Override
//	public void onStart(Intent intent, int startId) {
//		super.onStart(intent, startId);
//		RemoteViews remoteViews = new  RemoteViews( this .getPackageName(), R.layout.widget_contact);  
//        ComponentName cn = new  ComponentName( this , Widget.class );  
//        AppWidgetManager am = AppWidgetManager.getInstance(this);  
//        
//		//上
//		if(intent.getAction().equals(Widget.ACTION_WIDGET_PRE)){
//			remoteViews.setTextViewText(R.id.widget_contact_name, "pre");  
//			am.updateAppWidget(cn, remoteViews);  
//		}
//		//下
//		if(intent.getAction().equals(Widget.ACTION_WIDGET_NEXT)){
//			remoteViews.setTextViewText(R.id.widget_contact_name, "next"); 
//			am.updateAppWidget(cn, remoteViews); 
//		}
//	}
	
}
