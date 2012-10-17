<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CoffeeCMS-left</title>

<link href="${path}/res/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/css/theme.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.validate.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.treeview.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.ui.css" rel="stylesheet" type="text/css"/>

<script src="${path}/res/common/js/jquery.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.ext.js" type="text/javascript"></script>
<script src="${path}/res/js/admin.js" type="text/javascript"></script>
<!-- 
	注意该js有bug 
	不能写成<script src="${path}/res/common/js/pony.js" type="text/javascript"/>
	否则， 解析该js文件的时候报错， 则不会继续往下解析其他js文件/或者js脚本
 -->
<script src="${path}/res/common/js/pony.js" type="text/javascript"></script>

<script type="text/javascript">

	function menuClick(id)
	{
		var menuItem = $("#"+id + " ul");
		var obj = menuItem.eq(1);
		//如果： == none 或者  == undefined 
		if(obj.css("display") != 'block')
		{
			obj.css("display","block");
		}
		else
		{
			obj.css("display","none");
		}
	}
</script>

</head>
<body class="lbody">
	
	<!-- 左侧的时间和菜单操作  -->
	<div class="left">
		<div class="date">
			<span>今天： <script language="javascript">
				var day = "";
				var month = "";
				var ampm = "";
				var ampmhour = "";
				var myweekday = "";
				var year = "";
				mydate = new Date();
				myweekday = mydate.getDay();
				mymonth = mydate.getMonth() + 1;
				myday = mydate.getDate();
				year = mydate.getFullYear();
				if (myweekday == 0)
					weekday = " 星期日 ";
				else if (myweekday == 1)
					weekday = " 星期一 ";
				else if (myweekday == 2)
					weekday = " 星期二 ";
				else if (myweekday == 3)
					weekday = " 星期三 ";
				else if (myweekday == 4)
					weekday = " 星期四 ";
				else if (myweekday == 5)
					weekday = " 星期五 ";
				else if (myweekday == 6)
					weekday = " 星期六 ";
				document.write(year + "年" + mymonth + "月" + myday + "日 "
						+ weekday);
			</script>
			</span>
		</div>
		<div class="fresh">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tbody>
					<tr>
						<td height="35" align="center"><img
							src="${path}/res/img/admin/refresh-icon.png">&nbsp;&nbsp;<a
							href="javascript:location.href=location.href">刷新</a></td>
						<td width="2" height="35" valign="middle"><img
							src="${path}/res/img/admin/left-line.png"></td>
						<td height="35" align="center"><img
							src="${path}/res/img/admin/model-icon.png">&nbsp;&nbsp;<a
							href="../model/v_list.do" target="rightFrame">模型管理</a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	
	<ul id="browser" class="filetree treeview">
		<li id="" class="open collapsable lastCollapsable">
			<div class="hitarea open-hitarea collapsable-hitarea lastCollapsable-hitarea "></div>
			<span class="folder"><a href="v_list.do" target="rightFrame">根目录</a></span>
			
			<!-- 菜单的顶级 ul -->
			<ul>
				<li id="46" class="hasChildren expandable">
					<!-- 菜单前面的加号 -->
					<div onclick="javascript:menuClick('46');" class="hitarea hasChildren-hitarea expandable-hitarea"></div>
					<span class="folder">
						<a href="v_edit.do?id="  target="rightFrame">视频 [<span style="color: red">视频</span>]</a>
					</span>
					<ul style="display: none;">
						<li id="placeholder" class="last"><span>placeholder</span></li>
					</ul>
					<ul style="display: block; ">
						<li id="49"><span class="file"><a href="v_edit.do?id=49" target="rightFrame">明星糗镜头 [<span style="color:red">视频</span>]</a></span></li>
						<li id="50"><span class="file"><a href="v_edit.do?id=50" target="rightFrame">有才恶搞 [<span style="color:red">视频</span>]</a></span></li>
						<li id="51"><span class="file"><a href="v_edit.do?id=51" target="rightFrame">经典搞笑专辑 [<span style="color:red">视频</span>]</a></span></li>
						<li id="52" class="last"><span class="file"><a href="v_edit.do?id=52" target="rightFrame">哈哈趣闻 [<span style="color:red">视频</span>]</a></span></li>
					</ul>
				</li>
				 
				<li id="10" class="last">
					<span class="file">
						<a href="v_edit.do?id=10" target="rightFrame" class="">关于我们 [<span style="color: red">单页</span>]</a>
					</span>
				</li>
			</ul>
		</li>
	</ul>

</body>
</html>