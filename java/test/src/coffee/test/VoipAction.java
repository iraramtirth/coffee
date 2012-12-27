/*
 * 文件名: VoipType.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights Reserved.
 * 描 述: [该类的简要描述] 创建人: Maoah 创建时间:2012-6-9 修改人： 修改时间: 修改内容：[修改内容]
 */
package coffee.test;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author zhouxin
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-6-9]
 */
public class VoipAction
{

    /**
     */
    public static enum Close
    {
        /**
         * 通话结束
         */
       VOIP_CALL_CLOSE_IN_SUCCESS(0),

        /**
         * 来电结束-未接
         */
        VOIP_CALL_CLOSE_IN_MISSED(1),
        /**
         * 来电结束-拒绝
         */
        VOIP_CALL_CLOSE_IN_REFUSE(2),

        /**
         * 呼叫结束 -- 正常
         */
        VOIP_CALL_CLOSE_OUT_SUCCESS(3),
        /**
         * 呼叫结束 -- 对方未接
         */
        VOIP_CALL_CLOSE_OUT_MISS(4),
        /**
         * 呼叫结束 -- 对方拒绝
         */
        VOIP_CALL_CLOSE_OUT_REFUSE(5),
       /**
        * 呼叫结束 -- 取消
        */
       VOIP_CALL_CLOSE_OUT_CANCEL(6);
       
        private int codeValue;

        private Close(int codeValue)
        {
            this.codeValue = codeValue;
        }

        public int getCodeValue()
        {
            return this.codeValue;
        }
    }
    
    public static void main(String[] args) {
    	int val = Close.VOIP_CALL_CLOSE_IN_MISSED.getCodeValue();
    	
    	System.out.println(val);
    	
    	int num = 104;
    	
    	Close[] c = Close.values();
    	
    	System.out.println(c);
    	
    	Close cl = Close.valueOf(c[val].toString());
    	
    	System.out.println(cl);
    	
    	
    	boolean isSpeakerOpen = false;
    	isSpeakerOpen = isSpeakerOpen | isSpeakerOpen;
    	System.out.println(isSpeakerOpen);
    	isSpeakerOpen = isSpeakerOpen | isSpeakerOpen;
    	System.out.println(isSpeakerOpen);
	}
}
