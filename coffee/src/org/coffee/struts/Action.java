package org.coffee.struts;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.coffee.spring.ObjectManager;
import org.coffee.spring.ioc.annotation.Resource;
import org.coffee.struts.annotation.Result;
import org.coffee.struts.reflect.ParamaterReflect;
import org.coffee.struts.upload.FormFile;
import org.coffee.struts.upload.MultipartStream;

/**
 * action 的公共父类 总的控制器
 * 
 * @author wangtao
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
			BeanInfo bi = Introspector.getBeanInfo(this.getClass(),
					Action.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				Resource resource = prop.getWriteMethod().getAnnotation(
						Resource.class);
				if (resource != null) {
					prop.getWriteMethod().invoke(this,
							ObjectManager.getIocObject().get(prop.getName()));
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
		this.application = request.getServletContext();
	}

	/**
	 * 该方法没有任何作用；运行时将会有其子类覆盖，并执行之
	 */
	public abstract String execute();

	/**
	 * get请求
	 * 
	 *  注意 ServletInputStream 流只能读取一次，第二次便取不到其中的内容了
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.init(request, response);
		boolean bool = false;
		if (request.getParameterMap().size() > 0) {
			for (String key : request.getParameterMap().keySet()) {
				this.parameterMap.put(key, request.getParameterMap().get(key));
			}
			bool = true;
		} else {
			if (MultipartStream.isMultipartContent(request)) {
				this.parameterMap = new MultipartStream(request).parser();
				if(this.parameterMap.size() > 0){
					bool = true;
				}
			} 
		}
		if(bool){
			ParamaterReflect pr = new ParamaterReflect();
			pr.parserParameter(parameterMap, this);
			pr.invoke(this);
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
	 * 转发请求
	 */
	private void dispatchRequest(String targetMathod) {
		if (targetMathod != null) {
			try {
				Method method = this.getClass().getMethod(targetMathod,
						new Class[] {});
				method.invoke(this, new Object[] {});
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

	/**
	 * 参数映射 按照class中的属性查找映射
	 * 
	 * @param preName
	 *            参数前缀名：如 user.username 此时preName=user
	 **/
	@Deprecated
	public Object paramsReflect(String preName, Class<?> clazz) {
		try {
			BeanInfo bi = null;
			Object targetObj = null;
			// 处理 Servlet 里面的属性
			if (preName == null) {
				preName = "";
				bi = Introspector.getBeanInfo(clazz, Action.class);
				targetObj = this;
			} else {// 处理Model中的属性
				preName += ".";
				try {
					bi = Introspector.getBeanInfo(clazz, Object.class);
				} catch (Exception e) {
					// java.lang.Object not superclass of ....
					// 说明 Action 中的Field不需要映射
					return null;
				}
				targetObj = clazz.newInstance();
			}
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				// JavaBean的 Field 名
				String fieldName = preName + prop.getName();
				if(fieldName.contains("describe")){
					System.out.println();
				}
				// JavaBean的 Field 类型
				String fieldType = prop.getPropertyType().getSimpleName()
						.toLowerCase();
				// 参数值 ：String类型
				Object paramValue = this.parameterMap.get(fieldName);// request.getParameter(fieldName);
				// 将参数值映射成适当的类型
				Object fieldValue = null;
				if (fieldType.contains("string")) {
					fieldValue = paramValue;
				} else if (fieldType.contains("int")
						|| fieldType.contains("long")) {
					try {
						fieldValue = Integer.valueOf(paramValue.toString());
					} catch (Exception e) {
						fieldValue = 0;
					}
				}// 映射时间类型
				else if (fieldType.contains("date")) {
					SimpleDateFormat sdf = null;
					try {
						sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						fieldValue = sdf.parse(paramValue.toString());
					} catch (Exception e) {
						try {// 如果不符合 yyyy-MM-dd HH:mm:ss 则重新parser字符串
							sdf = new SimpleDateFormat("yyyy-MM-dd");
							fieldValue = sdf.parse(paramValue.toString());
						} catch (Exception e1) {
							try {
								sdf = new SimpleDateFormat("HH:mm:ss");
								fieldValue = sdf.parse(paramValue.toString());
							} catch (Exception e2) {
								fieldValue = null;
							}
						}
					}
				} else if (fieldType.contains("FormFile")) {
					fieldValue = paramValue;
				} else {
					/**
					 * type 自定义对象类型 递归
					 **/
					Class<?> fieldClazz = prop.getPropertyType();
					fieldValue = this.paramsReflect(fieldName, fieldClazz);
				}
				/**
				 * 当 fieldValue == null 的时候。 有可能会将通过IOC方式注入的对象重置为null
				 **/
				if (fieldValue != null) {
					prop.getWriteMethod().invoke(targetObj,
							new Object[] { fieldValue });
				}
			}
			return targetObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析InputStream流
	 * 
	 * @param content
	 *            : 流的内容
	 */
	@Deprecated
	public void parserInputStream(String content) {
		if(content == null || content.trim().equals("")){
			return;
		}
		System.out.println(content);
		String contentType = request.getContentType();
		String spt = "--"+contentType.substring(contentType.indexOf("boundary=")
				+ "boundary=".length());
		String[] items = content.split(spt);
		// 用于文本域的正则
		String textRegex = "name=\"(.+?)\"([\\s\\S]+)";
		Pattern textPat = Pattern.compile(textRegex);
		// 用于file流的正则
		String fileRegex = "name=\"(.+?)\";\\s+filename=\"(.*?)\"[\\r\\n]+?Content-Type:\\s(.+)\\r\\n([\\s\\S]+)";
		Pattern filePat = Pattern.compile(fileRegex);
		// 遍历：解析流
		for (String item : items) {
			if (item == null || "".equals(item.trim())) {
				continue;
			}
			// 解析文本流
			if (item.contains("Content-Type") == false) {
				Matcher textMat = textPat.matcher(item);
				while (textMat.find()) {
					parameterMap.put(textMat.group(1).trim(), textMat.group(2)
							.trim());
				}
			} else {// 文件流
				Matcher fileMat = filePat.matcher(item);
				while (fileMat.find()) {
					String fileType = fileMat.group(3);
					System.out.println(fileType);
					FormFile formFile = new FormFile();
					String fileContent = fileMat.group(4);
					// 判断文件内容是否为空
					if (fileType.trim().equals("application/octet-stream") == false) {
						System.out.println("文件流的内容\n"+item);
						
						formFile.setFileName(fileMat.group(2));
						formFile.setContentType(fileMat.group(3));
						try {
							// System.getProperty("catalina.home")
							File file = new File("c:/");
							if (file.exists() == false) {
								file.mkdirs();
							}
							DataOutputStream bufOut = new DataOutputStream(
									new BufferedOutputStream(new FileOutputStream(file + UUID.randomUUID().toString() + ".jpg")));
							
							bufOut.write(fileContent.getBytes());
							bufOut.close();
							formFile.setFile(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					parameterMap.put(fileMat.group(1), formFile);
				}
			}
		}
	}

}
