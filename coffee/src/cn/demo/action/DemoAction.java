package cn.demo.action;

import javax.servlet.annotation.WebServlet;

import org.coffee.struts.Action;
import org.coffee.struts.annotation.Path;
import org.coffee.struts.annotation.Result;
import org.coffee.struts.annotation.Result.Type;

@WebServlet("/demo/*")
public class DemoAction extends Action {

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	@Override
	@Result(page="index.jsp")
	public String execute() {
		System.out.println("xxxxxx");
		return SUCCESS;
	}
	
	@Path("/insert")
	@Result(page="/index.jsp",type=Type.DISPATCHER)
	public String insert(){
		System.out.println("insert....");
		return SUCCESS;
	}
}
