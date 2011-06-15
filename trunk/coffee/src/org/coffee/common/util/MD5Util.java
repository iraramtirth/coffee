package org.coffee.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static byte[] getKeyedDigest(byte[] buffer) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String getKeyedDigest(String strSrc) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(strSrc.getBytes("UTF-8"));

            String result = "";
            byte[] temp;
            temp = md5.digest();
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString(
                        (0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(getKeyedDigest(
                        "101&12984418&scaiedkqlzqkdqie783132*^$%#!&<?xml version=\"1.0\" encoding=\"UTF-8\"?><product><productId>11</productId><name>特步男子百搭时尚慢跑鞋</name><catalogId>16</catalogId><attr></attr><introduction>鞋子款型为经典百搭款式,可随意搭配各类服饰,做工精细,时尚大方.</introduction><abstract>材质:优质合成革/橡胶底newLine舒适性:纯棉内里,舒适经穿newLine耐磨性:耐磨鞋底,减震护脚newLine重量:300g</abstract><image>http://qudao.ebinf.com/qshop_admin/upload/20110222/1298357940388443.jpg</image><status>1</status><code>17711186</code><price>198</price><price1>318</price1><brandId>8</brandId><createTime>2011-02-22 13:51:15</createTime><updateTime>2011-02-22 15:01:53</updateTime><imageList/><subProductList><subProduct><subProductId>15</subProductId><color>白</color><size>40</size><stockCode>17711183</stockCode><status>1</status></subProduct><subProduct><subProductId>16</subProductId><color>白</color><size>41</size><stockCode>17711184</stockCode><status>1</status></subProduct><subProduct><subProductId>17</subProductId><color>白</color><size>42</size><stockCode>17711185</stockCode><status>1</status></subProduct><subProduct><subProductId>18</subProductId><color>白</color><size>43</size><stockCode>17711186</stockCode><status>1</status></subProduct><subProduct><subProductId>19</subProductId><color>白</color><size>44</size><stockCode>17711187</stockCode><status>1</status></subProduct><subProduct><subProductId>20</subProductId><color>白</color><size>45</size><stockCode>17711188</stockCode><status>1</status></subProduct></subProductList></product>")
                        .toUpperCase());
    }

}
