package coffee.cms.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import coffee.cms.core.bean.MenuItemBean;
import coffee.cms.core.service.MenuItemService;

/**
 * Servlet Filter implementation class CMSFilter
 */
@WebFilter(urlPatterns = "/*")
public class CMSFilter implements Filter {

	public CMSFilter() {

	}

	public void destroy() {
		System.out.println("destroy");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
		System.out.println("doFilter");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("init");

		MenuItemService menuService = new MenuItemService();

		List<MenuItemBean> menuItems = menuService.getMenuItems();

		fConfig.getServletContext().setAttribute("menus", menuItems);
	}

}
