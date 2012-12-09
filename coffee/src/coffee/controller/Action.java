package coffee.controller;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import coffee.controller.annotation.Result;
import coffee.controller.util.BeanUtils;
import coffee.controller.util.MultipartStream;
import coffee.controller.util.ParameterReflect;
import coffee.controller.util.RequestUtils;
import coffee.spring.ObjectManager;
import coffee.spring.ioc.annotation.Resource;

/**
 * action 的公共父类 总的控制器
 * 
 * @author coffee
 */
public abstract class Action extends HttpServlet implements Constants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected ServletContext application;

	public final String charset = "UTF-8";
	
	protected Map<String, Object> parameterMap = new HashMap<String, Object>();

	@Override
	public void init() throws ServletException {
		try {
			/**
			 * 为Action中被Resource注解的对象进行注入
			 */
			for (PropertyDescriptor prop : BeanUtils.getPropertyDescriptors(this)) {
				/**
				 *  若该属性不存在writeMethod，则不对该属性进行依赖注入
				 */
				if(prop.getWriteMethod() != null){
					Resource resource = prop.getWriteMethod().getAnnotation(Resource.class);
					if (resource != null) {
						prop.getWriteMethod().invoke(this,ObjectManager.getIocObject().get(prop.getName()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.init();
	}

	/**
	 * 初始化 .....
	 */
	private void init(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		this.application = request.getSession().getServletContext();
		try {
			//初始化属性为null
			for (PropertyDescriptor prop : BeanUtils.getPropertyDescriptors(this)) {
				if(prop.getWriteMethod() != null){
					Resource resource = prop.getWriteMethod().getAnnotation(Resource.class);
					// 如果该属性被注解/注入；则不进行初始化工作
					if(resource == null){
						prop.getWriteMethod().invoke(this, new Object[]{null});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 该方法没有任何作用；运行时将会有其子类覆盖，并执行之
	 */
	public abstract String execute();

	/**
	 * get请求
	 * 
	 * 注意 ServletInputStream 流只能读取一次，第二次便取不到其中的内容了
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		this.init(request, response);
		boolean bool = false;
		/**
		 * 从parameterMap中分析参数
		 */
		if (request.getParameterMap().size() > 0) {
			StringBuffer buf = null;
			for (String key : request.getParameterMap().keySet()) {
				buf = new StringBuffer();
				String[] vals = request.getParameterMap().get(key);
				for(int i=0; i<vals.length; i++){
					buf.append(vals[i]);
					if(i+1 < vals.length){
						buf.append(",");
					}
				}
				this.parameterMap.put(key, buf);
			}
			bool = true;
		} else {/**
				 * 从InputStrean流中解析的参数
				 **/
			if (MultipartStream.isMultipartContent(request)) {
				this.parameterMap = new MultipartStream(request).parser();
				if(this.parameterMap.size() > 0){
					bool = true;
				}
			} 
		}
		// 为真则说明parameterMap中参数不为空
		if(bool){
			try {
				ParameterReflect pr = new ParameterReflect();
				//解析参数并执行参数反射
				pr.invoke(parameterMap,this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			bool = false;
		}
		// 请求转发 按照url参数method指定的值，反射执行
		String targetMathod = request.getParameter("method");
		String uri = request.getRequestURI();
		targetMathod = uri.substring(uri.lastIndexOf("/") + 1);
		// 去后缀
		if (targetMathod.endsWith(".action")) {
			targetMathod = targetMathod.substring(0, targetMathod
					.indexOf(".action"));
		}
		this.dispatchRequest(targetMathod);
	}

	// post 请求
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * 调用相应的action方法  ：insert query update 等方法
	 * 并执行响应的页面跳转：底层调用的是request的sendRedirect | getRequestDispatcher
	 */
	private void dispatchRequest(String targetMathod) {
		if (targetMathod != null) {
			try {
				Method method = this.getClass().getMethod(targetMathod,
						new Class[] {});
				/**
				 * 此行代码将执行到相应action的方法（execute）中
				 */
				method.invoke(this, new Object[]{});
				/**
				 * 设置request属性 ：将Action的属性以及其值设置到request的attribute中
				 * request.setAttribute
				 */
				RequestUtils.setAttribute(this,request);
				Result result = method.getAnnotation(Result.class);
				// 跳转的页面
				String page = result.page(); // 注意该路径以工程的contextPath为根路径
				// 请求转发类型
				Result.Type type = result.type();
				/**
				 * 注意请求转发时候的路径 转发：相对于向前请求的上下路径为根路径 重定义相当于主机的跟主机的
				 **/
				if (type.equals(Result.Type.REDIRECT)) {
					this.request.getRequestDispatcher(page).forward(
							this.request, this.response);
				} else {
					page = request.getContextPath() + page;
					this.response.sendRedirect(page);
				}
				return; // 停止继续执行
			} catch (Exception e) {
				e.printStackTrace();
				// 如果抛出异常；或者没有指定相应的method；则执行默认的execute方法
				// this.dispatchRequest("execute");
			}
		}
	}

}
