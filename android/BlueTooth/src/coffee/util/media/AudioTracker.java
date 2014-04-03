package coffee.util.media;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class AudioTracker {
	private AudioTrack audioTrack;

	public AudioTracker() {
		int m_out_buf_size = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, m_out_buf_size, AudioTrack.MODE_STREAM);
	}

	public void start() {
		audioTrack.play();
	}

	public void write(byte[] data) {
		if (audioTrack.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
			start();
		}
		audioTrack.write(data, 0, data.length);
	}

	public void stop() {
		audioTrack.stop();
		audioTrack.release();
	}
}
