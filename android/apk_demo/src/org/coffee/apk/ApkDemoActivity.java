package org.coffee.apk;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import orf.coffee.apk.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class ApkDemoActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
	
		
		List<Integer> resList = new ArrayList<Integer>();

		final ViewFlipper flipper  = (ViewFlipper)this.findViewById(R.id.flipperView);
		try {
			Class<?> drawCLass = Class.forName(this.getPackageName() + ".R$drawable");
			Field[] fields = drawCLass.getDeclaredFields();
			for(Field field : fields){
				if(field.getName().equals("icon")){
					continue;
				}
				resList.add(field.getInt(null));
				ImageView image = new ImageView(this);
				image.setImageResource(field.getInt(null));
				flipper.addView(image);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//flipper.setAutoStart(true);
		flipper.setFlipInterval(1000 * 3);
		
		//flipper.setBackgroundResource(resList.get(index));
		
		flipper.setOnTouchListener(new OnGestureEvent(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//left
				if(e1.getX() - e2.getX() >50 && Math.abs(velocityX) > 20){
					flipper.showPrevious();
				}
				//right
				if(e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 20){
					flipper.showNext();
				}
				return true;
			}
        	
        });
    }
}