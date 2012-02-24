package org.coffee.provider;

import org.coffee.sqlite.DbHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class AppContentProvider extends ContentProvider {

	private UriMatcher uriMatcher;
	
	@Override
	public boolean onCreate() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("coffee.provider.AppConentProvide", "user", 1);
		int result = uriMatcher.match(Uri.parse("content://coffee.provider.AppConentProvide/user"));
		switch(result){
		case 0:
			System.out.println("xxx");
			break;
		case 1:
			System.out.println("cccc");
			break;
		default:
			System.out.println("vvvv");
			break;
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		System.out.println("xxxxxxxxxxx");
		DbHelper db = new DbHelper();
		Cursor cursor = db.getWritableDatabase().query("sale", null, null, null, null, null, null);
		db.close();
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
	
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
	
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
