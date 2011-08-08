/*
 * Created on 2005-9-28
 *
 */
package org.coffee.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.coffee.common.util.io.Outer;
import org.coffee.tools.excel.XlsReader;
import org.coffee.tools.excel.XlsUtils;

/**
 * @author lbj
 *  
 */
public class Test {

    public static void main(String[] args) {
     
        List mobileMap = new ArrayList();
       // mobileList.add("15210788660");
       // mobileList.add("15210788660");
        XlsReader reader = new XlsReader("F:\\工作任务\\phone.xls");
        mobileMap = reader.query(0, 0, 0);
        List mobileList = XlsUtils.toStringList(mobileMap);
        int i=0;
        Outer.setPath("c:/wap_pust_phone.log", true, true);
        for(Iterator it = mobileList.iterator();it.hasNext();){
        	i++;
        	if(i%100 == 0){
        	}
        	String mobile = it.next().toString();
        }
       
        
    }
    
   
}
