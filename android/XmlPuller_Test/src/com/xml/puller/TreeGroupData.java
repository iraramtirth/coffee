package com.xml.puller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.chinaunicom.woyou.utils.xml.annotation.XmlElement;
import com.chinaunicom.woyou.utils.xml.annotation.XmlRootElement;

/**
 * 
 * 企业群相关数据解析封装类<BR>
 * [功能详细描述]
 * 
 * @author Leung
 * @version [ME MTVClient_Handset V100R001C04SPC002, 2012-8-16]
 */
public class TreeGroupData {

    /**
     * 用于群的根节点和上/下级群
     */
    @XmlRootElement(name = "result")
    public static class RootGroup {
        /**
         * 返回码
         */
        @XmlElement(name = "retcode")
        private int retcode;

        /**
         * 返回码描述
         */
        @XmlElement(name = "retdesc")
        private String retdesc;

        @XmlElement(name = "groupinfo", type = GroupInfo.class)
        private List<GroupInfo> groupList = new ArrayList<GroupInfo>();

        public int getRetcode() {
            return retcode;
        }

        public void setRetcode(int retcode) {
            this.retcode = retcode;
        }

        public String getRetdesc() {
            return retdesc;
        }

        public void setRetdesc(String retdesc) {
            this.retdesc = retdesc;
        }

        public List<GroupInfo> getGroupList() {
            return groupList;
        }

        public void setGroupList(List<GroupInfo> groupList) {
            this.groupList = groupList;
        }

    }

    @XmlRootElement(name = "result")
    public static class TreeGroupMember {
        /**
         * 返回码
         */
        @XmlElement
        private int retCode;

        /**
         * 返回码描述
         */
        @XmlElement
        private String retDesc;

        @XmlElement(name = "gpmemlist", type = GroupList.class)
        private GroupList groupList;

        public int getRetCode() {
            return retCode;
        }

        public void setRetCode(int retCode) {
            this.retCode = retCode;
        }

        public String getRetDesc() {
            return retDesc;
        }

        public void setRetDesc(String retDesc) {
            this.retDesc = retDesc;
        }

        public GroupList getGroupList() {
            return groupList;
        }

        public void setGroupList(GroupList groupList) {
            this.groupList = groupList;
        }
    }

    @XmlRootElement(name = "gpmemlist")
    public static class GroupList {
        /**
         * 群组 id
         */
        @XmlElement(name = "gpid")
        private String groupId;

        @XmlElement(name = "gpmemitem", type = TreeMemberInfo.class)
        private List<TreeMemberInfo> gpMemItemList = new ArrayList<TreeGroupData.TreeMemberInfo>();

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public List<TreeMemberInfo> getGpMemItemList() {
            return gpMemItemList;
        }

        public void setGpMemItemList(List<TreeMemberInfo> gpMemItemList) {
            this.gpMemItemList = gpMemItemList;
        }
    }

    @XmlRootElement(name = "gpmemitem")
    public static class TreeMemberInfo implements Serializable {
        /**
         * 成员id
         */
        @XmlElement(name = "gpmemid")
        private String memberId;

        @XmlElement(name = "gpmeminfoitem", type = MemberInfoItem.class)
        private List<MemberInfoItem> memberInfiItem = new ArrayList<TreeGroupData.MemberInfoItem>();

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public List<MemberInfoItem> getMemberInfiItem() {
            return memberInfiItem;
        }

        public void setMemberInfiItem(List<MemberInfoItem> memberInfiItem) {
            this.memberInfiItem = memberInfiItem;
        }

    }

    @XmlRootElement(name = "gpmeminfoitem")
    public static class MemberInfoItem implements Serializable {
        /**
         * 成员信息对应的字段
         */
        @XmlElement(name = "gpmeminfoitemname")
        private String memberInfoItemName;

        /**
         * 成员信息对应的值
         */
        @XmlElement(name = "gpmeminfoitemval")
        private String gpMemInfoItemVal;

        public String getMemberInfoItemName() {
            return memberInfoItemName;
        }

        public void setMemberInfoItemName(String memberInfoItemName) {
            this.memberInfoItemName = memberInfoItemName;
        }

        public String getGpMemInfoItemVal() {
            return gpMemInfoItemVal;
        }

        public void setGpMemInfoItemVal(String gpMemInfoItemVal) {
            this.gpMemInfoItemVal = gpMemInfoItemVal;
        }

    }

    /**
     * 
     * Http返回 共用<BR>
     * [功能详细描述]
     * 
     * @author Leung
     * @version [ME MTVClient_Handset V100R001C04SPC002, 2012-8-16]
     */
    @XmlRootElement(name = "result")
    public static class Result {
        /**
         * 返回码
         */
        @XmlElement(name = "retcode")
        private int retcode;

        /**
         * 返回码描述
         */
        @XmlElement(name = "retdesc")
        private String retdesc;

        public int getRetcode() {
            return retcode;
        }

        public void setRetcode(int retcode) {
            this.retcode = retcode;
        }

        public String getRetdesc() {
            return retdesc;
        }

        public void setRetdesc(String retdesc) {
            this.retdesc = retdesc;
        }

    }

    /**
     * 
     * 群封装类<BR>
     * [功能详细描述]
     * 
     * @author Leung
     * @version [ME MTVClient_Handset V100R001C04SPC002, 2012-8-16]
     */
    @XmlRootElement
    public static class GroupInfo implements Serializable {
        /**
         * 群组id
         */
        @XmlElement(name = "groupid")
        private String groupId;

        /**
         * 群组名
         */
        @XmlElement(name = "groupname")
        private String groupName;

        /**
         * 创建者id
         */
        @XmlElement(name = "ownerid")
        private String ownerId;

        @XmlElement(name = "orgname")
        private String orgName;

        @XmlElement(name = "grouprelationid")
        private String grouprelationId;

        /**
         * 是否有下级群
         */
        @XmlElement(name = "ifhasson")
        private String ifHasSon;

        /**
         * 上级群id
         */
        @XmlElement(name = "fatherid")
        private String fatherId;

        @XmlElement(name = "realnamemsisdn")
        private String realnamemsisdn;

        /**
         * 群组成员数
         */
        @XmlElement(name = "membernumer")
        private String membernumer;

        @XmlElement(name = "isuserarea")
        private String isuserarea;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getGrouprelationId() {
            return grouprelationId;
        }

        public void setGrouprelationId(String grouprelationId) {
            this.grouprelationId = grouprelationId;
        }

        public String getIfHasSon() {
            return ifHasSon;
        }

        public void setIfHasSon(String ifHasSon) {
            this.ifHasSon = ifHasSon;
        }

        public String getFatherId() {
            return fatherId;
        }

        public void setFatherId(String fatherId) {
            this.fatherId = fatherId;
        }

        public String getRealnamemsisdn() {
            return realnamemsisdn;
        }

        public void setRealnamemsisdn(String realnamemsisdn) {
            this.realnamemsisdn = realnamemsisdn;
        }

        public String getMembernumer() {
            return membernumer;
        }

        public void setMembernumer(String membernumer) {
            this.membernumer = membernumer;
        }

        public String getIsuserarea() {
            return isuserarea;
        }

        public void setIsuserarea(String isuserarea) {
            this.isuserarea = isuserarea;
        }

    }
}
