package org.coffee.activity;

import org.coffee.R;
import org.coffee.view.RoundCornerImageView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HandleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout lnLayout = new LinearLayout(this);
		lnLayout.setBackgroundColor(Color.WHITE);
		lnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lnLayout.setOrientation(LinearLayout.VERTICAL);
	
		final ImageView iv = new RoundCornerImageView(this);
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				iv.setImageResource(R.drawable.woyou);
//			}
//		}).start();
		
		iv.setImageResource(R.drawable.woyou);
		 lnLayout.addView(iv);
		iv.getLayoutParams().width = 100;
		iv.getLayoutParams().height = 100;
		
//		BitmapDrawable bd = new BitmapDrawable();
//         bd.setAntiAlias(true);
//		iv.setImageDrawable(bd);
         
        
		this.setContentView(lnLayout);
	}
	
}
