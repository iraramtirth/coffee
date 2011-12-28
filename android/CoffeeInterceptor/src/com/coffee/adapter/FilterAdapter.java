package com.coffee.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.coffee.R;

/**
 * 黑名单
 * @author wangtao
 */
public class FilterAdapter extends BaseAdapter {

	//黑名单Map<phoneNumber, name>
	private Map<String,String> filterMap = new HashMap<String, String>();
	//
	private List<String> phoneNumberList = new ArrayList<String>();
	
	public List<ViewHolder> holderList = new ArrayList<ViewHolder>();
	
	
	private LayoutInflater mInflater;
	private FilterType filterType;
	public enum FilterType{
		Message,
		Phone
	}
	public FilterAdapter(FilterType filterType,Activity context){
		if(filterType == FilterType.Message){
			this.filterMap = ContactAdapter.messageFilterMap;
		}else{
			this.filterMap = ContactAdapter.phoneFilterMap;
		}
		this.mInflater = context.getLayoutInflater();
		this.filterType = filterType;
		phoneNumberList.addAll(filterMap.keySet());
	}
	
	@Override
	public int getCount() {
		return filterMap.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	//ListView并非一下子全部加在完成该方法，只是加在当前页面的list记录，当翻屏的时候该方法将会再次执行
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
		
		String phoneNumber = phoneNumberList.get(position);
		holder.name.setText(filterMap.get(phoneNumber));
		//holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon));
		holder.phones.setText(phoneNumber);
		
		return convertView;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		this.phoneNumberList.clear();
		if(filterType == FilterType.Message){
			this.filterMap = ContactAdapter.messageFilterMap;
		}else{
			this.filterMap = ContactAdapter.phoneFilterMap;
		}
		this.phoneNumberList.addAll(this.filterMap.keySet());
	}

	public class ViewHolder {
		public CheckBox chx;
		public TextView name; // 姓名
		public ImageView photo; // 头像
		public TextView phones; // 电话号，多个号由 ','分割
	}

}
