/**
 * 删除记录 
 * @param {Object} url
 */
function delete_(url) {
	if (confirm("确定删改该记录?")) {
		window.location.href = url;
	}
	return false;
}

/**
 * 全选：取消全选
 * @param {Object} thiz
 * @param {Object} checkboxName
 */
function selectAll(thiz, checkboxName) {
	if (checkboxName == undefined) {
		checkboxName = thiz.name; //'sid';
	}
	var flag = thiz.checked;
	var sid = document.getElementsByName(checkboxName);
	for ( var i = 0; i < sid.length; i++) {
		if (flag == true) {
			sid[i].checked = true;
		} else {
			sid[i].checked = false;
		}
	}
}