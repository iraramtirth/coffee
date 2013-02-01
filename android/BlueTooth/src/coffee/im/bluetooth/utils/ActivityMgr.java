package coffee.im.bluetooth.utils;

import java.util.Stack;

import android.app.Activity;

/**
 * 项目用到的activity manager
 * 主要记录当前
 * @author wangtaoyfx
 * 		2013-1-14下午2:41:05
 */
public class ActivityMgr {
	
	public static Stack<Activity> stack = new Stack<Activity>();
	
	public static void push(Activity activity)
	{
		stack.push(activity);
	}
	
	public static Activity pop()
	{
		if(stack.isEmpty() == false)
		{
			return stack.pop();
		}
		return null;
	}
	
	public static Activity peek()
	{
		if(stack.isEmpty() == false)
		{
			return stack.peek();
		}
		return null;
	}
}
