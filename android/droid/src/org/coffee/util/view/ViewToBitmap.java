package org.coffee.util.view;

import org.coffee.http.HttpClient;
import org.coffee.util.view.BitmapUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author coffee
 */
public class ViewToBitmap extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.onCreate(savedInstanceState);
		LinearLayout lnLayout = new LinearLayout(this);
		lnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lnLayout.setOrientation(LinearLayout.VERTICAL);
	
		TextView tv = new TextView(this);
		tv.setText("fdjfdhdkjf");
		
		lnLayout.addView(tv);
	
		HttpClient.setNetworkType(this);
		
		lnLayout.setDrawingCacheEnabled(true);  
		lnLayout.measure(  
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),  
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
		lnLayout.layout(0, 0, lnLayout.getMeasuredWidth(),  
				lnLayout.getMeasuredHeight());  
  
		lnLayout.buildDrawingCache();  
          
        Bitmap cacheBitmap= lnLayout.getDrawingCache();  
        
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        BitmapUtils.cacheBitmapToFile(bitmap, "aa.jpg");
        
        
    	this.setContentView(lnLayout);
	}
}
