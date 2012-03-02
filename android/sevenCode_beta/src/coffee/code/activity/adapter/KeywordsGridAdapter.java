package coffee.code.activity.adapter;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import coffee.seven.R;
import coffee.seven.bean.KeywordsBean;
import coffee.util.sqlite.DbHelper;

/**
 * 热门搜索
 * @author coffee
 */
public class KeywordsGridAdapter extends BaseAdapter{

	private List<KeywordsBean> keywordsList;
	private Activity context;
	public KeywordsGridAdapter(Activity context){
		keywordsList = new ArrayList<KeywordsBean>();
		this.context = context;
		DbHelper db = new DbHelper();
		keywordsList = db.queryForList(KeywordsBean.class, null, null, null);
		db.close();
	}
	
	@Override
	public int getCount() {
		return keywordsList.size();
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
					.inflate(R.layout.search_keywords_grid_item, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.keywords_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(keywordsList.get(position).getName());
		return convertView;
	}
	
	static class ViewHolder{
		TextView name;			// 活动名称
	}

}
