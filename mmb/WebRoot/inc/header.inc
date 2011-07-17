<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
		<title>买卖宝</title>
		<link href="${path}/css/style.css" rel="stylesheet" type="text/css" />
		<link href="${path}/css/index.css" rel="stylesheet" type="text/css" />
		<script src="${path}/js/jquery.js"></script>
		<script src="${path}/js/header.js"></script>
	</head>
	<body>
		<div class="body" style="width: 900px; overflow: hidden;">
			<!--头部信息-->
			<div class="top">
				<div class="top_t">
					<div class="top_tl">
						<a href="#"><img src="images/top_03.jpg" alt="买卖宝" />
						</a>
					</div>
					<div class="top_tc">
						<img src="images/top_05.jpg" alt="生活" />
					</div>
					<div class="top_tr">
						<form>
							<input class="go1" type="text" value="搜索" />
							<input class="go" type="submit" value=" " />
						</form>
					</div>
				</div>
				<div class="nav">
					<div id="nav1">
						<ul>
							<li class="wang">
								<a onclick="setNavClass(0)" href="${path}/index.jsp">首页</a>
							</li>
							<li>
								<a onclick="setNavClass(1)" href="#">公司介绍</a>
							</li>
							<li>
								<a onclick="setNavClass(2)" href="${path}/news.jsp">新闻中心</a>
							</li>
							<li>
								<a onclick="setNavClass(3)" href="#">联系我们</a>
							</li>
							<li class="wang1" id="nbodr">
								<a onclick="setNavClass(4)" href="${path}/zhaopin.jsp">人才招聘</a>
							</li>
						</ul>
					</div>
					<div class="navr"></div>
				</div>
			</div>
