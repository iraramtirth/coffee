package coffee.cms.admin.action;

import javax.servlet.http.HttpServletRequest;

import coffee.cms.core.action.BaseAction;


public class UserAction extends BaseAction{
	
	private HttpServletRequest request;
	
	private UserAction(HttpServletRequest request){
		this.request = request;
	}
	
	public void query()
	{
		String sid = request.getParameter("sid");
		
		
		
	}
}
