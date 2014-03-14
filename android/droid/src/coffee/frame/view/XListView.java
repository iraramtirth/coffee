package coffee.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 
 * @author coffee
 * 
 *         2014年3月13日上午11:31:37
 */
public class XListView extends BasePullRefreshView<ListView> {

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = new ListView(context, attrs);

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

}
