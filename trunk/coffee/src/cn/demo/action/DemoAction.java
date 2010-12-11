package cn.demo.action;

import java.io.InputStream;

import javax.servlet.annotation.WebServlet;

import org.coffee.struts.Action;
import org.coffee.struts.annotation.Path;
import org.coffee.struts.annotation.Result;
import org.coffee.struts.annotation.Result.Type;
import org.coffee.util.PagerModel;

import cn.demo.bean.User;

@WebServlet("/demo/*")
public class DemoAction extends Action {

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	private PagerModel<User> pager = new PagerModel<User>();
	
	
	@Override
	@Result(page="index.jsp")
	public String execute() {
		System.out.println("xxxxxx");
		return SUCCESS;
	}
	
	@Path("/insert")
	@Result(page="/index.jsp",type=Type.DISPATCHER)
	public String insert()throws Exception{
		System.out.println("insert....");
		System.out.println(request.getParameter("test"));
		InputStream ins = request.getInputStream();
		System.out.println(ins.available());
		return SUCCESS;
	}
	@Result(page="/admin/list.jsp")
	public String list()throws Exception{
		System.out.println(pager.getSize());
		request.setAttribute("pager", pager);
		return SUCCESS;
	}
}
