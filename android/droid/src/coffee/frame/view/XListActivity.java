package coffee.frame.view;

import org.coffee.R;

import coffee.frame.pull2refresh.XListView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class XListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_listview);
		XListView xlist = (XListView) findViewById(R.id.pull_refresh_list);

		xlist.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tv = new TextView(XListActivity.this);
				tv.setText(position + "");
				return tv;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return 30;
			}
		});
	}
}
