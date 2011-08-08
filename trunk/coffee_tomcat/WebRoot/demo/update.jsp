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
	<link href="<%=path%>/resource/css/all.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=path%>/resource/js/jquery.js"></script>
	<script type="text/javascript" src="<%=path%>/resource/js/validate.js"></script>
	<script type="text/javascript" src="<%=path%>/demo/check.js"></script>
  </head>
   
  <body class="right_body">
  <form action="<%=path%>/user/update.action" method="post" id="f1">
  <input type="hidden" name="model.id" id="id" value="${model.id}">
  <input type="hidden" name="pager.curpage" id="curpage" value="${pager.curpage}">
  <input type="hidden" id="info" value="true">
<div class="active"><span></span></div>
<!-- 内容开始 -->
 
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table_best">
      <tr>
        <td width="41%" class="left_best" >用户名：</td>
        <td width="59%">
        	<input type="text" name="model.username" id="username" value="${model.username}" class="required" onchange="check_username()">
        	<span id="usernameError"></span>
        </td>
      </tr>
      <tr>
        <td width="41%" class="left_best" >密码：</td>
        <td width="59%">
        	<input type="password" name="model.password" id="password" value="${model.password}" class="required">
        	<span id="passwordError"></span>
        </td>
      </tr>
      <tr style="display:">
        <td width="41%" class="left_best" >年龄：</td>
        <td width="59%"><input type="text" name="model.age" id="age" value="${model.age}" class="digit">
        	<span id="ageError"></span>
        </td>
      </tr>
      <tr>
        <td width="41%" class="left_best" >出生年月：</td>
        <td width="59%"><input type="text" name="model.birthday" id="birthday" value="${model.birthday}" readonly="readonly">
      		<span id="birthdayError"></span>
        </td>
      </tr>
      <tr>
        <td width="41%" class="left_best" >头像：</td>
        <td width="59%"><input type="file" name="model.photo" id="photo">
      		<span id="urlError"></span>
      		
        </td>
      </tr>
      <tr>
      	<td width="41%" class="left_best">简介：</td>
      	<td width="59%">
      		<div id="describeError"></div>
      		<textarea rows="20" cols="80" name="model.describe" id="describe" class="required">${model.describe}</textarea>
      	</td>
      </tr>
      
    </table>
	    <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="0" class="table">
      <tr>
        <td align="center"><label>
          <input type="button" name="Submit" value="提 交" class="button_ntab" onclick="return goSub();" />
          &nbsp;&nbsp;<input type="button" name="back" value="返 回" class="button_ntab" onclick="javascript:history.go(-1)">
        </label></td>
        <td width="1%" align="right">&nbsp;</td>
      </tr>
    </table>
</form>
</body>
</html>
