package com.coffee.adapter;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.coffee.ContactsUtils;
import com.coffee.ContactsUtils.Contacts;
import com.coffee.R;

public class ContactAdapter extends BaseAdapter {

	//联系人hash 
	public static Map<String,ContactsUtils.Contacts> contactsMap = new HashMap<String, ContactsUtils.Contacts>();
	private List<ContactsUtils.Contacts> contactsList = new ArrayList<ContactsUtils.Contacts>();
	
	//电话黑名单<电话号码,姓名>
	public final static Map<String,String> phoneFilterMap = new HashMap<String, String>();
	//短信黑名单
	public final static Map<String,String> messageFilterMap = new HashMap<String, String>();
	
	public List<ViewHolder> holderList = new ArrayList<ViewHolder>();
	
	private LayoutInflater mInflater;
	public ContactAdapter(Activity context) {
		this.mInflater = context.getLayoutInflater();
		//处理contactsList
		for(Contacts contacts : ContactsUtils.getContacks(context)){
			String phonesNumber = contacts.getPhones();
			//tmpList不包括手机号为""的
			if(phonesNumber != null && phonesNumber.trim().length() > 0){
				String[] phones = phonesNumber.split(",");
				if(phones.length > 1){
					for(int i=0; i<phones.length; i++){
						contacts.setPhones(phones[i]);
						contactsMap.put(phones[i], contacts);
					}
				}else{
					contactsMap.put(phonesNumber, contacts);
				}
			}
		}
		//按照ContactsUtils.Contacts.name：联系人姓名中文
		contactsMap = this.sortByContactName(contactsMap);
		//排序
		contactsList.clear();
		contactsList.addAll(contactsMap.values());
	}

	
	
	@Override
	public int getCount() {
		return contactsMap.size();
	}

	@Override
	public Object getItem(int position) {
		return holderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			// 商品列表布局
			convertView = mInflater.inflate(R.layout.list_item_contact, parent, false);
			holder = new ViewHolder();
			holder.chx = (CheckBox) convertView.findViewById(R.id.contact_chx);
			holder.name = (TextView) convertView.findViewById(R.id.contact_name);
			holder.phones = (TextView) convertView.findViewById(R.id.contact_phones);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.chx.setChecked(false);//初始化 
		holderList.add(holder);
		Contacts item = contactsList.get(position);
		 
		holder.name.setText(item.getName());
		holder.phones.setText(item.getPhones());
		
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		//contactsList.clear();
		//contactsList.addAll(contactsMap.values());
	}
	
	public class ViewHolder {
		public CheckBox chx;
		public TextView name; // 姓名
		//public ImageView photo; // 头像
		public TextView phones; // 电话号，多个号由 ','分割
	}
	
	
	private Map<String,ContactsUtils.Contacts> sortByContactName(final Map<String,ContactsUtils.Contacts> map){
		//
		List<String> keyList = new LinkedList<String>(contactsMap.keySet());
		Collections.sort(keyList, new java.util.Comparator<String>() {
			 Collator collator = Collator.getInstance();
			@Override
			public int compare(String key1, String key2) {
				  CollationKey val1 = collator.getCollationKey(map.get(key1).getName().toLowerCase());
				  CollationKey val2 = collator.getCollationKey(map.get(key2).getName().toLowerCase());
				  return val1.compareTo(val2);
			}
		});
		Map<String,ContactsUtils.Contacts> newMap = new LinkedHashMap<String, ContactsUtils.Contacts>();
		for(String key : keyList){
			newMap.put(key, map.get(key));
		}
		return newMap;
	}
}
