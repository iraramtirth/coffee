package coffee.util.activity;

import java.util.Stack;

/**
 * TabActivity的访问历史
 * 记录TAB之间， activity的跳转记录
 * @author wangtao
 */
public class History {
	
	private static Stack<Integer> historyStack = new Stack<Integer>();
	
	static{
		//初始化放入一个元素
		historyStack.clear();
		historyStack.push(0);
	}
	/**
	 * 访问一个TAB入栈 
	 */
	public static void go(Integer go){
		//如果俩元素相同，则跳过
		if(historyStack.size() > 0 &&
				go == historyStack.lastElement()){
			return;
		}
		historyStack.push(go);
//		System.out.println("GO-"+historyStack);
	}
	/**
	 * 后退
	 */
	public static void back(){
		if(historyStack.size() > 0){
			historyStack.pop();
		}
//		System.out.println("back-"+historyStack);
	}
	
	/**
	 * 当==-1的时候 程序退出
	 */
	public static int getTop(){
		if(historyStack.size() > 0){
			return historyStack.lastElement();
		}
		return -1;
	}
	
	public static void clear(){
		historyStack.clear();
	}
	
	public static int size(){
		return historyStack.size();
	}
	
}
