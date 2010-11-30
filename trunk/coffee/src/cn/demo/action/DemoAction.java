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
		System.out.println("鎻掑叆鏁版嵁銆傘�銆傘�");
		return SUCCESS;
	}
}
