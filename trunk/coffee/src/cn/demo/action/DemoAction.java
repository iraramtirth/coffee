package cn.demo.action;

import javax.servlet.annotation.WebServlet;

import org.coffee.hibernate.service.TService;
import org.coffee.spring.ioc.annotation.Resource;
import org.coffee.struts.Action;
import org.coffee.struts.PagerModel;
import org.coffee.struts.annotation.Result;

import cn.demo.bean.User;

@WebServlet("/user/*")
public class DemoAction extends Action {

	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;

	private PagerModel<User> pager = new PagerModel<User>();
	private TService service;
	private String info;
	private User model;
	
	@Override
	public String execute() {
		System.out.println("xxxxxx");
		return SUCCESS;
	}
	
	@Result(page="/user/list.action")
	public String insert()throws Exception{
		System.out.println("开始插入数据。。。。。。。。");
		this.service.insert(model);
		return SUCCESS;
	}
	@Result(page="/demo/list.jsp")
	public String list()throws Exception{
		String sql = "select * from users order by ID desc";
		PagerModel<User> pager = this.service.queryForPagerModel(sql, this.pager.getOffset(), 10, User.class);
		request.setAttribute("pager", pager);
		return SUCCESS;
	}
	
	@Result(page="/demo/insert.jsp")
	public String toInsert(){
		
		return SUCCESS;
	}
	 
	@Result(page="/demo/update.jsp")
	public String toUpdate()throws Exception{
		User user = this.service.queryForObject(model.getClass(), model.getId());
		this.setModel(user);
		return SUCCESS;
	}
	public String update()throws Exception{
		this.service.update(this.model);
		return UPDATE;
	}
	@Result(page="/user/list.action")
	public String delete() throws Exception{
		this.service.delete(model.getClass(), this.model.getId());
		return DELETE;
	}
	// 批量删除
	
	public String deleteBatch() throws Exception{
		String ids = request.getParameter("ids");
		if(ids != null){
			this.service.deleteBatch(User.class, ids);			
		}
		return DELETE;
	}
	
	// 检测用户名是否存在
	public String checkUsername()throws Exception{
		try {
			String sql = "select count(*) from users where username ='"+this.model.getUsername()+"'";;
			Integer user = this.service.queryForObject(Integer.class,sql);
			if(user == 0){
				this.info = "恭喜！可以注册";
			}else{
				this.info = "用户名已经存在";
			}
			request.setAttribute("info", info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

	// setter getter
	@Resource(name="service")
	public void setService(TService service) {
		this.service = service;
	}
	public void setModel(User model) {
		this.model = model;
	}
	public User getModel() {
		return model;
	}
	public PagerModel<User> getPager() {
		return pager;
	}
}
