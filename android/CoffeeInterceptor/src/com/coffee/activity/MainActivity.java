package com.coffee.activity;

import java.util.List;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.coffee.R;
import com.coffee.adapter.ContactAdapter;
import com.coffee.adapter.FilterAdapter;
import com.coffee.adapter.FilterAdapter.FilterType;
/**
 *TabHost的默认布局
 * --------------
 * 
 	<TabHost xmlns:android="http://schemas.android.com/apk/res/android"    
	    android:id="@android:id/tabhost"  
	    android:layout_width="fill_parent"      
	    android:layout_height="fill_parent" >  
		<LinearLayout      
		    android:orientation="vertical"  
		    android:gravity="bottom"  
		    android:layout_width="fill_parent"      
		    android:layout_height="fill_parent" >  
		    <TabWidget      
			    android:id="@android:id/tabs"    
			    android:layout_width="fill_parent"      
			    android:layout_height="wrap_content" />   
			<FrameLayout      
			    android:id="@android:id/tabcontent"  
			    android:layout_width="fill_parent"      
			    android:layout_height="200dip" > 
			</FrameLayout>  
		</LinearLayout>      
	</TabHost>   

 * @author wangtao
 */
public class MainActivity extends TabActivity implements OnClickListener {

	private ContactAdapter contactAdapter;
	private FilterAdapter messageFilterAdapter;
	private FilterAdapter phoneFilterAdapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		TabHost tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.tabs,
				tabHost.getTabContentView(), true);

		//Tab选项卡
//		TabWidget tabWidget = tabHost.getTabWidget();
//		LinearLayout lLayout = (LinearLayout) tabHost.getChildAt(0);
//		lLayout.removeViewAt(0);
//		lLayout.addView(tabWidget);
		//
		
		// 黑名单-短信
		TabHost.TabSpec specFilterMesssage = tabHost
				.newTabSpec("Message Filter");
		specFilterMesssage.setIndicator("Message Filter");
		specFilterMesssage.setContent(R.id.tab_filter_message);
		tabHost.addTab(specFilterMesssage);
		this.initListOfFilterMessage();// 初始化listview

		// 黑名单-电话
		TabHost.TabSpec specFilterPhone = tabHost.newTabSpec("Phone Filter");
		specFilterPhone.setIndicator("Phone Filter");
		specFilterPhone.setContent(R.id.tab_filter_phone);
		tabHost.addTab(specFilterPhone);
		this.initListOfFilterPhone();

		// 设置
		TabHost.TabSpec specSetting = tabHost.newTabSpec("Setting");
		specSetting.setIndicator("Setting");
		specSetting.setContent(R.id.tab_setting);
		tabHost.addTab(specSetting);
		this.initListOfContact();
	}

	// 初始化黑名单(短信) 列表页面
	private void initListOfFilterMessage() {
		ListView filterMessageList = (ListView) this
				.findViewById(R.id.list_filter_message);
		messageFilterAdapter = new FilterAdapter(FilterType.Message, this);
		filterMessageList.setAdapter(messageFilterAdapter);
		CheckBox chx = (CheckBox) this.findViewById(R.id.filter_message_chx);
		final List<FilterAdapter.ViewHolder> holderList = messageFilterAdapter.holderList;
		// 选中联系人列表
		chx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					for (FilterAdapter.ViewHolder holder : holderList) {
						holder.chx.setChecked(true);
					}
				} else {
					for (FilterAdapter.ViewHolder holder : holderList) {
						holder.chx.setChecked(false);
					}
				}
			}
		});
		// 从黑名单(短信)中删除该记录
		Button btnCancel = (Button) this.findViewById(R.id.filter_message_btn);
		btnCancel.setOnClickListener(this);
	}

	// 初始化黑名单(电话) 列表页面
	private void initListOfFilterPhone() {
		ListView filterPhoneList = (ListView) this
				.findViewById(R.id.list_filter_phone);
		phoneFilterAdapter = new FilterAdapter(FilterType.Phone, this);
		filterPhoneList.setAdapter(phoneFilterAdapter);
		final List<FilterAdapter.ViewHolder> holderList = phoneFilterAdapter.holderList;
		CheckBox chx = (CheckBox) this.findViewById(R.id.filter_phone_chx);
		// 选中联系人列表
		chx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					for (FilterAdapter.ViewHolder holder : holderList) {
						holder.chx.setChecked(true);
					}
				} else {
					for (FilterAdapter.ViewHolder holder : holderList) {
						holder.chx.setChecked(false);
					}
				}
			}
		});
		// 从黑名单(电话)中删除该记录
		Button btnCancel = (Button) this.findViewById(R.id.filter_phone_btn);
		btnCancel.setOnClickListener(this);
	}

	// 初始化联系人列表页面
	private void initListOfContact() {
		ListView contactList = (ListView) this.findViewById(R.id.list_contact);
		contactAdapter = new ContactAdapter(this);
		contactList.setAdapter(contactAdapter);
		// 获取联系人列表
		final List<ContactAdapter.ViewHolder> holderList = contactAdapter.holderList;
		CheckBox chx = (CheckBox) this.findViewById(R.id.setting_chx);
		// 选中联系人列表
		chx.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					for (ContactAdapter.ViewHolder holder : holderList) {
						holder.chx.setChecked(true);
					}
				} else {
					for (ContactAdapter.ViewHolder holder : holderList) {
						holder.chx.setChecked(false);
					}
				}
			}
		});
		// 过滤-即拉入黑名单
		Button btnMessage = (Button) this
				.findViewById(R.id.setting_btn_message);
		btnMessage.setOnClickListener(this);
		Button btnPhone = (Button) this.findViewById(R.id.setting_btn_phone);
		btnPhone.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 拉黑(电话=短信)
		if (v.getId() == R.id.setting_btn_message
				|| v.getId() == R.id.setting_btn_phone) {
			final List<ContactAdapter.ViewHolder> holderList = contactAdapter.holderList;
			for (ContactAdapter.ViewHolder holder : holderList) {
				if (holder.chx.isChecked()) {
					String phoneNumber = holder.phones.getText().toString();
					String name = holder.name.getText().toString();
					// 删除
					// ContactAdapter.contactsMap.remove(phoneNumber);
					if (v.getId() == R.id.setting_btn_message) {
						ContactAdapter.messageFilterMap.put(phoneNumber, name);
						this.messageFilterAdapter.notifyDataSetChanged();
					}
					if (v.getId() == R.id.setting_btn_phone) {
						ContactAdapter.phoneFilterMap.put(phoneNumber, name);
						this.phoneFilterAdapter.notifyDataSetChanged();
					}
				}
			}
		}

		// 取消拉黑-短信
		if (v.getId() == R.id.filter_message_btn) {
			final List<FilterAdapter.ViewHolder> holderList = messageFilterAdapter.holderList;
			for (FilterAdapter.ViewHolder holder : holderList) {
				if (holder.chx.isChecked()) {
					String phoneNumber = holder.phones.getText().toString();
					ContactAdapter.messageFilterMap.remove(phoneNumber);
					messageFilterAdapter.notifyDataSetChanged();
				}
			}
		}
		// 取消拉黑-电话
		if (v.getId() == R.id.filter_phone_btn) {
			final List<FilterAdapter.ViewHolder> holderList = phoneFilterAdapter.holderList;
			for (FilterAdapter.ViewHolder holder : holderList) {
				if (holder.chx.isChecked()) {
					String phoneNumber = holder.phones.getText().toString();
					ContactAdapter.phoneFilterMap.remove(phoneNumber);
					phoneFilterAdapter.notifyDataSetChanged();
				}
			}
		}
		Toast.makeText(this, "Setting Success", Toast.LENGTH_SHORT).show();
		// System.out.println(ContactAdapter.contactsMap);
		// System.out.println(ContactAdapter.messageFilterMap);
		// System.out.println(ContactAdapter.phoneFilterMap);
	}

}
