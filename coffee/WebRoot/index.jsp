<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
		window.location.href = "<%=request.getContextPath()%>/user/list.action";
</script>
</head>
<body>
	hello world
	
	<form action="demo/insert" method="post">
		<input type="text" name="test" value="111">		
		<input type="file" name="file">
		<input type="submit" value="提交">
	</form>
</body>
</html>