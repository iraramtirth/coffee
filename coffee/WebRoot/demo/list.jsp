<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
pageContext.setAttribute("path",path);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>myccibs演示...</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link href="<%=path%>/resource/css/all.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=path%>/resource/js/jquery.js"></script>
	
	<script type="text/javascript">
	function del(id){
	   if(confirm('您确定要删除吗?')){
	      window.location="<%=path%>/user/delete.action?model.id="+id+"&pager.curpage=${pager.curpage}";
	   }
	   return false;
	}
	var i = -1;
	// 选中‘反选 复选框
	function doChecked(nodeName){
	   i = i * -1;
	   if(i > 0){
	   	 $("[name='sid']").attr("checked",'true');//全选
	   }else{
	   	 $("[name='sid']").removeAttr("checked");//取消全选
	   }
	}
	//批量删除
	function delBatch(sid,url){
		var ids = "";
        $("[name='"+sid+"'][checked]").each(function(){
   	        ids += $(this).val()+",";
        })
        window.location.href = url+"?ids="+ids;
	}
</script>
  </head>
  
<body class="right_body">
<div class="active"></div>
<!-- 内容开始 -->
  <div id="myTab1_Content1" class="none  right_nav" >
    <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="0" class="table_more">
      <tr>
        <td width="22%"><a href="<%=path%>/user/toInsert.action">添加记录</a></td>
        <td width="1%" align="right">&nbsp;</td>
      </tr>
    </table>
    <table border="0" cellpadding="0" cellspacing="0" class="table2">
      <tr >
        <td width="15%" class="td2">用户名</td>
        <td width="13%" class="td2">密码</td>
        <td width="14%" class="td2">年龄</td>
        <td width="13%" class="td2">出生年月</td>
        <td width="13%" class="td2">状态</td> 
        <td width="13%" class="td2">编辑</td> 
        <td width="13%" class="td2">删除</td>
        <td width="12%" class="td2"> 
        	<a href="javascript:void();" onclick="if(confirm('您确定要删除吗?')) delBatch('sid','<%=path%>/user/deleteBatch.action')">批量删除</a> 
        	<input type="checkbox" onclick="doChecked('sid');"> 
        </td>
      </tr>
    </table>
    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3">
   	  <c:forEach items="${pager.items}" var="item">
	      <tr  onmouseover="this.className='b_list_active';"  onmouseout="this.className='b_list';">
	        <td width="15%">${item.username}&nbsp; </td>
	        <td width="13%">${item.password}&nbsp; </td>
	        <td width="14%">${item.age}&nbsp; </td>
	        <td width="13%">
	         	<fmt:formatDate value="${item.birthday}" pattern="yyyy-MM-dd"/>
	        	&nbsp;
	        </td>
            <td width="13%" class="hover">
           		&nbsp;
            </td>
	        <td width="13%" class="hover">
				<a href="<%=path%>/user/toUpdate.action?model.id=${item.id}&pager.curpage=${pager.curpage}">修改</a>	        		
	        	&nbsp;
	       </td>
	       <td width="13%" class="hover">
				<a href="" onclick="return del('${item.id}')">删除</a>       		
	        	&nbsp;
	       </td>
	       <td width="13%" class="hover">
				<input type="checkbox" name="sid" value="${item.id}">		
	        	&nbsp;
	       </td>
	      </tr>
   	  </c:forEach>
    </table>
   <table width="100%" border="0" cellpadding="0" cellspacing="0" bordercolor="0" class="table">
       <tr>
        <td width="24%" align="right">
      	  <pg:pager scope="request" maxIndexPages="10" index="center"
				    maxPageItems="${pager.size}" url="${path}/user/list.action" items="${pager.total}"
				    export="currentPageNumber=pageNumber">
			<!-- 参数 -->
			
		    <!-- 以下内容保持不变 -->
		    <pg:first> <a href="${pageUrl}">首页</a> </pg:first>
		    <pg:prev> <a href="${pageUrl}">前页</a> </pg:prev>
		    <pg:pages>
		     <c:choose>
		      <c:when test="${pageNumber eq currentPageNumber }">
		       <font color="red">${pageNumber}</font>
		      </c:when>
		      <c:otherwise>
		       <a href="${pageUrl}">${pageNumber}</a>
		      </c:otherwise>
		     </c:choose>
		    </pg:pages>
		    <pg:next> <a href="${pageUrl}">下页</a> </pg:next>
		    <pg:last> <a href="${pageUrl}">尾页</a> </pg:last>
	     </pg:pager>	
	     &nbsp;&nbsp;&nbsp;&nbsp;								
      	</td>
      </tr>
    </table>
  </div>
</body>
</html>
