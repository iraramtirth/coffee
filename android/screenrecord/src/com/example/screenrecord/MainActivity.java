package com.example.screenrecord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	Process process;
	DataOutputStream os;
	DataInputStream is;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				captureScreen();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void captureScreen() {
		FileInputStream graphics = null;
		try {
			Runtime.getRuntime().exec("chmod 777 /dev/graphics/fb0\n");
			Runtime.getRuntime().exec("cp /dev/graphics/fb0 /sdcard/fb0\n");
			graphics = new FileInputStream("/sdcard/fb0");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		byte[] piex = new byte[1280 * 720 * 4];
		StringBuilder data = new StringBuilder();
		try {
			int len = -1;
			while ((len = graphics.read(piex)) != -1) {
				data.append(new String(piex, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		piex = data.toString().getBytes();
		int screenHeight = 1280;
		int screenWidth = 720;
		int[] colors = new int[screenHeight * screenWidth];
		// 将rgb转为色值
		for (int m = 0; m < colors.length; m++) {
			int r = (piex[m * 4] & 0xFF);
			int g = (piex[m * 4 + 1] & 0xFF);
			int b = (piex[m * 4 + 2] & 0xFF);
			int a = (piex[m * 4 + 3] & 0xFF);
			colors[m] = (a << 24) + (b << 16) + (g << 8) + r;
		}

		Bitmap bitmap = Bitmap.createBitmap(colors, screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		ImageView image = (ImageView) findViewById(R.id.image);
		image.setImageBitmap(bitmap);
	}
}
