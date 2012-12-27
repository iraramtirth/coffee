/*
 * @(#)Contact.java 11-10-10 上午11:04 CopyRight 2011. All rights reserved
 */
package coffee.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 联系人信息<BR>
 * 
 * 
 * @author 许金波
 * @version [ME MTVClient_Handset V100R001C04SPC002, 2011-10-17]
 */
public class PhoneContactIndexModel extends BaseContactModel
{

    /**
     * 手机联系人
     */
    public static final int CONTACT_TYPE_PHONE = 0;
    /**
     * SIM卡1联系人
     */
    public static final int CONTACT_TYPE_SIM_ONE = 1;
    /**
     * SIM卡2联系人(如果有SIM卡2的话)
     */
    public static final int CONTACT_TYPE_SIM_TWO = 2;

    /**
     * 修改标记：新增
     */
    public static final int CONTACT_MODIFY_FLAG_ADD = 0;
    /**
     * 修改标记：修改
     */
    public static final int CONTACT_MODIFY_FLAG_UPDATE = 1;
    /**
     * 修改标记：删除
     */
    public static final int CONTACT_MODIFY_FLAG_DELETE = 2;

    /**
     * 修改标记：正常，没变更
     */
    public static final int CONTACT_MODIFY_FLAG_NORMAL = 3;

    /**
     * 用户被加好友的验证方式说明: 1：允许任何人
     */
    public static final int ADDFRIENDPRIVACY_ALLOW_ALL = 1;

    /**
     * 用户被加好友的验证方式说明: 2：需要验证信息
     */
    public static final int ADDFRIENDPRIVACY_NEED_CONFIRM = 2;

    /**
     * 用户被加好友的验证方式说明: 3：允许通讯录的沃友用户（客户端无特殊操作，同赋值1）
     */
    public static final int ADDFRIENDPRIVACY_ALLOW_CONTACT = 3;

    /**
     * 用户被加好友的验证方式说明: 4：不允许任何人
     */
    public static final int ADDFRIENDPRIVACY_NO_ALLOW = 4;

    /**
     * 用户被加好友的验证方式说明: 5：允许绑定了手机号码的用户经我确认后加我为好友（暂未使用）
     */
    public static final int ADDFRIENDPRIVACY_ALLOW_BIND = 5;

    private static final long serialVersionUID = 1L;

    /**
     * 在数据库表中的ID
     */
    private long id;

    /**
     * 联系人的本地通讯录ID
     */
    private String contactLUID;

    /**
     * 联系人在服务器上的ID
     */
    private String contactGUID;

    /**
     * 联系人在沃友系统上的唯一标识
     */
    private String contactSysId;

    /**
     * 联系人沃友ID
     */
    private String contactUserId;

    /**
     * 用户被加好友的验证方式说明： <br>
     * 1：允许任何人 <br>
     * 2：需要验证信息 <br>
     * 3：允许通讯录的沃友用户（客户端无特殊操作，同赋值1） <br>
     * 4：不允许任何人 <br>
     * 5：允许绑定了手机号码的用户经我确认后加我为好友（暂未使用）
     */
    private int addFriendPrivacy;

    /**
     * 手机号码集合
     */
    private List<String> phoneNumbers = new ArrayList<String>();

    /**
     * 邮箱集合
     */
    private List<String> emailAddrs;

    /**
     * 签名
     */
    private String signature;

    /**
     * 是否是沃友
     */
    private boolean isWoYou;

    /**
     * 是否是我的好友
     */
    private boolean isMyFriend;

    /**
     * 图像
     */
    private long photoId;

    /**
     * 联系人类型
     */
    private int contactType;

    /**
     * 联系人Crc值, 用于比对是否修改
     */
    private String contactCrcValue;
    
    /**
     * 标记是否有手机号码
     */
    private int hasPhoneNumber;

    /**
     * 联系人修改标记
     */
    private int contactModifyFlag = CONTACT_MODIFY_FLAG_ADD;

    /**
     * [构造简要说明]
     */
    public PhoneContactIndexModel()
    {
        super();
    }

    public String getContactLUID()
    {
        return contactLUID;
    }

    public void setContactLUID(String contactLUID)
    {
        this.contactLUID = contactLUID;
    }

    public String getContactGUID()
    {
        return contactGUID;
    }

    public String getContactSysId()
    {
        return contactSysId;
    }

    public void setContactSysId(String contactSysId)
    {
        this.contactSysId = contactSysId;
    }

    public void setContactGUID(String contactGUID)
    {
        this.contactGUID = contactGUID;
    }

    public List<String> getPhoneNumbers()
    {
        return phoneNumbers;
    }

    /**
     * 增加一个电话<BR>
     * 
     * @param phoneNumber 电话
     */
    public void addPhoneNumber(String phoneNumber)
    {
        if (phoneNumbers == null)
        {
            phoneNumbers = new ArrayList<String>();
        }
        phoneNumbers.add(phoneNumber);
    }

    public List<String> getEmailAddrs()
    {
        return emailAddrs;
    }

    /**
     * 增加一个邮箱<BR>
     * 
     * @param emailAddr 邮箱地址
     */
    public void addEmailAddr(String emailAddr)
    {
        if (emailAddrs == null)
        {
            emailAddrs = new ArrayList<String>();
        }
        emailAddrs.add(emailAddr);
    }

    public String getSignature()
    {
        return signature;
    }

    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    public boolean isWoYou()
    {
        return isWoYou;
    }

    public void setWoYou(boolean woyou)
    {
        this.isWoYou = woyou;
    }

    public boolean isMyFriend()
    {
        return isMyFriend;
    }

    public void setMyFriend(boolean myFriend)
    {
        this.isMyFriend = myFriend;
    }

    public long getPhotoId()
    {
        return photoId;
    }

    public void setPhotoId(long photoId)
    {
        this.photoId = photoId;
    }

    public int getContactType()
    {
        return contactType;
    }

    public void setContactType(int contactType)
    {
        this.contactType = contactType;
    }

    public String getContactCrcValue()
    {
        return contactCrcValue;
    }

    public void setContactCrcValue(String contactCrcValue)
    {
        this.contactCrcValue = contactCrcValue;
    }

    public int getContactModifyFlag()
    {
        return contactModifyFlag;
    }

    public void setContactModifyFlag(int contactModifyFlag)
    {
        this.contactModifyFlag = contactModifyFlag;
    }

    public String getContactUserId()
    {
        return contactUserId;
    }

    public void setContactUserId(String contactUserId)
    {
        this.contactUserId = contactUserId;
    }

    public int getAddFriendPrivacy()
    {
        return addFriendPrivacy;
    }

    public void setAddFriendPrivacy(int addFriendPrivacy)
    {
        this.addFriendPrivacy = addFriendPrivacy;
    }

    public int getHasPhoneNumber()
    {
        return hasPhoneNumber;
    }

    public void setHasPhoneNumber(int hasPhoneNumber)
    {
        this.hasPhoneNumber = hasPhoneNumber;
    }

    /**
     * 获取可打印的对象信息
     * 
     * @return 可打印的对象信息
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("phoneNumbers:").append(phoneNumbers);
        return sb.toString();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
