package org.coffee.util.media;

import org.coffee.App;
import org.coffee.R;
import org.coffee.util.log.Log;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * 铃声播放器
 * 
 * @author coffee
 */
public class RingtonePlayer
{
    /**
     * TAG
     */
    private final String TAG = "RingtonePlayer";

    /**
     * 媒体播放对象
     */
    private MediaPlayer mMediaPlayer = null;

    /**
     * 音频管理器
     */
    private AudioManager mAudioManager = null;

    /**
     * 播放线程
     */
    private Thread mThread = null;

    /**
     * context对象
     */
    private Context mContext;

    private RingType mRingType;

    /**
     * 是否扬声器模式
     */
    private boolean mIsSpeakerOn = true;

    private static RingtonePlayer instance;

    /**
     * 构造方法
     * 
     * @param context context对象
     */
    private RingtonePlayer()
    {
        this.mContext = App.getContext();
        this.mAudioManager = (AudioManager) mContext
            .getSystemService(Context.AUDIO_SERVICE);
    }

    public static RingtonePlayer getInstance()
    {
        if (instance == null)
        {
            instance = new RingtonePlayer();
        }
        return instance;
    }

    public enum RingType
    {
        RING_CALL_IN, // 来电
        RING_CALL_OUT, // 去电(回铃)
        RING_CALL_BUSY;// 忙
    }

    /**
     * 开始播放铃声
     * 
     * @mRingType
     * @param isSpeakerOn : 听筒或者扬声器模式 true : 扬声器 false : 听筒
     */
    public void start(RingType mRingType, boolean isSpeakerOn)
    {
        this.mRingType = mRingType;

        this.mIsSpeakerOn = isSpeakerOn;

        if (mRingType == null)
        {
            this.mRingType = RingType.RING_CALL_IN;
        }

        if (mThread == null || !mThread.isAlive())
        {
            mThread = new InnerThread();
            mThread.start();
        }
    }

    /**
     * 开始停止铃声
     */
    public void stop()
    {
        try
        {
            if (null != mMediaPlayer && mMediaPlayer.isPlaying())
            {
                mMediaPlayer.stop();
                Log.info(TAG,
                    "VOIP_MediaPlayer_stop");
            }
            if (mAudioManager != null)
            {
                // 重置回正常模式, 不然播放其他音频也成听筒模式了
                mAudioManager.setMode(AudioManager.MODE_NORMAL);
            }
        }
        catch (Exception e)
        {
            Log.error(TAG,
                "VOIP_RING铃声关闭 " + e);
        }
        finally
        {
            if (mMediaPlayer != null)
            {
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

            //关闭振动
            VibratorManager.cancelVibratorDelayed(null);
        }
    }

    private void createMediaPlayer()
    {
        try
        {
            AssetFileDescriptor afd = null;
            mMediaPlayer = new MediaPlayer();
            switch (this.mRingType)
            {
                case RING_CALL_OUT:
                    afd = this.mContext
                        .getResources()
                            .openRawResourceFd(R.raw.voip_call_ring);
                case RING_CALL_BUSY:
                    if (null == afd)
                    {
                        afd = this.mContext
                            .getResources()
                                .openRawResourceFd(R.raw.voip_busy);
                    }
                    // 摩托Me525 需要通过该方式设置听筒播放
//                    if (false)
                    {
                        mMediaPlayer
                            .setAudioStreamType(AudioManager.FLAG_PLAY_SOUND);
                    }
                    mMediaPlayer.setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(),
                        afd.getLength());
                    afd.close();
                    break;
                case RING_CALL_IN:
                default:
                    Uri alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                    mMediaPlayer.setDataSource(mContext,
                        alert);
            }
            mMediaPlayer.prepare();
        }
        catch (Exception e)
        {
            Log.error(TAG,
                e + "");
        }

    }

    /**
     * 播放器内部播放线程
     * 
     * @author 刘鲁宁
     * @version [RCS Client V100R001C03, Mar 26, 2012]
     */
    private class InnerThread extends Thread
    {
        /**
         * 线程运行方法
         * 
         * @see java.lang.Thread#run()
         */
        public void run()
        {
            try
            {
                int currentVolume = mAudioManager
                    .getStreamVolume(AudioManager.STREAM_RING);
                if (null == mMediaPlayer || !mMediaPlayer.isPlaying())
                {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_RING,
                        currentVolume,
                        AudioManager.FLAG_ALLOW_RINGER_MODES);

                    createMediaPlayer();
                    // 设置听筒播放
                    if (!mIsSpeakerOn)
                    {
                     // 如果不是单通机型的手机
//                        if (!OsBuild.isSingleVoice())
//                        {
//                            mAudioManager.setMode(AudioManager.MODE_IN_CALL);
//                        }
                       setMediaSpeakerOff();
                    }
                    else
                    {
                        mAudioManager.setMode(AudioManager.MODE_NORMAL);
                        setMediaSpeakerOn();
                    }

                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                    Log.info(TAG,
                        "VOIP_MediaPlayer_start");
                }

                // 只有来电(被叫才有震动模式)
                if (mRingType == RingType.RING_CALL_IN)
                {
                    int ringerMode = mAudioManager.getRingerMode();
                    int vibrateSetting = mAudioManager
                        .getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);

                    if (ringerMode == AudioManager.RINGER_MODE_NORMAL)
                    {
                        if (vibrateSetting == AudioManager.VIBRATE_SETTING_OFF || 
                            vibrateSetting == AudioManager.VIBRATE_SETTING_ONLY_SILENT)
                        {
                            // 震动关闭,nothing to do
                        }
                        if (vibrateSetting == AudioManager.VIBRATE_SETTING_ON)
                        {
                            startVibrator();
                        }
                    }
                    else if (ringerMode == AudioManager.RINGER_MODE_VIBRATE)
                    {
                        startVibrator();
                    }
                    else if (ringerMode == AudioManager.RINGER_MODE_SILENT)
                    {
                        // 震动关闭,nothing to do
                    }
                }
            }
            catch (Exception e)
            {
                Log.error(TAG,
                    "MediaPlayer run error.",
                    e);
            }
        }
    }

    /**
     * 开启震动
     */
    private void startVibrator()
    {
        long[] pattern = {500, 100, 500, 100 };
        VibratorManager.vibrate(pattern,
            1);
    }

    /**
     * 
     * 设置铃声外放
     * 
     * @return 是否在播放
     */
    public void setMediaSpeakerOn()
    {
        if (null != mAudioManager)
        {
            mAudioManager.setSpeakerphoneOn(true);
        }
    }

    /**
     * 关闭外放
     * 
     * @return
     */
    public void setMediaSpeakerOff()
    {
        if (null != mAudioManager)
        {
            mAudioManager.setSpeakerphoneOn(false);
        }
    }
}
