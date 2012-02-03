package coffee.seven.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import org.droid.util.sqlite.DbHelper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import coffee.seven.bean.SaleBean;

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
		TextView view = new TextView(context);
		view.setText("hello world");
		return view;
	}

}
