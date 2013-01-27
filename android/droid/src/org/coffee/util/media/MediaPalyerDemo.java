package org.coffee.util.media;

import org.coffee.R;
import org.coffee.util.media.RingtonePlayer.RingType;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MediaPalyerDemo extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.mediaplayer_demo);
		
		this.findViewById(R.id.media_play).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RingtonePlayer.getInstance().start(RingType.RING_CALL_OUT, true);
			}
		});
		
		this.findViewById(R.id.media_stop).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RingtonePlayer.getInstance().stop();
			}
		});
	}
	
}
