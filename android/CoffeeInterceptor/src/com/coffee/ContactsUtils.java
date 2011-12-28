package com.coffee;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

public class ContactsUtils {

	/**
	 * 获取联系人列表 
	 */
	public static List<Contacts> getContacks(Activity context){
		List<Contacts> contactsList = new ArrayList<ContactsUtils.Contacts>();
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		while(cursor.moveToNext()){
			//联系人姓名
			String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			//头像
			String photoId = cursor.getString(cursor.getColumnIndex(PhoneLookup.PHOTO_ID));
			
			//联系人号
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
			String phones = "";
			while(phoneCursor.moveToNext()){
				String phoneNumber = phoneCursor.getString
					(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				phoneNumber = phoneNumber.trim();
				if(phoneNumber.length() > 11){
					if(phoneNumber.startsWith("0")){
						//座机
					}else{
						//去除+86 等
						phoneNumber = phoneNumber.substring(phoneNumber.length() - 11);
					}
				}
				phones += phoneNumber + ",";
			}
			//去除的 ','
			phones = phones.replaceAll(",$", "");
			contactsList.add(new Contacts(name, photoId, phones));
		}
		return contactsList;
	}
	//联系人
	public static class Contacts{
		String name;		//姓名
		String photo;	  	//头像
		String phones;		//电话号，多个号由 ','分割
		
		Contacts(String name, String photo, String phones) {
			this.name = name.trim();
			this.photo = photo;
			this.phones = phones.trim();
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPhoto() {
			return photo;
		}
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		public String getPhones() {
			return phones;
		}
		public void setPhones(String phones) {
			this.phones = phones;
		}
	}

	/**
	 * 删除指定联系人的最近一个的通话记录
	 * @param phoneNumber
	 */
	public static void deleteCallLog(Context context,String phoneNumber){
		ContentResolver resolver = context.getContentResolver();
		//resolver.query(CallLog.Calls.CONTENT_URI, projection, selection, selectionArgs, sortOrder)
		Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[]{"_id"},
				"number=? and (type=1 or type=3)",  new String[]{phoneNumber},  "_id desc limit 1");  
		if(cursor.moveToFirst()) {  
		    int id = cursor.getInt(0);  
		    resolver.delete(CallLog.Calls.CONTENT_URI, "_id=?", new String[] {id + ""});  
		}  
	}
}
