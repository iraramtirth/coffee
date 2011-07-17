<%@ page contentType="text/html; charset=UTF-8"%>
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path",path);
%>
<%@ include file="inc/header.inc" %>		
			<!--中间内容-->
			<div class="center">
				<div id="box" onload="menu()">
					<img src="images/long1.jpg" id="picture" />
					<div class="lvjing">
						<!--悦享夏日风情礼,欢乐赠品大放送,欢乐购物3重大礼,你买我就赠....-->
					</div>
					<div class="lvjing1"></div>
					<span id="number"> <a class="redd" onmouseover="go_to(1)"
						href="#">7月最低折扣</a><a onmouseover="go_to(2)" href="#">7月最低折扣</a><a
						onmouseover="go_to(3)" href="#">7月最低折扣</a><a
						onmouseover="go_to(4)" href="#">7月最低折扣</a>
					</span>
				</div>
				<script type="text/javascript">
					function go_to(e) {
						document.getElementById("picture").src = "images/long" + e + ".jpg";
					}
	var k = 1;
	function zidong() {
		document.getElementById("picture").src = "images/long" + k + ".jpg";
		var s = document.getElementById("number").getElementsByTagName("a");
		for ( var j = 0; j < s.length; j++) {
			s[j].style.background = "#fff";
			s[j].style.color = "#000";
			s[j].style.filter = "alpha(opacity=90)";
			s[j].style.opacity = "1";
		}
		s[k - 1].style.background = "#F00";
		s[k - 1].style.color = "#fff";
		s[k - 1].style.filter = "alpha(opacity=80)";
		s[k - 1].style.opacity = "0.5";
		k++;
		if (k > 4) {
			k = 1;
		}
	}
	var t = setInterval(zidong, 2000);

	function menu() {
		var e = document.getElementById("number").getElementsByTagName("a");
		for ( var i = 0; i < e.length; i++) {
			e[i].onmouseover = function() {
				clearInterval(t);
				var m = this.getAttribute("title");
				if (document.all) {
					document.getElementById("picture").filters.revealTrans.Transition = 30;
					document.getElementById("picture").filters.revealTrans.apply();
					document.getElementById("picture").filters.revealTrans.play();
				}
				document.getElementById("picture").src = "images/long" + m	+ ".jpg";
			};
			e[i].onmouseout = function() {
				t = setInterval(zidong, 2000);
			};
		}
	}
</script>
		<div class="cc">
			<div class="ccl">
				<div class="cclh">
					<h3>
						公司新闻
					</h3>
					<span><a href="#">更多</a>
					</span>
				</div>
				<ul>
					<li>
						<span>2011-06-24</span><a href="#">010全球移动互联网大会5月27日在北京举行...</a>
					</li>
					<li>
						<span>2011-06-24</span><a href="#">随着用户推广业务的发力，进入2010年以来，买卖宝无线商城日浏览用户不断递增，已稳定达到

							百万量级。 </a>
					</li>
					<li>
						<span>2011-06-24</span><a href="#">2010年，买卖宝对商品的品牌化升级加大力度，

							引进松下小家电的销售，并将陆续与其它知名品 牌合作。 </a>
					</li>
					<li>
						<span>2011-06-24</span><a href="#">2010年，买卖宝对商品的品牌化升级加大力度，

							引进松下小家电的销售，并将陆续与其它知名品 牌合作。 </a>
					</li>
				</ul>
			</div>
			<div class="ccc">
				<div class="cclh">
					<h3>
						公司新闻
					</h3>
					<span><a href="#">更多</a>
					</span>
				</div>
				<p>
					以成功运作的“买卖宝”商城为蓝本，商机 无限为广大生产销售型企业体提供“无线电子商 务整体解决方案”，包括电子商务站点建设、购

					物流程梳理、品牌营销推广，帮助企业架构自己 的全流程无线B2C业务。
				</p>
				<p>
					根据中国互联网中心在2010年发布的《中国 互联网络发展状况统计报告》，中国的手机互联

					网用户已达到2.33亿，这部分人群多数为传统互 联网无法覆盖的二、三级市场甚至农村人群；随

					着中国无线通讯3g时代的全面展开，这一人群还 将成倍数增长，而我国手机用户总数为7.48亿。
				</p>
			</div>
			<div class="ccr">
				<a href="#"><img src="images/cc_03.jpg" />
				</a><a href="#"><img src="images/cc_09.jpg" alt="" />
				</a>
			</div>
		</div>
		<div class="cf">
			<h2>
				合作伙伴
			</h2>
			<a href="#"><img src="images/f_10.jpg" alt="" />
			</a><a href="#"><img src="images/f_13.jpg" alt="" />
			</a><a href="#"><img src="images/f_15.jpg" alt="" />
			</a><a href="#"><img src="images/f_17.jpg" alt="" />
			</a><a href="#"><img src="images/f_19.jpg" alt="" />
			</a><a style="margin: 0;" class="dian" href="#"><img
					src="images/f_22.jpg" style="margin: 15px 0 0 0" alt="" />
			</a>
		</div>
	</div>
 <%@ include file="inc/footer.inc" %>