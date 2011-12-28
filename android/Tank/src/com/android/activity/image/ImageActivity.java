package com.android.activity.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.game.activity.R;

public class ImageActivity extends Activity {
   
	private LinearLayout linearLayout;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	linearLayout = new LinearLayout(this);
    	
        super.onCreate(savedInstanceState);
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.icon);
        iv.setAdjustViewBounds(true);
        iv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout.addView(iv);
      
        
        Bitmap srcIamge =   BitmapFactory.decodeResource(this.getResources(), R.drawable.icon);
        // 图像剪切 
        Bitmap image2 = Bitmap.createBitmap(srcIamge,0,0,16,16);
        ImageView iv2 = new ImageView(this);
        iv2.setImageBitmap(image2);
        iv2.setAdjustViewBounds(true);
        iv2.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout.addView(iv2);
        
        // 图像旋转
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        Bitmap image3 = Bitmap.createBitmap(srcIamge, 0, 0, srcIamge.getWidth(), srcIamge.getHeight(), matrix, true);
        ImageView iv3 = new ImageView(this);
        iv3.setImageBitmap(image3);
        iv3.setAdjustViewBounds(true);
        iv3.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout.addView(iv3);
        setContentView(linearLayout);
    }
}