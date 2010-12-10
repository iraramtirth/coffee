<%@ page contentType="text/html; charset=UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//if(session.getAttribute("uname")==null){
 // response.sendRedirect("/Kingnet/admin/login.jsp");
//}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>业务人员-信息添加</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="<%=path%>/resource/css/all.css" rel="stylesheet" type="text/css">
	
  </head>
  
  <body class="right_body">
  <form action="./websiteConfig/update.action" method="post" id="f1">
  
  
<div class="active"><span></span></div>
<!-- 内容开始 -->
  <div id="myTab1_Content1" class="none right_nav">
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table_best">
      <tr>
        <td width="41%" class="left_best" >网站名称：</td>
        <td width="59%"><input type="text" name="model.username" id="username" value="${model.username}" readonly="readonly">
        	<span id="usernameError"></span>
        </td>
      </tr>
      <tr>
        <td width="41%" class="left_best" >网站域名：</td>
        <td width="59%"><input type="text" name="model.domain" id="domain" value="${model.domain}" readonly="readonly">
        	<span id="domainError"></span>
        </td>
      </tr>
      <tr>
        <td width="41%" class="left_best" >网站编号：</td>
        <td width="59%"><input type="text" name="model.webid" id="webid" value="${model.webid}" readonly="readonly">
        	<span id="webidError"></span>
        </td>
      </tr>
      <tr>
        <td width="41%" class="left_best" >列表页入口：</td>
        <td width="59%"><input type="text" name="model.url" id="url" size="80" value="${model.url}" readonly="readonly">
      		<span id="urlError"></span>
        </td>
      </tr>
      <tr>
      	<td width="41%" class="left_best">网站说明：</td>
      	<td width="59%">
      		<div id="describeError"></div>
      		<textarea rows="20" cols="80" name="model.describe" id="describe" readonly="readonly">${model.describe}</textarea>
      	</td>
      </tr>
    </table>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="0" class="table">
      <tr>
        <td align="center"><label>
          &nbsp;&nbsp;<input type="button" name="back" value="关闭" class="button_ntab" onclick="window.close();">
        </label></td>
        <td width="1%" align="right">&nbsp;</td>
      </tr>
    </table>
</div>
</form>
</body>
</html>
