package org.coffee.tools.log;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class Logger {
	private static Handler handler;
	static {
		handler = new ConsoleHandler();  
		handler.setLevel(Level.ALL); 
	}
	/**
	 * 获取指定级别的日志
	 */ 
	public static java.util.logging.Logger getLogger(Level level){
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger("logger");
		logger.addHandler(handler);
		//设置日志的级别
		logger.setLevel(level);
		return logger;
	}
	/**
	 * 默认级别  INFO
	 */
	public static java.util.logging.Logger getLogger(){
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger("logger");
		logger.setLevel(Level.INFO);
		return logger;
	}
	
	public static void main(String[] args) {
		java.util.logging.Logger logger = Logger.getLogger(Level.CONFIG);
		
		logger.config("xxxx");
	}
}
