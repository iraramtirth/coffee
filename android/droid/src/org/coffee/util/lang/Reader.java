package org.coffee.util.lang;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * 文件读取类
 * 
 * @author coffee <br>
 *         2014年4月29日下午3:39:20
 */
public class Reader {
	private BufferedReader in;
	private String line;

	public Reader(String file) {
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			URL url = this.getClass().getClassLoader().getResource("./" + file);
			if (url != null) {
				file = url.getFile();
			}
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @return ： 当文件全部读完的时候返回 null
	 */
	public String readLine() {
		try {
			if ((line = in.readLine()) != null) {
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取完毕,需要根据情况关闭流
	 * 
	 * @return
	 */
	public String readAll() {
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public void close() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Reader in = new Reader("c:/mmb_access.2011-06-02");
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
	}
}
