package coffee.common.util;

import java.util.Random;

public class RandomUtils {
	
	public static void main(String[] args) {
		for (int i = 0; i < 50; i++) {
			System.out.println(nextInt(1, 2));
		}
	}

	private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String digitChar = "0123456789";

	
	/**
	 * 俩整形之间，包括0 
	 * @param start ： 最小值(包括自身) 从0开始 
	 * @param end : 最大值 (包含自身) 
	 * @return
	 */
	public static int nextInt(int start, int end) {
		/**
		 * 因为random.nextInt函数 [0,1）  包括0但是不包含1
		 */
		int newStart = start + 1;

		Random random = new Random();
		int val = random.nextInt(newStart) + end - start;
		return val;
	}

	/**
	 * 返回一个定长的随机字符串 (只包含数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateDigitString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(digitChar.charAt(random.nextInt(digitChar.length())));
		}
		return sb.toString();
	}

	/**
	 * 生成一个位于最小值与最大值之间的整数
	 * 
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return 数
	 */
	public static int generateIntBetweenMinMax(int min, int max) {
		Random random = new Random();
		if (min > max) {
			int tmp = 0;
			tmp = min;
			min = max;
			max = tmp;
		}
		return random.nextInt(max - min) + min;
	}

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(allChar.length())));
		}
		return sb.toString();
	}

	/**
	 * 生成一个位于两数值之间的随机长度字符串
	 * 
	 * @param minLen
	 *            最小长度值
	 * @param maxLen
	 *            最大长度值
	 * @return 字符串
	 */
	public static String generateString(int minLen, int maxLen) {
		Random random = new Random();
		if (minLen > maxLen) {
			int tmp = 0;
			tmp = minLen;
			minLen = maxLen;
			maxLen = tmp;
		}
		return generateString(random.nextInt(maxLen - minLen) + minLen);
	}

	/**
	 * 返回一个定长的随机纯字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateMixString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(allChar.charAt(random.nextInt(letterChar.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机纯小写字母字符串(只包含小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLowerString(int length) {
		return generateMixString(length).toLowerCase();
	}

	/**
	 * 返回一个定长的随机纯大写字母字符串(只包含大写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

	/**
	 * 生成一个定长的纯0字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(long num, int fixdlenth) {
		StringBuilder sb = new StringBuilder();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
					+ "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(int num, int fixdlenth) {
		StringBuilder sb = new StringBuilder();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
					+ "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}

}
