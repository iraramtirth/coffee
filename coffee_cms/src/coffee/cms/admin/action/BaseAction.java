package coffee.cms.admin.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BaseAction extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL().toString();
		if (url.endsWith("query")) {
			this.query(request);
		} else if (url.endsWith("insert")) {
			this.insert(request);
		} else if (url.endsWith("update")) {
			this.update(request);
		} else if (url.endsWith("delete")) {
			this.delete(request);
		}
		String forward = url.substring(url.lastIndexOf("/") + 1) + ".jsp";
		response.sendRedirect(forward);
	}

	protected void query(HttpServletRequest request) {

	}

	protected void insert(HttpServletRequest request) {

	}

	protected void update(HttpServletRequest request) {

	}

	protected void delete(HttpServletRequest request) {

	}
}
