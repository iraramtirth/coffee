/*
 * 文件名: ContactUtils.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: Maoah 创建时间:2012-5-2 修改人： 修改时间: 修改内容：[修改内容]
 */
package coffee.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 通讯录相关工具类<BR>
 * 
 * @author wangtao
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-5-2]
 */
public class ContactUtils
{
    /**
     * *** 去重规则 *** 1、SIM卡去重 SIM卡某个联系人的姓名和手机号码 （小于或等于） 本地通讯录内联系人的姓名和手机号码 举例1：
     * （等于关系）张三 1381111 等于 张三 1381111 ； 举例2： （包含关系）张三 1381111 小于 张三 1381111
     * 1382222 2、本地通讯录去重 姓名相同，手机号码完全相同的，只显示第一个数据； 姓名相同，手机号码属于包含关系的，显示手机号码多的数据；
     * 
     * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-5-2]
     */
    private class Item
    {
        private String name;
        private Set<String> phoneSet = new HashSet<String>();

        public Item(String name, List<String> phoneList)
        {
            if(name == null)
            {
                name = "";
            }
            this.name = name.trim();
            
            if(phoneList != null)
            {
                this.phoneSet.addAll(phoneList);
            }
        }

        /**
         * [一句话功能简述]<BR>
         * [功能详细描述]
         * @return
         * @see java.lang.Object#hashCode()
         */
        
   

        /**
         * [一句话功能简述]<BR>
         * [功能详细描述]
         * @param obj
         * @return
         * @see java.lang.Object#equals(java.lang.Object)
         */
        
        @Override
        public boolean equals(Object obj)
        {
            //同一个对象 ： 返回 true
            if (this == obj)
            {
                return true;
            }
              
            //类型不一致 ： 返回false  
            if (obj == null || getClass() != obj.getClass())
            {
                return false;
            }
            
            Item other = (Item) obj;
            
            //name不同： 返回false 
            if (!this.name.equals(other.name))
            {
                return false;
            }
            
            /**
             * 以上代码不必考虑，直接往下看
             * 
             * 以下开始去重    : 
             */
            if(this.phoneSet.containsAll(other.phoneSet) || 
                other.phoneSet.containsAll(this.phoneSet))
            {
                //如果俩phoneSet存在包含关系
                if(this.phoneSet.size() >= other.phoneSet.size())
                {
                    
                }
                else
                {
                    //取phoneSet大的值
                    this.phoneSet = other.phoneSet;
                }
                return true;
            }
            else
            {
                return false;
            }
        }

		@Override
		public int hashCode() {
			return 0x0011aa;
		}
    }

    /**
     * 通讯录联系人排重 <BR>
     * 
     * @param contectList ： 联系人列表
     * @return
     */
    public void filter(List<PhoneContactIndexModel> contactList)
    {
        Set<Item> itemsSet = new HashSet<ContactUtils.Item>();
        
        for(Iterator<PhoneContactIndexModel> it = contactList.iterator(); it.hasNext();)
        {
            PhoneContactIndexModel contact = it.next();
            Item item = new Item(contact.getDisplayName(), contact.getPhoneNumbers());
            
            //如果添加成功：即不重复
            if(itemsSet.add(item))
            {   
                continue;
            }
            else
            {
            	//去重 过滤掉该条记录
            	it.remove();
            }
        }
        
    }
    
    public static void main(String[] args) {
    	List<PhoneContactIndexModel> contactList = new ArrayList<PhoneContactIndexModel>();
    	PhoneContactIndexModel item = new PhoneContactIndexModel();
    	
    	item = new PhoneContactIndexModel();
    	item.setDisplayName("张三");
    	item.getPhoneNumbers().add("1111");
    	contactList.add(item);
    	
    	item = new PhoneContactIndexModel();
    	item.setDisplayName("张三");
    	item.getPhoneNumbers().add("1111");
    	item.getPhoneNumbers().add("1111");
    	item.getPhoneNumbers().add("3333");
    	contactList.add(item);
    	
    	System.out.println(contactList);
    	
    	new ContactUtils().filter(contactList);
    	
    	System.out.println(contactList);
	}
}
