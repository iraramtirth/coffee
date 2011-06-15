package org.coffee.common.util.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Reader {
	private BufferedReader in;
	private String line;
	public Reader(String file){
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return ： 当文件全部读完的时候返回 null 
	 */
	public String readLine(){
		try {
			if((line = in.readLine()) != null){
				return line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		Reader in = new Reader("c:/mmb_access.2011-06-02");
		String line;
		while((line = in.readLine()) != null){
			System.out.println(line);
		}
	}
}