<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<link href="css/erji.css" rel="stylesheet" type="text/css" />

<%@ include file="inc/header.inc" %>	
  <!--中间内容-->
  <div class="center"><img src="images/er_03.jpg" alt="" />
    <div class="cn"><a href="#">首页&nbsp;&nbsp;</a><a href="#">人才招聘&nbsp;&nbsp;</a><a style="background:none;" href="#">最新招聘&nbsp;&nbsp;</a></div>
    <div class="cc">
      <div class="cr">
        <h1>人才招聘</h1>
        <ul>
          <li class="red"><a href="#">最新招聘</a></li>
          <li><a href="#">社会招聘</a></li>
          <li><a href="#">校园招聘</a></li>
        </ul>
      </div>
      <div class="cl">
        <h1>最新招聘</h1>
        <p>销售客服 <br />
          工作地点：北京 <br />
          招聘人数：20 <br />
          工作经验：1-2年 <br />
          学　　历：大专以上 <br />
        </p>
        <p> 职责描述： <br />
          1、 接听用户咨询电话，解答用户的疑问； <br />
          2、 电话联系用户完成订单确认工作； <br />
          3、 定期对老用户进行电话回访； <br />
          4、 跟踪订单状态，保持和顾客的沟通； <br />
          5、 拓展新用户群体； <br />
          6、 在各个环节提供优质的服务，挖掘客户需求，推荐相应公司上线的产品； <br />
        </p>
        <p> 职位要求： <br />
          1、 性格开朗、普通话标准、具有良好的口语表达能力和沟通能力； <br />
          2、 热爱销售工作，工作积极、勤奋、执着； <br />
          3、 具有较强的学习能力； <br />
          4、 熟悉计算机操作及office办公软件的使用； <br />
          5、 能承受较大的工作压力，能接受倒班制的工作时间（公司提供宿舍休息）； <br />
          6、 有电子商务、电视购物客服工作或电话销售经验者优先。 <br />
        </p>
      </div>
    </div>
  </div>
  
  <%@ include file="inc/footer.inc" %>
