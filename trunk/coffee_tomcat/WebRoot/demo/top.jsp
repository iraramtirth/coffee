<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>网站抓取管理系统</title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<link href="<%=path%>/resource/css/all.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript">
function timenow(){ 	
   var time= new Date(); 
   hours= time.getHours(); 
   mins= time.getMinutes(); 
   secs= time.getSeconds(); 
   mons= (time.getMonth()+1); 
   years= time.getYear(); 
   days= time.getDate(); 
   week=time.getDay(); 
   switch (week) { 
    case 0: week="星期日"; break; 
    case 1: week="星期一"; break; 
    case 2: week="星期二"; break; 
    case 3: week="星期三"; break; 
    case 4: week="星期四"; break; 
    case 5: week="星期五"; break; 
    case 6: week="星期六"; break; 
   } 
    if(hours<10)
    hours="0"+hours;
    if(mins<10)
    mins="0"+mins;
    if(secs<10)
    secs="0"+secs;
    document.getElementById("timenow").innerHTML=years+"年"+mons+"月"+days+"日  "+week;
    setTimeout("timenow()",500); //毫秒 
}

</script>
  </head>

<body class="top_body" onload="timenow();">
<div class="top">
  <div class="logo"><img src="<%=path%>/resource/img/logo.gif"></div>
  <div class="uers_text">系统管理员：<b></b><span><br />
    <a href="king/index.jsp" target="_blank">前台首页</a>|<a href="adminpub.do?at=logout" target="_top">退出</a></span></div>
</div>
<div class="nav">
<div class="nav_time" id="timenow"></div>
<div class="nav_nav"><a href="<%=path%>/user/list.action" target="mainFrame">演示系统</a></div>
</div>
</body>
</html>
