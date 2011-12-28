package test;

import org.apache.jasper.JspC;


public class Main {
	
	public static void main(String[] args) {
		
		String arg = "-compile -d c:/ -uriroot E:/Workspaces/MyEclipse9/android-server/WebRoot index.jsp";
		
		//JspC jspc = new JspC();
			JspC.main(arg.split("\\s+"));
		
	}
}
