package org.coffee.provider;

import org.coffee.util.sqlite.DbHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * AppContentProvider 为单例模式
 * @author coffee
 */
public class AppContentProvider extends ContentProvider {
	private final String TAG = AppContentProvider.class.getCanonicalName(); 

	private DbHelper dbHelper;
	
	public static final String AUTH_ORITIES = "coffee.provider.AppContentProvider";
	
	private UriMatcher uriMatcher;
	 
	@Override
	public boolean onCreate() {
		Log.i(TAG, "onCreate ContentProvider");
		dbHelper = new DbHelper(this.getContext());
		
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTH_ORITIES, "insert/*", 1);//插入 # 匹配表名
		uriMatcher.addURI(AUTH_ORITIES, "delete/*", 2);//删除 # 匹配表名
		uriMatcher.addURI(AUTH_ORITIES, "update/*", 3);//更新 .......
		uriMatcher.addURI(AUTH_ORITIES, "query/*", 4); //查询  ......
		
		int result = uriMatcher.match(Uri.parse("content://"+AUTH_ORITIES+"/insert/1"));
		System.out.println(result);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.i(TAG, "query");
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		System.out.println(database + "");
		Cursor cursor = database.query("user", null, null, null, null, null, null);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
	
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		System.out.println(uri);
		String tableName = uri.getLastPathSegment();
		System.out.println(tableName);
		long id = dbHelper.getWritableDatabase().insert(tableName, null, values);
		if(id != -1){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
	
		return 0;
	}

}
