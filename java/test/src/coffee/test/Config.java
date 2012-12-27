/*
 * 文件名: SystemConfig.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [系统参数] 创建人: 陈罡 创建时间: 2011-10-20 修改人： 修改时间: 修改内容：[修改内容]
 */
package coffee.test;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.omg.CORBA.Environment;

/**
 * 
 * [初始化]<BR>
 * [功能详细描述]
 * 
 * @author CG
 * @version [ME MTVClient_Handset V100R001C04SPC002, Oct 20, 2011]
 */
public class Config
{
    private final static String TAG = "Config";

    /**
     * Config单例对象
     */
    private static Config instance;
    /**
     * 测试环境 AAS ： 123.125.97.217:5020 Portal:123.125.97.217:18095
     * 
     * OUS:123.125.97.217:8080
     * 
     * 公网环境 AAS： 123.125.97.178:44010/tellin/ Portal：
     * 123.125.97.177:80/Portal/servlet/
     **/
    // 北京公网环境   221.226.48.130:2503   

    private String aas_url = "http://221.226.48.130:2503/tellin/";
    private String portalurl = "http://123.125.97.177:80/Portal/servlet/";

    // 测试环境不用提交(短信小助手)
    // private String aas_url = "http://123.125.97.217:5020/tellin/";
    // private String portalurl = "http://123.125.97.217:18095/Portal/servlet/";

    // 南京n5现网环境
    // private String aas_url = "http://218.2.129.2:6010/tellin/";
    // private String portalurl = "http://218.2.129.2:31907/Portal/servlet/";

    // 北京现网环境 【数据迁移、CAB头像切割】
    // private String aas_url = "http://123.125.97.178:15020/tellin/";
    // private String portalurl = "http://123.125.97.177:80/Portal/servlet/";

     /**
     * 客户端升级地址
     */
     private String appUpdateUrl = "http://123.125.97.219:8080";
     //221.226.48.130:2502
     //218.2.129.42 5060
     private String voipaddr = "218.2.129.42:5060";

     /**
      * voip登录密码
      */
     private String imspwd;
    /**
     * 客户端测试升级地址
     */
//    private String appUpdateUrl = "http://123.125.97.217:8080";

    /**
     * 获取超邮未读邮件的地址
     */
    public String unReadMailService_url = "http://114.247.0.106:8808/ChinaUnicomWoContactsAPI.asmx";

    /**
     * 获取超邮未读邮件action地址
     */
    public String unReadMailSOAPAction_url = "http://api.cevt.org/CheckUserUnreadMailNum";

    /**
     * 进入云邮系统的url地址
     */
    // public String mailServiceUrl
    // ="http://mail.im.wo.com.cn/client/auth/wo/?_token=";
    public String mailServiceUrl = "http://114.247.0.106:8808/client/auth/wo/?_token=%1$s&_page=mail";

    /**
     * 查看邮件详情的url地址
     */
    public String mailDetailServiceUrl = "http://114.247.0.106:8808/client/auth/wo/?_token=%1$s&_page=show&_uid=%2$s";

    /**
     * 用户状态
     */
    private int userStatus;

    /**
     * 用户登录吗
     */
    private String loginCode;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 令牌token
     */
    private String token;

    /**
     * 用户沃友
     */
    private String userAccount;

    /**
     * token过期时间
     */
    private String expiretime;

    /**
     * CAB组url
     */
    private String cabgroupurl;

    /**
     * nd网盘地址
     */
    private String ndurl = "http://218.2.129.2:6190";

    /**
     * cab地址
     */
    private String cabSrvUrl = "http://218.2.129.2:6188/";

    /**
     * cab SSL通道
     */
    private String cabHttpsUrl = "https://218.2.129.2:6200";

    /**
     * ose地址
     */
    private String oseSrvUrl = "http://218.2.129.2:6014";

    /**
     * rmc地址
     */
    private String rmcUrl = "http://218.2.129.2:8999";

    /**
     * rich地址
     */
    private String richLifeAppUrl = "http://123.125.97.219:47010";

    /**
     * sip服务器的地址
     */
    private String sipUrl ;
    
    private String strCabAuthReq;
    /**
     * ose部件鉴权token
     */
    private String strOseAuthReq;

    /**
     * 微博服务器地址
     */
    // private String blogServer = "http://192.168.9.173:8080/Portal";
    // private String blogServer = "http://58.249.55.68:8090/mb";
    private String blogServer = "http://218.2.129.2:31907/Portal/";
    /**
     * 微博服务端IP
     */
    private String blogServerIp = "192.168.9.173";
    // private String blogServerIp = "58.249.55.68";

    /**
     * 微博服务端Port
     */
    private String blogServerPort = "31907";
    // private String blogServerPort = "8090";

    /**
     * 微博服务端应用路径
     */
    private String blogServerApp = "Portal";
    // private String blogServerApp = "mb";


    private String xmppDomain;

    private boolean logSwitch;

    /**
     * 
     * [构造函数]
     */
    private Config()
    {
       
    }

    /**
     * 
     * [获取实例]<BR>
     * [功能详细描述]
     * 
     * @return SystemConfig实例
     */
    public static Config getInstance()
    {
        if (instance == null)
        {
            synchronized (Config.class)
            {
                if (instance == null)
                {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    /**
     * 
     * 获取rmc地址
     * 
     * @return rmc地址
     */
    public String getRmcUrl()
    {
        return rmcUrl;
    }

    public void setRmcUrl(String rmcUrl)
    {
        this.rmcUrl = rmcUrl;
    }

    /**
     * 
     * 获取rich地址
     * 
     * @return rich地址
     */
    public String getRichLifeAppUrl()
    {
        return richLifeAppUrl;
    }

    public void setRichLifeAppUrl(String richLifeAppUrl)
    {
        this.richLifeAppUrl = richLifeAppUrl;
    }

    /**
     * 获取cab地址
     * 
     * @return cab地址
     */
    public String getCabSrvUrl()
    {
        return cabSrvUrl;
    }

    public void setCabSrvUrl(String cabSrvUrl)
    {
        this.cabSrvUrl = cabSrvUrl;
    }

    /**
     * 获取ose地址
     * 
     * @return ose地址
     */
    public String getOseSrvUrl()
    {
        return oseSrvUrl;
    }

    public String getSipUrl()
    {
        return sipUrl;
    }

    public void setSipUrl(String sipUrl)
    {
        this.sipUrl = sipUrl;
    }

    public void setOseSrvUrl(String oseSrvUrl)
    {
        this.oseSrvUrl = oseSrvUrl;
    }

    
    public String getVoipaddr()
    {
        return voipaddr;
    }

    public void setVoipaddr(String voipaddr)
    {
        this.voipaddr = voipaddr;
    }

    public String getImspwd()
    {
        return imspwd;
    }

    public void setImspwd(String imspwd)
    {
        this.imspwd = imspwd;
    }

    /**
     * 
     * 获取cab组地址
     * 
     * @return cab组地址
     */
    public String getCabgroupurl()
    {
        return cabgroupurl;
    }

    public void setCabgroupurl(String cabgroupurl)
    {
        this.cabgroupurl = cabgroupurl;
    }

    /**
     * 
     * 获取nd网盘地址
     * 
     * @return nd网盘地址
     */
    public String getNdurl()
    {
        return ndurl;
    }

    public void setNdurl(String ndurl)
    {
        this.ndurl = ndurl;
    }

    public String getExpiretime()
    {
        return expiretime;
    }

    public void setExpiretime(String expiretime)
    {
        this.expiretime = expiretime;
    }

    public String getLoginCode()
    {
        return loginCode;
    }

    public void setLoginCode(String loginCode)
    {
        this.loginCode = loginCode;
    }

    public boolean hasUserid()
    {
        return userid != null;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     * 沃友ID
     */
    public String getUserAccount()
    {
        return userAccount;
    }

    public void setUserAccount(String user)
    {
        this.userAccount = user;
    }

    public String getDomain()
    {
        return xmppDomain;
    }

    public boolean getLogSwitch()
    {
        return logSwitch;
    }

    /**
     * 
     * [一句话功能简述]<BR>
     * [功能详细描述]
     * 
     * @return 成功标记
     */
    public boolean isSucceed()
    {
        if (loginCode == null)
        {
            return false;
        }
        return loginCode.equals("0");
    }

    public int getUserStatus()
    {
        return userStatus;
    }

    public void setUserStatus(int inUserStatus)
    {
        userStatus = inUserStatus;
    }

    public String getAppUpdateUrl()
    {
        return appUpdateUrl;
    }

    public void setAppUpdateUrl(String appUpdateUrl)
    {
        this.appUpdateUrl = appUpdateUrl;
    }

    public void setCabAuthReq(String inStrCabAuthReq)
    {
        strCabAuthReq = inStrCabAuthReq;
    }

    public String getCabAuthReq()
    {
        return strCabAuthReq;
    }

    public String getStrOseAuthReq()
    {
        return strOseAuthReq;
    }

    public void setStrOseAuthReq(String strOseAuthReq)
    {
        this.strOseAuthReq = strOseAuthReq;
    }

    


    
    
    /**
     * 获取 aas服务器地址
     * 
     * @return the aas_url
     */
    public String getAas_url()
    {
        return this.aas_url;
    }

    /**
     * set ass服务器地址
     * 
     * @param aas_url the aas_url to set
     */
    public void setAas_url(String aas_url)
    {
        this.aas_url = aas_url;
    }

    /**
     * @return the portalurl
     */
    public String getPortalurl()
    {
        return this.portalurl;
    }

    /**
     * @param portalurl the portalurl to set
     */
    public void setPortalurl(String portalurl)
    {
        this.portalurl = portalurl;
    }

    public String getBlogServer()
    {
        return this.blogServer;
    }


    public String getBlogServerIp()
    {
        return this.blogServerIp;
    }

    public void setBlogServerIp(String blogServerIp)
    {
        this.blogServerIp = blogServerIp;
    }

    public String getBlogServerPort()
    {
        return this.blogServerPort;
    }

    public void setBlogServerPort(String blogServerPort)
    {
        this.blogServerPort = blogServerPort;
    }

    public String getBlogServerApp()
    {
        return this.blogServerApp;
    }

    public void setBlogServerApp(String blogServerApp)
    {
        this.blogServerApp = blogServerApp;
    }

    public String getTerminalId()
    {
        return "ANDROID_TERMINAL";
    }

    public String getLocalCacheRoot()
    {
        return "woyou/" + this.userAccount + "/";
    }

    /**
     * 用于获取超邮未读邮件总数的地址
     * 
     * @return 超邮未读邮件总数的地址
     */
    public String getUnReadMailServiceUrl()
    {
        return this.unReadMailService_url;
    }

    /**
     * 
     * [用于设置超邮未读取邮件总数的地址]<BR>
     * [功能详细描述]
     * 
     * @param unReadMailUrl 超邮未读取邮件总数的地址
     */
    public void setUnReadMailServiceUrl(String unReadMailUrl)
    {
        this.unReadMailService_url = unReadMailUrl;
    }

    /**
     * 
     * [用于获取进入邮件详情的url地址]<BR>
     * [功能详细描述]
     * 
     * @return 进入邮件详情的url地址
     */
    public String getMailDetailsServiceUrl()
    {
        return this.mailDetailServiceUrl;
    }

    /**
     * 
     * [用于设置邮件详情的url地址]<BR>
     * [功能详细描述]
     * 
     * @param mailDetailsServiceUrl 邮件详情的url地址
     */
    public void setMailDetilsServiceUrl(String mailDetailsServiceUrl)
    {
    }

    /**
     * 用于获取超邮未读取邮件sap的action地址
     * 
     * @return 超邮未读取邮件sap的action地址
     */
    public String getUnReadMailSapActionUrl()
    {
        return this.unReadMailSOAPAction_url;
    }

    /**
     * 
     * [用于设置超邮未读取邮件sap的action地址]<BR>
     * [功能详细描述]
     * 
     * @param sapUrl 超邮未读取邮件sap的action地址
     */
    public void setUnreadMailSapActionUrl(String sapUrl)
    {
        this.unReadMailSOAPAction_url = sapUrl;
    }

    /**
     * 
     * [用于获取进入云邮系统的url地址]<BR>
     * [功能详细描述]
     * 
     * @return 进入云邮系统的url地址
     */
    public String getMailServiceUrl()
    {
        return this.mailServiceUrl;
    }

    /**
     * 
     * [用于设置进入云邮系统的url地址]<BR>
     * [功能详细描述]
     * 
     * @param mailServiceUrl 进入云邮系统的url地址
     */
    public void setMailServiceUrl(String mailServiceUrl)
    {
    }

    

    public String getCabHttpsUrl()
    {
        return cabHttpsUrl;
    }

    public void setCabHttpsUrl(String cabHttpsUrl)
    {
        this.cabHttpsUrl = cabHttpsUrl;
    }
   
    
    public static void main(String[] args) {
		Config.getInstance().setImspwd("11111");
		String log = Config.getInstance().getImspwd();
		
		System.out.println(log);
	}
}
