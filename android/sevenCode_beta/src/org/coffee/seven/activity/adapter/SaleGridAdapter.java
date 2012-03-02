package org.coffee.seven.activity.adapter;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.bean.SaleBean;
import coffee.util.sqlite.DbHelper;

/**
 * 活动分类
 * @author coffee
 */
public class SaleGridAdapter extends BaseAdapter{

	private List<SaleBean> saleList;
	private Activity context;
	public SaleGridAdapter(Activity context){
		saleList = new ArrayList<SaleBean>();
		this.context = context;
		DbHelper db = new DbHelper();
		saleList = db.queryForList(SaleBean.class, null, null, null);
		db.close();
	}
	
	@Override
	public int getCount() {
		return saleList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = context.getLayoutInflater()
					.inflate(R.layout.search_sale_grid_item, parent, false);
			holder = new ViewHolder();
			holder.saleId = (TextView) convertView.findViewById(R.id.sale_id);
			holder.saleName = (TextView) convertView.findViewById(R.id.sale_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.saleId.setText(saleList.get(position).getId() + "");
		holder.saleName.setText(saleList.get(position).getName());
		return convertView;
	}
	
	static class ViewHolder{
		TextView saleId;			// 活动编号
		TextView saleName;			// 活动名称
	}

}
