package com.googlecode.xmpplib;

import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;


/**
 * 保存socket 
 * @author coffee
 */
public class SocketContainer { 
	/**
	 * k：clientId
	 * v: socket
	 */
	public static Map<String, Socket> socketMap = new Hashtable<String, Socket>();
}
