package org.coffee.util.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Config;
import coffee.utils.log.Log;

/**
 * 录音机 <BR>
 * 
 * @author wangtao
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-8-9]
 */
public class AudioRecorder
{
    private String TAG = "AudioRecorder";

    private static AudioRecorder instance;

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
    private final String AUDIO_TEMPORARY_PATH = UriUtil
        .getLocalStorageDir(Config.getInstance().getUserAccount(),
            UriUtil.LocalDirType.AUDIO) + "/temporary.pcm";

    private static String mAmrFilePath;

    private RecordTask mRecordTask;

    private AudioRecorder()
    {
    }

    public static AudioRecorder getInstance(AudioRecordButton audioRecordButton)
    {
        if (instance == null)
        {
            instance = new AudioRecorder();
        }
        instance.mAudioRecordButton = audioRecordButton;
        return instance;
    }

    /**
     * 开启录音
     */
    public void startRecord()
    {
        mRecordTask = new RecordTask();
        mRecordTask.execute();

        isRecording = true;
    }

    private AudioRecordButton mAudioRecordButton;

    /**
     * 停止录音
     */
    public void stopRecord()
    {
        isRecording = false;
        mAmrFilePath = null;
    }

    /**
     * 返回最后的文件路径 注意：该变量每次用完都重置
     * 即：mAmrFilePath = null
     * @return
     */
    public static String getAudioFilePath()
    {
        mAmrFilePath = generateAudioFile();
        return mAmrFilePath;
    }

    /**
     * 获取录音路径 这个是最终生成的，不是临时的
     * 
     * @return
     */
    private static String generateAudioFile()
    {
        File file = new File(UriUtil.getLocalStorageDir(Config
            .getInstance()
                .getUserAccount(),
            UriUtil.LocalDirType.AUDIO)
            + System.currentTimeMillis() + ".amr");
        File parentFile = file.getParentFile();
        if (!parentFile.exists())
        {
            parentFile.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 获取最大振幅
     * 
     * @return
     */
    public int getMaxAmplitude()
    {
        if(this.maxAmplitude > 10000 * 10 && this.maxAmplitude < 10000 * 15)//10w -- 15w
        {
            this.maxAmplitude = 10000;
        }
        if (this.maxAmplitude < 10000)
        {
           this.maxAmplitude = this.maxAmplitude * 8;
        }
        return this.maxAmplitude;
    }

    private class RecordTask extends AsyncTask<Void, Integer, Void>
    {

        private int frequence = 8000; // 录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备

        private AudioRecord audioRecord;
        private int bufferSizeInBytes;

        @Override
        protected void onPreExecute()
        {
            bufferSizeInBytes = AudioRecord.getMinBufferSize(frequence,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                frequence,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSizeInBytes);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            isRecording = true;
            try
            {
                // 开通输出流到指定的文件
                FileOutputStream fos = new FileOutputStream(AUDIO_TEMPORARY_PATH);
                // 定义缓冲
                byte[] buffer = new byte[bufferSizeInBytes];

                // 开始录制
                audioRecord.startRecording();

                // 定义循环，根据isRecording的值来判断是否继续录制
                while (isRecording)
                {
                    // 从bufferSize中读取字节，返回读取的short个数
                    // 这里老是出现buffer overflow，不知道是什么原因，试了好几个值，都没用，TODO：待解决
                    int bufferReadResult = audioRecord.read(buffer,
                        0,
                        buffer.length);

                    fos.write(buffer,
                        0,
                        buffer.length);

                    for (int i = 0; i < bufferReadResult; i++)
                    {
                        maxAmplitude += Math.abs(buffer[i]);
                    }
                    maxAmplitude /= buffer.length;
                }

                copyToAmr(AUDIO_TEMPORARY_PATH,
                    getAudioFilePath(),
                    bufferSizeInBytes);
                // 录制结束
                audioRecord.stop();
                fos.close();
            }
            catch (Exception e)
            {
                Log.error(TAG,
                    e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            // 压缩完成以后再通知AudioRecordButton
            mAudioRecordButton.onStoped();
            if(audioRecord != null)
            {
               audioRecord.release();
            }
            mAmrFilePath = null;
        }
    }

    private void copyToAmr(String inFilename, String outFilename, int bs)
    {
        InputStream in = null;
        FileOutputStream out = null;
        try
        {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            byte[] header = new byte[6];
            header[0] = '#';
            header[1] = '!';
            header[2] = 'A';
            header[3] = 'M';
            header[4] = 'R';
            header[5] = '\n';
            out.write(header,
                0,
                6);
            Class<?> AmrInputStream = Class
                .forName("android.media.AmrInputStream");
            Constructor<?> cons = AmrInputStream
                .getConstructor(InputStream.class);
            Object mAmrInputStream = cons.newInstance(in);
            Method read = AmrInputStream.getMethod("read",
                byte[].class);
            Method close = AmrInputStream.getMethod("close");
            byte[] data = new byte[32];
            while (((Integer) read.invoke(mAmrInputStream,
                data)) != -1)
            {
                out.write(data);
            }
            close.invoke(mAmrInputStream);
            in.close();
            out.close();
        }
        catch (Exception e)
        {
            // 压缩成amr文件过程中出错
            Log.error(TAG,
                "mark amr file error",
                e);
        }

    }

}
