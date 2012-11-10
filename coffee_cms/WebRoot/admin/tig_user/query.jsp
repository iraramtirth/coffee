<%@page import="coffee.cms.admin.action.TigUserAction"%>
<%@page import="coffee.cms.admin.bean.UserJIDBean"%>
<%@page import="coffee.cms.admin.action.UserJIDAction"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
	
	String action = request.getParameter("action");
	TigUserAction act = new TigUserAction();
	if(action == null)
	{
		act.query(request);
	}
	else
	{
		if("delete".equals(action))
		{
			act.delete(request);
			act.query(request);
		}
		else if("toUpdate".equals(action))
		{
			act.toUpdate(request);
			request.getRequestDispatcher("update.jsp").forward(request, response);
			return;
		}
		else if("toShow".equals(action))
		{
			act.toUpdate(request);
			request.getRequestDispatcher("update.jsp?action=toShow").forward(request, response);
			return;
		}
	}
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
	function del(url,sid){
	  if(!confirm('您确定要删除吗?')){
		  return;
	  }
	  var frm = $("#tableForm");
	  frm.attr("action", url);
	  $("[name='sid']").val(sid);
	  $("[name='action']").val('delete');
	  
	  frm.submit();
	}
	var i = -1;
	// 选中‘反选 复选框
	function doChecked(nodeName){
	   i = i * -1;
	   if(i > 0){
	   	 $("[name=\"cid\"]").attr("checked",'true');//全选
	   }else{
	   	 $("[name=\"cid\"]").removeAttr("checked");//取消全选
	   }
	}
	//批量删除
	function delBatch(url){
		var sid = "";
		var checked=  $("[name=\"cid\"]:checked");
		if(checked.length <= 0 )
		{
			alert("请选择您要操作的数据");
			return;
		}
		if(!confirm("您确定删除吗？")) {
			return;
		}
		checked.each(function(){
			sid += $(this).val()+",";
	    });
		sid = sid.substring(0,sid.length-1);
		
		var frm = $("#tableForm");
		frm.attr("action", url);
		$("[name='sid']").val(sid);
		$("[name='action']").val('delete');
		frm.submit();
	}
</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 内容管理 - 列表</div>
	<form class="ropt">
		<input class="add" type="submit" value="添加" onclick="this.form.action='insert.jsp';"/>
	</form>
	<div class="clear"></div>
</div>
<div class="body-box">
	
	<form action="query.jsp" method="post" style="padding-top:5px;">
		<div>
		用户JID: <input type="text" name="jid" value="${jid}" style="width:100px"/>
			<input class="query" type="submit" value="查询"/>
		</div>
	</form>
	<!-- 批量删除  -->
	<div style="margin-top:15px;">
		<input type="button" value="批量删除" onclick="delBatch('query.jsp','cid');"/>
	</div>
	<!-- 查询结果  -->
	<form id="tableForm" method="post">
		<input type="hidden" name="action"/>
		<input type="hidden" name="sid"/>
		<input type="hidden" name="pager.curpage" value="${pager.curpage}"/>
		<table class="pn-ltable" style="" width="100%" cellspacing="1" cellpadding="0" border="0">
			<thead class="pn-lthead">
				<tr>
					<th width="30"><input type='checkbox' onclick="doChecked('cid')"/>
					</th>
					<th>ID</th>
					<th>用户JID</th>
					<th>账号创建时间</th>
					<th>最后登录</th>
					<th>最后注销</th>
					</tr>
			</thead>
			<tbody class="pn-ltbody">
				<c:forEach items="${pager.items}" var="item">
					<tr onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
						<td><input type="checkbox" name="cid" value="${item.id}"/></td>
						<td>${item.id}</td>
						<td>${item.userId}</td>
						<td><fmt:formatDate value="${item.accCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${item.lastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${item.lastLogoutTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td align="center">		
							<a href="query.jsp?action=toShow&sid=${item.id}" class="pn-opt">查看</a> | 
							<a href="query.jsp?action=toUpdate&sid=${item.id}" class="pn-opt">修改</a> | 		
							<a href="#" onclick="del('query.jsp','${item.id}')" class="pn-opt">删除</a>
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
      
</form>
</div>

</body>
</html>