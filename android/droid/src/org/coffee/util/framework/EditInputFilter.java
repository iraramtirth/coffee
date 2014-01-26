package org.coffee.util.framework;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

/**
 * EditText 过滤空白符\回车\& 等
 * 
 * @author coffee
 * 
 *         2013年12月28日上午11:49:26
 */
public class EditInputFilter implements InputFilter {

	private final String regx = "[/\\:*?<>|\"\\s]";

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		Log.d("InputFilter", source + "," + start + "," + end + " " + dest + "," + dstart + "," + dend);
		source = ToDBC(source.toString());
		if (source.toString().matches(regx)) {
			return source.toString().replaceAll(regx, "");
		}
		return null;
	}

	/**
	 * 中英文、半全角符号转换<br>
	 * 全角-->半角<br>
	 * 
	 * @param input
	 * @return
	 */
	public String ToDBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	/**
	 * 半角转全角
	 * 
	 * @param input
	 *            String.
	 * @return 全角字符串.
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000'; // 采用十六进制,相当于十进制的12288

			} else if (c[i] < '\177') { // 采用八进制,相当于十进制的127
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}
}
