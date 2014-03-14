package coffee.frame.view;

import org.coffee.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

public class LoadingLayout extends FrameLayout{

	public LoadingLayout(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
	}
	
}
