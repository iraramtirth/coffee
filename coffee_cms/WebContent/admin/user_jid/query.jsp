<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title></title>

<link href="${path}/res/css/admin.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/css/theme.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.validate.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.treeview.css" rel="stylesheet" type="text/css"/>
<link href="${path}/res/common/css/jquery.ui.css" rel="stylesheet" type="text/css"/>

<script src="${path}/res/common/js/jquery.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.ext.js" type="text/javascript"></script>
<script src="${path}/res/js/admin.js" type="text/javascript"></script>
<script src="${path}/res/common/js/pony.js" type="text/javascript"></script>

<script type="text/javascript">
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
		<input class="add" type="submit" value="添加" onclick="this.form.action='insert.jsp';"/>
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
	<input type="hidden" name="rejectOpinion"/>
<table class="pn-ltable" style="" width="100%" cellspacing="1" cellpadding="0" border="0">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type='checkbox' onclick='Pn.checkbox("ids",this.checked)'/></th>
			<th>ID</th>
			<th>用户JID</th>
			<th>操作选项</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${pager}" var="item">
			<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type='checkbox' name='ids' value='340'/></td>
				<td>${item.it}</td>
				<td>${item.jid}</td>
				<td align="center">		
					<a href="v_view.do?id=340" class="pn-opt">查看</a> | 
					<a href="v_edit.do" class="pn-opt">修改</a> | 		
					<a href="" onclick="if(!confirm('您确定删除吗？')) {return false;}" class="pn-opt">删除</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<c:if test="${!empty pager}">
		 <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
	        <tr>
	          <td height="6"><img src="${path}/resource/img/spacer.gif" width="1" height="1" /></td>
	        </tr>
	        <tr>
	          <td height="33"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="right-font08">
	              <tr>
	                <td width="50%">共 <span class="right-text09"> ${pager.page} </span> 页  (${fn:length(pager.items)}/${pager.count}) </td>
	                <td width="49%" align="right">
	                 <pg:pager scope="request" maxIndexPages="5" index="center"
						    maxPageItems="${pager.size}" url="${localPath}" items="${pager.count}"
						    export="currentPageNumber=pageNumber">
						   
						   <pg:param name="action" value="query"/>
						   <pg:param name="qName" value="${qName}"/>
						   
						    <!-- 以下内容保持不变 -->
						    <pg:first> <a href="${pageUrl}">首页</a> </pg:first>
						    <pg:prev> <a href="${pageUrl}">前页</a>  </pg:prev>
						     <pg:pages>
							     <c:choose>
							  	    <c:when test="${pageNumber eq currentPageNumber }">
							       		<font color="red">${pageNumber }</font>
							      	</c:when>
							      	<c:otherwise>
							       		<a href="${pageUrl }">${pageNumber}</a>
							      	</c:otherwise>
							     </c:choose>
						    </pg:pages>
						    <pg:next> <a href="${pageUrl}">下页</a> </pg:next>
						    <pg:last> <a href="${pageUrl}">尾页</a> </pg:last>
						    <pg:skip pages="<%= 5 %>"> 
						 	 	<a href="${pageUrl}">[ 跳转 #${pageNumber} ]</a> 
							</pg:skip> 
						   </pg:pager>
	                 </td>
	              </tr>
	          </table></td>
	        </tr>
	      </table>
      </c:if>
      
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
</form>
</div>

</body>
</html>