<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>
<link href="/res/jeecms/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="/res/common/css/theme.css" rel="stylesheet" type="text/css"/>
<link href="/res/common/css/jquery.validate.css" rel="stylesheet" type="text/css"/>
<link href="/res/common/css/jquery.treeview.css" rel="stylesheet" type="text/css"/>
<link href="/res/common/css/jquery.ui.css" rel="stylesheet" type="text/css"/>

<script src="/thirdparty/fckeditor/fckeditor.js" type="text/javascript"></script>
<script src="/thirdparty/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="/res/common/js/jquery.js" type="text/javascript"></script>
<script src="/res/common/js/jquery.ext.js" type="text/javascript"></script>
<script src="/res/common/js/pony.js" type="text/javascript"></script>
<script src="/res/jeecms/js/admin.js" type="text/javascript"></script><script type="text/javascript">
$(function() {

	$("#rejectDialog").dialog({
		autoOpen: false,
		modal: true,
		width: 380,
		height: 200,
		position: ["center",50],
		buttons: {
			"OK": function() {
				rejectSubmit();
			}
		}
	});
});

function getTableForm() {

	return document.getElementById('tableForm');
}

function optDelete() {

	if(Pn.checkedCount('ids')<=0) {
		alert("请选择您要操作的数据");
		return;
	}

	if(!confirm("您确定删除吗？")) {
		return;
	}

	var f = getTableForm();
	f.action="o_delete.do";
	f.submit();
}

function optCheck() {

	if(Pn.checkedCount('ids')<=0) {
		alert("请选择您要操作的数据");
		return;
	}

	var f = getTableForm();
	f.action="o_check.do";
	f.submit();
}

function optStatic() {

	if(Pn.checkedCount('ids')<=0) {
		alert("请选择您要操作的数据");
		return;
	}

	var f = getTableForm();
	f.action="o_static.do";
	f.submit();
}

function optReject() {

	if(Pn.checkedCount('ids')<=0) {
		alert("请选择您要操作的数据");
		return;
	}
	$("#rejectDialog").dialog("open");
}

function rejectSubmit() {

	$("input[name=rejectOpinion]").val($("#rejectOpinion").val());
	$("input[name=rejectStep]").val($("#rejectStep").val());
	$("#rejectDialog").dialog("close");
	var f = getTableForm();
	f.action="o_reject.do";
	f.submit();
}

function chgStatus() {
	var queryStatus = $("input[name=queryStatus]:checked").val();
	location.href="v_list.do?cid=&queryStatus=" + queryStatus;
}

</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 内容管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="submit" value="添加" onclick="this.form.action='v_add.do';"/>
		<input type="hidden" name="cid" value=""/>
	</form>
	<div class="clear"></div>
</div>

<div class="body-box">
<form action="v_list.do" method="post" style="padding-top:5px;">
<div>
标题: <input type="text" name="queryTitle" value="" style="width:100px"/>

发布者: <input type="text" name="queryInputUsername" value="" style="width:70px"/>

<label><input type="checkbox" name="queryTopLevel" value="true"/>固顶</label>

<label><input type="checkbox" name="queryRecommend" value="true"/>推荐</label>

<select name="queryTypeId"><option value="" selected="selected">--所有类型--</option><option value="1">普通</option><option value="2">图文</option><option value="3">焦点</option><option value="4">头条</option></select>

<select name="queryOrderBy"><option value="0" selected="selected">ID降序</option><option value="1">ID升序</option><option value="2">发布时间降</option><option value="3">发布时间升</option><option value="4">固顶降,发布降</option><option value="5">固顶降,发布升</option><option value="6">日点击降</option><option value="7">周点击降</option><option value="8">月点击降</option><option value="9">总点击降</option><option value="10">日评论降</option><option value="11">周评论降</option><option value="12">月评论降</option><option value="13">总评论降</option><option value="14">日下载降</option><option value="15">周下载降</option><option value="16">月下载降</option><option value="17">总下载降</option><option value="18">日顶降</option><option value="19">周顶降</option><option value="20">月顶降</option><option value="21">总顶降</option></select>

<input type="hidden" name="cid" value=""/>

<input class="query" type="submit" value="查询"/>

</div>

<div style="padding-top:5px">

<label><input type="radio" name="queryStatus" value="all" onclick="chgStatus();" checked="checked"/>所有内容</label>

<label><input type="radio" name="queryStatus" value="draft" onclick="chgStatus();"/>草稿</label>

<label><input type="radio" name="queryStatus" value="prepared" onclick="chgStatus();"/>待审</label>

<label><input type="radio" name="queryStatus" value="passed" onclick="chgStatus();"/>已审</label>

<label><input type="radio" name="queryStatus" value="checked" onclick="chgStatus();"/>终审</label>

<label><input type="radio" name="queryStatus" value="rejected" onclick="chgStatus();"/>退回</label>

</div>

</form>

<form id="tableForm" method="post">

<input type="hidden" name="pageNo" value=""/>

<input type="hidden" name="cid" value=""/>

<input type="hidden" name="queryOrderBy" value="0"/><input type="hidden" name="queryTopLevel" value="false"/><input type="hidden" name="queryRecommend" value="false"/><input type="hidden" name="rejectStep"/>

<input type="hidden" name="rejectOpinion"/>



<table class="pn-ltable" style="" width="100%" cellspacing="1" cellpadding="0" border="0">

<thead class="pn-lthead"><tr>

	<th width="20"><input type='checkbox' onclick='Pn.checkbox("ids",this.checked)'/></th>

	<th>ID</th>

	<th>标题</th>

	<th>类型</th>

	<th>发布者</th>

	<th>点击</th>

	<th>发布时间</th>

	<th>状态</th>

	<th>静态页</th>

	<th>操作选项</th></tr></thead>

<tbody  class="pn-ltbody"><tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='340'/></td>

	<td>340</td>

	<td>		

		

		<strong>[国内新闻]</strong>

		<a href="/gnxw/340.jhtml" target="_blank">1212</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">0</td>

	<td align="center">2011-12-19</td>

	<td align="center">审核中		 0

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=340&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=340&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=340&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='339'/></td>

	<td>339</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[文娱体育]</strong>

		<a href="/wyty/339.jhtml" target="_blank">周渝民小小彬温情携手《新天生一对》</a>

</td>

	<td align="center">焦点</td>

	<td align="center">admin</td>

	<td align="right">6</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=339&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=339&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=339&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='338'/></td>

	<td>338</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[文娱体育]</strong>

		<a href="/wyty/338.jhtml" target="_blank">海豚的故事-先行版预告片</a>

</td>

	<td align="center">焦点</td>

	<td align="center">admin</td>

	<td align="right">5</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=338&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=338&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=338&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='337'/></td>

	<td>337</td>

	<td>		

		

		<strong>[实用助手]</strong>

		<a href="/syzs/337.jhtml" target="_blank">多玩YY</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">2</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=337&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=337&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=337&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='336'/></td>

	<td>336</td>

	<td>		

		

		<strong>[实用助手]</strong>

		<a href="/syzs/336.jhtml" target="_blank">迅雷7</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">1</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=336&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=336&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=336&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='335'/></td>

	<td>335</td>

	<td>		

		

		<strong>[实用助手]</strong>

		<a href="/syzs/335.jhtml" target="_blank">有道词典</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">3</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=335&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=335&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=335&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='334'/></td>

	<td>334</td>

	<td>		

		

		<strong>[实用助手]</strong>

		<a href="/syzs/334.jhtml" target="_blank">搜狗拼音输入</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">1</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=334&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=334&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=334&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='333'/></td>

	<td>333</td>

	<td>		

		

		<strong>[系统软件]</strong>

		<a href="/system/333.jhtml" target="_blank">酷狗音乐</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">2</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=333&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=333&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=333&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='332'/></td>

	<td>332</td>

	<td>		

		

		<strong>[媒体工具]</strong>

		<a href="/media/332.jhtml" target="_blank">风行网络电影</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">10</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=332&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=332&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=332&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='331'/></td>

	<td>331</td>

	<td>		

		

		<strong>[媒体工具]</strong>

		<a href="/media/331.jhtml" target="_blank">腾讯QQ </a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">5</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=331&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=331&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=331&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='330'/></td>

	<td>330</td>

	<td>		

		

		<strong>[网络游戏]</strong>

		<a href="/network/330.jhtml" target="_blank">永恒之塔</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">7</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=330&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=330&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=330&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='329'/></td>

	<td>329</td>

	<td>		

		

		<strong>[网络游戏]</strong>

		<a href="/network/329.jhtml" target="_blank">神魔大陆</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">6</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=329&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=329&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=329&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='328'/></td>

	<td>328</td>

	<td>		

		

		<strong>[网络游戏]</strong>

		<a href="/network/328.jhtml" target="_blank">天下3</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">1</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=328&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=328&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=328&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='327'/></td>

	<td>327</td>

	<td>		

		

		<strong>[冬雪系列]</strong>

		<a href="/dong/327.jhtml" target="_blank">足球宝贝徐冬冬海边写真 湿身展无限魅力</a>

</td>

	<td align="center">图文</td>

	<td align="center">admin</td>

	<td align="right">8</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=327&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=327&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=327&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='326'/></td>

	<td>326</td>

	<td>		

		

		<strong>[网络游戏]</strong>

		<a href="/network/326.jhtml" target="_blank">魔兽世界</a>

</td>

	<td align="center">普通</td>

	<td align="center">admin</td>

	<td align="right">13</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=326&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=326&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=326&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='325'/></td>

	<td>325</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[蓝天白云绿地]</strong>

		<a href="/tiankong/325.jhtml" target="_blank">AC米兰球星出席圣诞晚会，帅哥美女云集</a>

</td>

	<td align="center">图文</td>

	<td align="center">admin</td>

	<td align="right">10</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=325&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=325&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=325&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='324'/></td>

	<td>324</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[蓝天白云绿地]</strong>

		<a href="/tiankong/324.jhtml" target="_blank">爨底下村私家写真</a>

</td>

	<td align="center">图文</td>

	<td align="center">admin</td>

	<td align="right">10</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=324&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=324&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=324&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='323'/></td>

	<td>323</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[蓝天白云绿地]</strong>

		<a href="/tiankong/323.jhtml" target="_blank">柳岩红黑诱惑大片写真</a>

</td>

	<td align="center">图文</td>

	<td align="center">admin</td>

	<td align="right">15</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=323&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=323&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=323&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='322'/></td>

	<td>322</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[蓝天白云绿地]</strong>

		<a href="/tiankong/322.jhtml" target="_blank">美国旧金山圣诞老人大聚会</a>

</td>

	<td align="center">图文</td>

	<td align="center">admin</td>

	<td align="right">3</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=322&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=322&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=322&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr>

<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">

	<td><input type='checkbox' name='ids' value='321'/></td>

	<td>321</td>

	<td>		

		<span style="color:red">[推]</span>

		<strong>[蓝天白云绿地]</strong>

		<a href="/tiankong/321.jhtml" target="_blank">童星今昔对比照大曝光</a>

</td>

	<td align="center">图文</td>

	<td align="center">admin</td>

	<td align="right">2</td>

	<td align="center">2011-12-19</td>

	<td align="center">已终审		

</td>

	<td align="center">		 <span style="color:red">需要生成</span>

</td>

	<td align="center">		<a href="v_view.do?id=321&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">查看</a> | 		<a href="v_edit.do?id=321&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" class="pn-opt">修改</a> | 		<a href="o_delete.do?ids=321&cid=&pageNo=&queryTitle=&queryInputUsername=&queryTopLevel=false&queryRecommend=false&queryTypeId=&queryStatus=&queryOrderBy=0" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>

		</td>

</tr></tbody>

</table>

<table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td align="center" class="pn-sp">

	共 189 条&nbsp;

	每页<input type="text" value="20" style="width:30px" onfocus="this.select();" onblur="$.cookie('_cookie_page_size',this.value,{expires:3650});" onkeypress="if(event.keyCode==13){$(this).blur();return false;}"/>条&nbsp;

	<input class="first-page" type="button" value="首 页" onclick="_gotoPage('1');" disabled="disabled"/>

	<input class="pre-page" type="button" value="上一页" onclick="_gotoPage('1');" disabled="disabled"/>

	<input class="next-page" type="button" value="下一页" onclick="_gotoPage('2');"/>

	<input class="last-page" type="button" value="尾 页" onclick="_gotoPage('10');"/>&nbsp;

	当前 1/10 页 &nbsp;转到第<input type="text" id="_goPs" style="width:50px" onfocus="this.select();" onkeypress="if(event.keyCode==13){$('#_goPage').click();return false;}"/>页

	<input class="go" id="_goPage" type="button" value="转" onclick="_gotoPage($('#_goPs').val());"/>

</td></tr></table>

<script type="text/javascript">

function _gotoPage(pageNo) {

	try{

		var tableForm = getTableForm();

		$("input[name=pageNo]").val(pageNo);

		tableForm.action="v_list.do";

		tableForm.onsubmit=null;

		tableForm.submit();

	} catch(e) {

		alert('_gotoPage(pageNo)方法出错');

	}

}

</script>

<div style="margin-top:15px;">

	<input type="button" value="删除" onclick="optDelete();" class="del-button"/>

	<input type="button" value="审核" onclick="optCheck();" class="check"/>

	<input id="rejectButton" type="button" value="退回" onclick="optReject();" class="reject"/>

	<input type="button" value="生成静态页" onclick="optStatic();" class="generate-static"/>

</div>

</form>

</div>

<div id="rejectDialog" style="display:none" title="退回原因">

<p>退回原因: <input type="text" id="rejectOpinion" style="width:260px" onkeypress="if(event.keyCode==13){rejectSubmit();return false;}"/></p>

<p>退回级数:

	<select id="rejectStep">

		<option value="">上一级</option>

		<option value="1">1</option><option value="0">0</option>

	</select>

</p>

</div>

</body>

</html>