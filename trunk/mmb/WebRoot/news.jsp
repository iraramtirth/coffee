<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<link href="css/erji.css" rel="stylesheet" type="text/css" />
<%@ include file="inc/header.inc" %>	
<!--中间内容-->
  <div class="center"><img src="images/er_03.jpg" alt="" />
    <div class="cn"><a href="#">首页&nbsp;&nbsp;</a><a href="#">公司简介&nbsp;&nbsp;</a><a style="background:none" href="#">公司动态&nbsp;&nbsp;</a></div>
    <div class="cc">
      <div class="cr">
        <h1>新闻中心</h1>
        <ul>
          <li class="red"><a href="#">行业动态</a></li>
          <li><a href="#">公司动态</a></li>
          
        </ul>
      </div>
      <div class="cl">
        <h1>公司动态</h1>
       <ul><span class="fen">第1页共2页</span><li><a href="#">1</a></li><li><a class="gl" href="#">2</a></li><span  style="float:left; margin-top:-25px;  margin-left:490px; width:80px; ">共9篇新闻</span>
       <div class="clear"></div>
       </ul>
       <div class="coc">
       <p>2011-06-07<font class="hh">买卖宝作为中国移动电子商务代表企业参与2010GMIC...</font>  </p>
          <p class="hou">2010全球移动互联网大会5月27日在北京举行，买卖宝作为中国移动电子
         </p> <p class="hou">商务的代表企业，参与了"移动电子商务论坛"的讨论。 </p>
          <p class="hou hou1">了解更多</p>
       </div>
       
       
           <div class="coc">
       <p>2011-06-07<font class="hh">买卖宝作为中国移动电子商务代表企业参与2010GMIC...</font>  </p>
          <p class="hou">2010全球移动互联网大会5月27日在北京举行，买卖宝作为中国移动电子
         </p> <p class="hou">商务的代表企业，参与了"移动电子商务论坛"的讨论。 </p>
          <p class="hou hou1">了解更多</p>
       </div>
           <div class="coc" style="border-bottom:0;">
       <p>2011-06-07<font class="hh">买卖宝作为中国移动电子商务代表企业参与2010GMIC...</font>  </p>
          <p class="hou">2010全球移动互联网大会5月27日在北京举行，买卖宝作为中国移动电子
         </p> <p class="hou">商务的代表企业，参与了"移动电子商务论坛"的讨论。 </p>
          <p class="hou hou1">了解更多</p>
       </div>
      </div>
    </div>
  </div>
  <%@ include file="inc/footer.inc" %>
