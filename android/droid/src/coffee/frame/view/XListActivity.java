package coffee.frame.view;

import org.coffee.R;

import android.app.Activity;
import android.os.Bundle;

public class XListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_listview);
		XListView xlist = (XListView) findViewById(R.id.pull_refresh_list);
	}
}
