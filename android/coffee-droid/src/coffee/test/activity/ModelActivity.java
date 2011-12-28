package coffee.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class ModelActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout lnLayout = new LinearLayout(this);
		lnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lnLayout.setOrientation(LinearLayout.VERTICAL);
	
//		lnLayout.addView(child)
		this.setContentView(lnLayout);
	}
}
