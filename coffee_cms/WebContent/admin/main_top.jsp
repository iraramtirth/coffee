<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>JEECMS Administrator's Control Panel - powered by jeecms</title>
<link href="${path}/res/admin/admin.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/theme.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.validate.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.treeview.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.ui.css" rel="stylesheet" type="text/css"/>

<script src="/thirdparty/fckeditor/fckeditor.js" type="text/javascript"></script>
<script src="/thirdparty/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.ext.js" type="text/javascript"></script>
<script src="${path}/res/common/js/pony.js" type="text/javascript"></script>
<script src="${path}/res/admin/admin.js" type="text/javascript"></script><style type="text/css">        
*{margin:0;padding:0}
a:focus {outline:none;}
html{height:100%;overflow:hidden;}
body{height:100%;}
#top{ background-color:#1d63c6; height:69px; width:100%;}
.logo{width:215px; height:69px;}
.topbg{background:url(${path}/res/admin/top-tbg.png) no-repeat; height:38px;}
.login-welcome{padding-left:20px; color:#fff; font-size:12px;background:url(${path}/res/jeecms/img/admin/topbg.gif) no-repeat;}
.login-welcome a:link,.login-welcome a:visited{color:#fff; text-decoration:none;}

#welcome {color: #FFFFFF;padding: 0 30px 0 5px;}
#logout {color: #FFFFFF; padding-left: 5px;}

.nav{height:31px; overflow:hidden;}
.nav-menu{background:url(${path}/res/admin/bg.png) repeat-x; height:31px; list-style:none; padding-left:20px; font-size:14px;}
.nav .current {background: url(${path}/res/admin/navcurrbg.gif) no-repeat 0px 2px; color:#fff; width:72px; text-align:center;} 
.nav .current a{color:#fff;}
.nav-menu li {height:31px;text-align:center; line-height:31px; float:left; }
.nav-menu li a{color:#2b2b2b; font-weight:bold;}
.nav-menu li.sep{background: url(${path}/res/admin/step.png) no-repeat; width:2px; height:31px; margin:0px 5px;}
.nav .normal{width:72px; text-align:center;}
.top-bottom{width:100%; background: url(${path}/res/admin/bg.png) repeat-x 0px -34px; height:3px;}
.undis{display:none;}
.dis{display:block;}
</style>

<script type="text/javascript">
function g(o){
	return document.getElementById(o);
}
function HoverLi(m,n,counter){
	for(var i=1;i<=counter;i++){
		g('tb_'+m+i).className='normal';
	}
	g('tb_'+m+n).className='current';
}
function countUnreadMsg(){
	 $.post("message/v_countUnreadMsg.do", {
		}, function(data) {
			if(data.result){
				 $("#countDiv").html(""+data.count+"");
			}else{
				alert("请先登录");
			}
		}, "json");
	 setTimeout( "countUnreadMsg() ",1000*60*10);    
}
$(function(){
	$('a').bind("focus", function(){   
	    $(this).blur();   
	}); 
	countUnreadMsg();
});
setTimeout( "countUnreadMsg() ",1000*60*10); 
function mapDialog(){
	var result=window.showModalDialog("map.do","","dialogHeight:600px;dialogWidth:800px;center:yes;resizable: yes;");
	if(result!=null){
		var href=result.split(";")[0];
		var target=result.split(";")[1];
		if(target=="rightFrame"){
				window.parent.mainFrame.document.getElementById(target).src=href;
				var rightFrameSrc=window.parent.mainFrame.document.getElementById(target).src;
				 if(rightFrameSrc.indexOf("frame")>=0){
					window.parent.mainFrame.document.getElementById(target).src=rightFrameSrc.split("frame/")[0]+rightFrameSrc.split("frame/")[1];
					}
			}
		else {
			 	window.parent.document.getElementById(target).src=href;
			}
		}
}
</script>
<script> 
	
</script> 
</head>

<body>
<div id="top">
     <div class="top">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="215"><div class="logo"><img src="${path}/res/admin/logo.png" width="215" height="69" /></div></td>
            <td valign="top">
                <div class="topbg">
                     <div class="login-welcome">
                             <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <tr>
                                    <td width="420" height="38">
                                    <img src="${path}/res/admin/welconlogin-icon.png"/><span id="welcome">您好, admin</span>
                                    <img src="${path}/res/admin/loginout-icon.png"/><a href="logout.do?returnUrl=index.do" target="_top" id="logout" onclick="return confirm('您确定退出吗？');">退出</a>　　
                                    <img src="${path}/res/admin/message-unread.png"/>&nbsp;<a href="message/v_list.do" target="rightFrame">您有<span id="countDiv"></span>条信息未读</a>
                                    </td>
                                    <td align="right">
                                         <form action="index.do" target="_top" method="get">
											<select name="_site_id_param" onchange="this.form.submit();">
								              <option value="1" selected="selected">JEECMS开发站</option>
								            </select>
							            </form>
                                    </td>
                                    <td width="180">
                                   &nbsp; <a href="#" onclick="mapDialog()">【网站地图】</a>
                                    &nbsp;<a id="view_index" href="/" target="_blank">【查看首页】</a>
                                    <!--
                                    &nbsp;<a style="color:#FFF" href="javascript:alert($(document).width()+','+$(document).height())">【窗口大小】</a>
                                    -->
                                    </td>
                                  </tr>
                                </table>
                       </div>  
                     <div class="nav">
                     	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td style="background-image:url('/res/jeecms/img/admin/nav-left.png')" width="14" height="31"></td>
                                <td>
                                	<ul class="nav-menu">
                                    	<li class="current" id="tb_11" onclick="HoverLi(1,1,10);"><a href="main.do" target="mainFrame">首页</a></li>
										<li class="sep"></li><li class="normal" id="tb_12" onclick="HoverLi(1,2,10);"><a href="frame/channel_main.do" target="mainFrame">栏目</a></li>
										<li class="sep"></li><li class="normal" id="tb_13" onclick="HoverLi(1,3,10);"><a href="frame/content_main.do" target="mainFrame">内容</a></li>
										<li class="sep"></li><li class="normal" id="tb_14" onclick="HoverLi(1,4,10);"><a href="frame/template_main.do" target="mainFrame">模板</a></li>
										<li class="sep"></li><li class="normal" id="tb_15" onclick="HoverLi(1,5,10);"><a href="frame/resource_main.do" target="mainFrame">资源</a></li>
										<li class="sep"></li><li class="normal" id="tb_16" onclick="HoverLi(1,6,10);"><a href="frame/assistant_main.do" target="mainFrame">辅助</a></li>
										<li class="sep"></li><li class="normal" id="tb_17" onclick="HoverLi(1,7,10);"><a href="frame/maintain_main.do" target="mainFrame">维护</a></li>
										<li class="sep"></li><li class="normal" id="tb_18" onclick="HoverLi(1,8,10);"><a href="frame/generate_main.do" target="mainFrame">生成</a></li>
										<li class="sep"></li><li class="normal" id="tb_19" onclick="HoverLi(1,9,10);"><a href="frame/user_main.do" target="mainFrame">用户</a></li>
										<li class="sep"></li><li class="normal" id="tb_110" onclick="HoverLi(1,10,10);"><a href="frame/config_main.do" target="mainFrame">配置</a></li>
										<!-- 
										<li class="sep"></li><li class="normal" id="tb_111" onclick="HoverLi(1,11,11);"><a href="frame/statistic_main.do" target="mainFrame">统计</a></li>
										 -->
                                    </ul>
                                </td>
                              </tr>
                            </table>
                     </div>  
                </div>
          </tr>
        </table>
     </div>
</div>
<div class="top-bottom"></div>
</body>
</html>