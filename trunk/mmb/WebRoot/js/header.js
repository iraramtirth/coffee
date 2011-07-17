//设置导航nav的样式
function setNavClass(index) {
	var a = document.getElementById("nav1").getElementsByTagName("a");
	for (i = 0; i < a.length; i++) {
		if (index == i) {
			a[i].className = "wang";
			alert("xxx");
		} else {
			a[i].className = "";
		}
	}
}
