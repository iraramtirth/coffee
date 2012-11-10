<%@page import="coffee.cms.admin.action.TigUserAction"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path", path);
	
	String action = request.getParameter("action");
	if(action != null)
	{
		if("insert".equals(action))
		{
			TigUserAction act = new TigUserAction();
			act.insert(request);
			response.sendRedirect("query.jsp");
			return;
		}
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<link href="${path}/res/css/admin.css" rel="stylesheet" type="text/css" />
<link href="${path}/res/css/theme.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 内容管理 - 添加</div>
		<form class="ropt">
			<input type="submit" value="返回列表" onclick="this.form.action='query.jsp';" class="return-button" />
			<input type="hidden" name="cid" value="" />
		</form>
		<div class="clear"></div>
	</div>
	<div class="body-box">
		<form method="post" action="insert.jsp?action=insert">
			<table width="100%" class="pn-ftable" cellpadding="2" cellspacing="1"
				border="0">
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>用户名:
					</td>
					<td colspan="3" class="pn-fcontent">
						<input type="text" maxlength="150" name="userId" class="required" size="70" maxlength="150" />
					</td>
				</tr>
				<tr>
					<td width="20%" class="pn-flabel pn-flabel-h">
						<span class="pn-frequired">*</span>密码:
					</td>
					<td colspan="3" class="pn-fcontent">
						<input type="password" maxlength="150" name="password" class="required" size="70" maxlength="150" />
					</td>
				</tr>
				<tr>
					<td colspan="4" class="pn-fbutton">
					<input type="submit" value="提交" /> &nbsp; 
					<input type="reset" value="重置" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>