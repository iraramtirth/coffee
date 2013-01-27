package org.coffee.util.exception;

/**
 *系统异常， 需要在程序中捕获且处理的异常
 * @author wangtao
 */
@SuppressWarnings("serial")
public class SystemException extends Exception{
	
	public final static String NET_EXCEPTION = "无法访问远程主机，信号异常";
	
	private IException exception;
	
	public SystemException(IException exp, String msg){
		super(exp + "," +msg);
		this.exception = exp;
	}
	
	public IException getException(){
		return this.exception;
	}
}
