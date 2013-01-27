/*
 * 文件名: VibratorManager.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: wangtao 创建时间:2012-7-17 修改人： 修改时间: 修改内容：[修改内容]
 */
package org.coffee.util.media;

import org.coffee.App;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

/**
 * [项目中用到的震动 单例]<BR>
 * 
 * @author wangtao
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-7-17]
 */
public class VibratorManager
{

    private static Vibrator mVibrator;

    /**
     * 震动时间
     * 
     * @author wangtao
     */
    public static enum ViberatorPeroid
    {
        P_500(500), // 500毫秒
        P_1000(1000);
        int codeValue;

        private ViberatorPeroid(int codeValue)
        {
            this.codeValue = codeValue;
        }

        public int getCodeValue()
        {
            return this.codeValue;
        }
    }

    private VibratorManager()
    {
    }

    private static Vibrator getVibrator()
    {
        if (mVibrator == null)
        {
            mVibrator = (Vibrator) App
                .getContext()
                    .getSystemService(Context.VIBRATOR_SERVICE);
        }
        return mVibrator;
    }

    /**
     * 震动(一次)
     * 
     * @param vp : 震动时长
     */
    public synchronized static void vibrate(ViberatorPeroid... vp)
    {
        if (vp.length == 0)
        {
            // 默认震动500毫秒
            getVibrator().vibrate(ViberatorPeroid.P_500.getCodeValue());
        }
        else
        {
            getVibrator().vibrate(vp[0].getCodeValue());
        }
    }

    public static void vibrate(long[] pattern, int repeat)
    {
        getVibrator().vibrate(pattern,
            repeat);
    }

    /**
     * 取消震动
     * 为了防止 vibrate(ViberatorPeroid... vp)方法 还未执行完， 就执行去掉， 所以先延迟一下
     */
    public static void cancelVibratorDelayed(ViberatorPeroid vp)
    {
        if(vp == null)
        {
            vp = ViberatorPeroid.P_1000;
        }
        
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                getVibrator().cancel();
            }
        }, vp.getCodeValue());
    }

    /**
     *  取消震动
     */
    public static void cancelVibrator()
    {
        getVibrator().cancel();
    }
}
