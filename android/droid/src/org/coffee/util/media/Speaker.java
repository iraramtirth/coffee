package org.coffee.util.media;

import android.content.Context;
import android.media.AudioManager;
import coffee.frame.App;

/**
 * 扬声器
 * 
 * @author coffee
 */
public class Speaker {
	final String TAG = "Speaker";

	private static Speaker instance = null;
	private AudioManager mAudioManager;

	private Speaker() {
		mAudioManager = (AudioManager) App.getContext().getSystemService(Context.AUDIO_SERVICE);
	}

	public static Speaker getInstance() {
		synchronized (Speaker.class) {
			if (instance == null) {
				instance = new Speaker();
			}
		}
		return instance;
	}

	/**
	 * 打开扬声器
	 */
	public void open() {
		if (null == mAudioManager) {
			return;
		}
		if (!mAudioManager.isSpeakerphoneOn()) {
			// if (OsBuild.isSpeakerDefault()) {
			// mAudioManager.setMode(AudioManager.MODE_IN_CALL);
			// }
			mAudioManager.setSpeakerphoneOn(true);
		}
	}

	/**
	 * 
	 * 扬声器是否打开
	 * 
	 * @return 扬声器是否打开
	 */
	public boolean isOpen() {
		if (null == mAudioManager) {
			return false;
		}
		return mAudioManager.isSpeakerphoneOn();
	}

	/**
	 * 关闭扬声器
	 */
	public void close() {
		if (null == mAudioManager) {
			return;
		}
		if (mAudioManager.isSpeakerphoneOn()) {
			// if (OsBuild.isSpeakerDefault()) {
			// mAudioManager.setMode(AudioManager.MODE_IN_CALL);
			// }
			mAudioManager.setSpeakerphoneOn(false);
		}
	}

}
