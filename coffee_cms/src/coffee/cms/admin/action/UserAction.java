package coffee.cms.admin.action;

import javax.servlet.http.HttpServletRequest;

import coffee.cms.core.action.Action;


public class UserAction extends Action{
	
	private HttpServletRequest request;
	
	private UserAction(HttpServletRequest request){
		this.request = request;
	}
	
	public void query()
	{
		String sid = request.getParameter("sid");
		
		
		
	}
}
