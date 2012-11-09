<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>Coffee CMS</title>
<link href="${path}/res/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/css/theme.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.validate.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.treeview.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.ui.css" rel="stylesheet" type="text/css"/>

<script src="/thirdparty/fckeditor/fckeditor.js" type="text/javascript"></script>
<script src="/thirdparty/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.ext.js" type="text/javascript"></script>
<script src="${path}/res/common/js/pony.js" type="text/javascript"></script>
<script src="${path}/res/js/admin.js" type="text/javascript"></script>
<style type="text/css">        
*{margin:0;padding:0}
a:focus {outline:none;}
html{height:100%;overflow:hidden;}
body{height:100%;}
#top{ background-color:#1d63c6; height:69px; width:100%;}
.logo{width:215px; height:69px;}
.topbg{background:url(${path}/res/img/admin/top-tbg.png) no-repeat; height:38px;}
.login-welcome{padding-left:20px; color:#fff; font-size:12px;background:url(${path}/res/img/admin/topbg.gif) no-repeat;}
.login-welcome a:link,.login-welcome a:visited{color:#fff; text-decoration:none;}

#welcome {color: #FFFFFF;padding: 0 30px 0 5px;}
#logout {color: #FFFFFF; padding-left: 5px;}

.nav{height:31px; overflow:hidden;}
.nav-menu{background:url(${path}/res/img/admin/bg.png) repeat-x; height:31px; list-style:none; padding-left:20px; font-size:14px;}
.nav .current {background: url(${path}/res/img/admin/navcurrbg.gif) no-repeat 0px 2px; color:#fff; width:72px; text-align:center;} 
.nav .current a{color:#fff;}
.nav-menu li {height:31px;text-align:center; line-height:31px; float:left; }
.nav-menu li a{color:#2b2b2b; font-weight:bold;}
.nav-menu li.sep{background: url(${path}/res/img/admin/step.png) no-repeat; width:2px; height:31px; margin:0px 5px;}
.nav .normal{width:72px; text-align:center;}
.top-bottom{width:100%; background: url(${path}/res/img/admin/bg.png) repeat-x 0px -34px; height:3px;}
.undis{display:none;}
.dis{display:block;}
</style>

<script type="text/javascript">
function g(o){
	return document.getElementById(o);
}
function hoverLi(m,counter){
	for(var i=1;i<=counter;i++){
		g('tb_'+i).className='normal';
	}
	g('tb_'+m).className='current';
}
setTimeout( "countUnreadMsg() ",1000*60*10); 
</script>
</head>
<body>
<div id="top">
<div class="top">
 	<!-- 共一行两列 -->
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
       <!-- 第一列  -->
       <td width="215"><div class="logo"><img src="${path}/res/img/admin/logo.png" width="215" height="69" /></div></td>
        <!--** 第二列  **  -->
       <td valign="top">
          <div class="topbg">
             <div class="login-welcome">
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                       <tr>
                         <td width="420" height="38">
                         <img src="${path}/res/img/admin/welconlogin-icon.png"/><span id="welcome">您好, admin</span>
                         <img src="${path}/res/img/admin/loginout-icon.png"/><a href="logout.do?returnUrl=index.do" target="_top" id="logout" onclick="return confirm('您确定退出吗？');">退出</a>　　
                         </td>
                         <td align="right">
                         	<a id="view_index" href="/" target="_blank">【查看首页】</a> &nbsp;
                         </td>
                       </tr>
                   </table>
             </div>  
             <div class="nav">
             	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td style="background-image:url('${path}/res/img/admin/nav-left.png')" width="14" height="31"></td>
                        <td>
                        	<ul class="nav-menu">
                            	<li class="current" id="tb_1" onclick="hoverLi(1,5);">
                            		<a href="${path}/admin/main.jsp" target="mainFrame">首页</a>
                            	</li>
								<li class="sep"></li><li class="normal" id="tb_2" onclick="hoverLi(2,5);">
									<a href="frame/channel_main.do" target="mainFrame">栏目</a>
								</li>
								<li class="sep"></li><li class="normal" id="tb_3" onclick="hoverLi(3,5);">
									<a href="frame/content_main.do" target="mainFrame">内容</a>
								</li>
								<li class="sep"></li><li class="normal" id="tb_4" onclick="hoverLi(4,5);">
									<a href="frame/template_main.do" target="mainFrame">模板</a>
								</li>
								<li class="sep"></li><li class="normal" id="tb_5" onclick="hoverLi(5,5);">
									<a href="frame/resource_main.do" target="mainFrame">资源</a>
								</li>
                            </ul>
                        </td>
                      </tr>
                    </table>
              </div>  
            </div>
        </td>
     </tr>
   </table>
</div>
</div>
<div class="top-bottom"></div>
</body>
</html>