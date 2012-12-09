package coffee.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

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
    	String key = UUID.randomUUID().toString();
    	System.out.println(key);
        System.out.println(getKeyedDigest(key));
    }

}
