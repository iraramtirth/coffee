/*
 * 文件名: VoipCmdGoing.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: wangtao 创建时间:2012-6-27 修改人： 修改时间: 修改内容：[修改内容]
 */
package coffee.test;

/**
 * VoIP命令执行进展 即：当前进行到哪一步了
 * 
 * @author wangtao
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-6-27]
 */
public class VoipCmdGoing
{

    // private static VoipCmdGoing instance = new VoipCmdGoing();

    // public static VoipCmdGoing getInstance()
    // {
    // synchronized (VoipCmdGoing.class)
    // {
    // if(instance == null)
    // {
    // instance = new VoipCmdGoing();
    // }
    // }
    // return instance;
    // }

    private static Step step = Step.GOING_NONE;

    public static void setCurrentStep(Step step)
    {
        VoipCmdGoing.step = step;
    }

    public static Step getCurrentStep()
    {
        return VoipCmdGoing.step;
    }

    public static boolean isGoingNone()
    {
        return VoipCmdGoing.step == Step.GOING_NONE;
    }

    /**
     * VoIP命令进展
     */
    public enum Step
    {
        GOING_SIP_LOGIN, // SIP服务器登录中
        GOING_CMD_INITIAL, // [主叫] 发送initial命令
        GOING_CMD_REPLY, // [被叫] 发送reply阶段
        GOING_CALL_INVITE, // [主叫] invite
        GOING_CALL_TAKING, // 通话
        GOING_NONE;
        
//        GOING_CMD_INITIAL_SEND, // [主叫] 发送initial命令
//        GOING_CMD_INITIAL_RECV, // [被叫] 接收initial命令
//        GOING_CMD_REPLY_SEND,   // [被叫] 发送reply阶段
//        GOING_CMD_REPLY_RECV,   // [主叫] 接收reply命令
    }

}
