package cn.demo.action;

import javax.servlet.annotation.WebServlet;

import org.coffee.struts.Action;
import org.coffee.struts.annotation.Path;

@WebServlet("/demo/*")
public class DemoAction extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() {
		System.out.println("xxxxxx");
		return null;
	}
	@Path("/insert")
	public String insert(){
		System.out.println("插入数据。。。。");
		return SUCCESS;
	}
}
