<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<html xmlns="http://www.w3.org/1999/xhtml"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>CoffeeCMS后台管理系统</title>
<link href="${path}/res/admin/admin.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/theme.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript">
	if(top!=this) {
		top.location=this.location;
	}
	$(function() {
		$("#username").focus();
		$("#jvForm").validate();
	});
</script>
<style type="text/css">
body{margin:0;padding:0;font-size:12px;background:url(${path}/res/admin/bg.jpg) top repeat-x;}
.input{width:150px;height:17px;border-top:1px solid #404040;border-left:1px solid #404040;border-right:1px solid #D4D0C8;border-bottom:1px solid #D4D0C8;}
</style>
</head>
<body>
<form id="jvForm" action="http://localhost:8080/jeeadmin/jeecms/login.do" method="post">

<table width="750" border="0" align="center" cellpadding="0" cellspacing="0">
  <tbody>
  <tr>
    <td height="200">&nbsp;</td>
  </tr>
  <tr>
    <td>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tbody><tr>
          <td width="423" height="280" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody><tr>
                <td><img src="${path}/res/admin/ltop.jpg"/></td>
              </tr>
              <tr>
                <td><img src="${path}/res/admin/llogo.jpg"/></td>
              </tr>
            </tbody></table></td>
          <td width="40" align="center" valign="bottom"><img src="${path}/res/admin/line.jpg" width="23" height="232"/></td>
          <td valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tbody><tr>
                <td height="90" align="center" valign="bottom"><img src="${path}/res/admin/ltitle.jpg"/></td>
              </tr>
              <tr>
                <td>
                <table width="100%" border="0" align="center" cellpadding="0" cellspacing="5">
                    <tbody><tr>
                      <td width="91" height="40" align="right"><strong>用户名: </strong></td>
                      <td width="211"><input type="text" id="username" name="username" maxlength="100" class="input"/></td>
                    </tr>
                    <tr>
                      <td height="40" align="right"><strong>密码: </strong></td>
                      <td><input name="password" type="password" id="password" maxlength="32" class="input"/></td>
                    </tr>
                    <tr>
                      <td height="40" colspan="2" align="center">
					    <input type="image" src="${path}/res/admin/login.jpg" name="submit"/>
                        &nbsp; &nbsp; <img name="reg" style="cursor: pointer" src="${path}/res/admin/reset.jpg" onclick="document.forms[0].reset()"/> </td>
                    </tr>
                  </tbody></table></td>
              </tr>
            </tbody></table></td>
        </tr>
      </tbody></table></td>
  </tr>
</tbody></table>
</form>
</body></html>