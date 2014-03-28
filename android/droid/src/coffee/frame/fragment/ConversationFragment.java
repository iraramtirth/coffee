package coffee.frame.fragment;

import org.coffee.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import coffee.frame.fragment.base.BaseDroidFragment;

/**
 * 
 * 
 * @author coffee<br>
 *         2013上午11:57:05
 */
public class ConversationFragment extends BaseDroidFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.x_listview, container, false);
		return layout;
	}

}
