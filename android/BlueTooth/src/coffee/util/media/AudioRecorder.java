package coffee.util.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import coffee.common.util.FileUtils;
import coffee.frame.Config;
import coffee.utils.log.Log;

/**
 * 录音
 * 
 * @author coffee <br>
 *         2014年4月3日上午9:22:56
 */
public class AudioRecorder {

	private String TAG = "AudioRecorder";

	/**
	 * 当前是否正在录音
	 */
	private boolean isRecording = false;
	/**
	 * 最大振幅
	 */
	private int maxAmplitude;

	/**
	 * pcm临时文件，用于生成amr文件
	 * */
	private final String AUDIO_TEMPORARY_PATH = Config.getCacheDir() + "/temporary.pcm";

	private RecordTask mRecordTask;
	/**
	 * 是否用于实时传输
	 */
	private boolean isRealTime;

	private Callback callback;

	public AudioRecorder() {
	}

	public abstract class Callback {
		/**
		 * 返回生成的Amr文件的路径
		 * 
		 * @param armPath
		 */
		public void execute(String armPath) {

		}

		/**
		 * 返回实时录制的数据
		 * 
		 * @param data
		 */
		public void execute(byte[] data) {

		}
	}

	/**
	 * 开启录音
	 * 
	 * @param isRealTime
	 *            是否用于实时传输。实时传输本地不保存。
	 */
	public void startRecord(boolean isRealTime, Callback callback) {
		mRecordTask = new RecordTask();
		mRecordTask.execute();
		isRecording = true;
		this.isRealTime = isRealTime;
		this.callback = callback;
	}

	/**
	 * 停止录音
	 */
	public void stop() {
		isRecording = false;
	}

	/**
	 * 获取录音路径 这个是最终生成的，不是临时的
	 * 
	 * @return
	 */
	private String generateAudioFile() {
		File file = new File(Config.getCacheDir(), System.currentTimeMillis() + ".amr");
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		return file.getPath();
	}

	/**
	 * 获取最大振幅
	 * 
	 * @return
	 */
	public int getMaxAmplitude() {
		// 10w - 15w
		if (this.maxAmplitude > 10000 * 10 && this.maxAmplitude < 10000 * 15) {
			this.maxAmplitude = 10000;
		}
		if (this.maxAmplitude < 10000) {
			this.maxAmplitude = this.maxAmplitude * 8;
		}
		return this.maxAmplitude;
	}

	private class RecordTask extends AsyncTask<Void, Integer, Void> {
		// 录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
		private int frequence = 8000;

		private AudioRecord audioRecord;
		private int bufferSizeInBytes;

		@Override
		protected void onPreExecute() {
			bufferSizeInBytes = AudioRecord.getMinBufferSize(frequence, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
			audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			isRecording = true;
			FileOutputStream fos = null;
			try {
				if (new File(AUDIO_TEMPORARY_PATH).exists() == false) {
					FileUtils.createNewFileOrDirectory(AUDIO_TEMPORARY_PATH);
				}
				// 开通输出流到指定的文件
				fos = new FileOutputStream(AUDIO_TEMPORARY_PATH);
				// 定义缓冲
				byte[] buffer = new byte[bufferSizeInBytes];
				// 开始录制
				audioRecord.startRecording();
				// 定义循环，根据isRecording的值来判断是否继续录制
				while (isRecording) {
					// 从bufferSize中读取字节，返回读取的short个数
					// 这里老是出现buffer overflow，不知道是什么原因，试了好几个值，都没用，TODO：待解决
					int bufferReadResult = audioRecord.read(buffer, 0, buffer.length);
					fos.write(buffer, 0, buffer.length);
					if (isRealTime && callback != null) {
						callback.execute(buffer);
					}
					for (int i = 0; i < bufferReadResult; i++) {
						maxAmplitude += Math.abs(buffer[i]);
					}
					maxAmplitude /= buffer.length;
				}
				if (isRealTime == false && callback != null) {
					String armFile = generateAudioFile();
					copyToAmr(AUDIO_TEMPORARY_PATH, armFile, bufferSizeInBytes);
					// 回调--arm文件保存路径
					callback.execute(armFile);
				}
				// 录制结束
				audioRecord.stop();
			} catch (Exception e) {
				Log.e(TAG, "", e);
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (audioRecord != null) {
				audioRecord.release();
			}
		}
	}

	private void copyToAmr(String inFilename, String outFilename, int bs) {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(inFilename);
			out = new FileOutputStream(outFilename);
			byte[] header = new byte[6];
			header[0] = '#';
			header[1] = '!';
			header[2] = 'A';
			header[3] = 'M';
			header[4] = 'R';
			header[5] = '\n';
			out.write(header, 0, 6);
			Class<?> AmrInputStream = Class.forName("android.media.AmrInputStream");
			Constructor<?> cons = AmrInputStream.getConstructor(InputStream.class);
			Object mAmrInputStream = cons.newInstance(in);
			Method read = AmrInputStream.getMethod("read", byte[].class);
			Method close = AmrInputStream.getMethod("close");
			byte[] data = new byte[32];
			while (((Integer) read.invoke(mAmrInputStream, data)) != -1) {
				out.write(data);
			}
			close.invoke(mAmrInputStream);
			in.close();
			out.close();
		} catch (Exception e) {
			// 压缩成amr文件过程中出错
			Log.e(TAG, "mark amr file error", e);
		}

	}

}
