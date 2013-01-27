package org.coffee.provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class AppProviderActivity extends Activity {
	private final String URI_PRE = "content://" + AppContentProvider.AUTH_ORITIES;
	
	private final String URI_INSERT = URI_PRE + "/insert/user";
	private final String URI_UPDATE = URI_PRE + "/update/user";
	private final String URI_QUERY = URI_PRE + "/query/user";
	private final String URI_DELETE = URI_PRE + "/delete/user";
	
	private ContentResolver contentResolver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.contentResolver = getContentResolver();
		
		//注册监听【insert】
		contentResolver.registerContentObserver(Uri.parse(URI_INSERT), 
				true, new AppContentObserver(new Handler()));
		
		ContentValues cv = new ContentValues();
		cv.put("username", "coffee");
		cv.put("password", "coffee");
		
		contentResolver.insert(Uri.parse(URI_INSERT), cv);
		
		//查询数据库
		Cursor cur = contentResolver.query(Uri.parse(URI_QUERY), null, null, null, null);
		while(cur.moveToNext()){
			for(int i=0; i<cur.getColumnCount(); i++){
				System.out.print(cur.getString(i) + "++");
			}
			System.out.println();
		}
		cur.close();
		
		new AppCursorWrapper(cur).requery();
		
		System.out.println();
	}
}
