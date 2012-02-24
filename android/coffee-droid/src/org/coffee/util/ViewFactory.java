package org.coffee.util;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ViewFactory {

	public LinearLayout getLinearLayout(Activity context){
		LinearLayout lnLayout = new LinearLayout(context);
		lnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lnLayout.setOrientation(LinearLayout.VERTICAL);
		return lnLayout;
	}

}
