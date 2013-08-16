/**
 * @author coffee
 * 表单验证：
 * required ： 必填项		/^\s*$/	
 * real 	: 实数		/^-?\d+(\.\d+)?$/
 * digit	： 数字		/^\d+(\.\d+)?$/
 * 
 */
function checkForm() {
	var nullReg = /^\s*$/; // 空
	var realReg = /^-?\d+(\.\d+)?$/; // 实数   
	var digitReg = /^\d+(\.\d+)?$/; // 数字 
	var postCodeReg = digitReg; // 邮编[6位数]
	var idCardReg = digitReg; // 身份证号
	/**
	 * text 文本框【非空】
	 * <input type='text' class="required"/> 
	 **/
	var txt = $("input[class*='required']");
	for ( var i = 0; i < txt.length; i++) {
		var obj = $("#" + txt.eq(i).attr("id"));
		var objError = $("#" + txt.eq(i).attr("id") + "Error");
		//if(obj.attr("type") == 'text'){
		if (nullReg.test(obj.val())) {
			objError.html("该项不能为空");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	txt = $("textarea[class*='required']");
	for ( var i = 0; i < txt.length; i++) {
		var obj = $("#" + txt.eq(i).attr("id"));
		var objError = $("#" + txt.eq(i).attr("id") + "Error");
		//if(obj.attr("type") == 'text'){
		if (nullReg.test(obj.val())) {
			objError.html("该项不能为空");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	/**
	 * 验证图片类型
	 */
	var imgs = $("input[class*='img']");
	for(var i=0;i<imgs.length;i++){
		if (/^\s*$/.test(imgs.eq(i).val()) == false) {
			if (checkImgType_2(imgs.eq(i).attr("id")) == false) {
				return false;
			}
		}
	}
	/**
	 * 验证视频类型
	 */
	var videos = $("input[class*='video']");
	for(var i=0;i<videos.length;i++){
		if (/^\s*$/.test(videos.eq(i).val()) == false) {
			if (checkVideoType_2(videos.eq(i).attr("id")) == false) {
				return false;
			}
		}
	}
	/**
	 * 下拉列表 【必选】
	 * <select class="required">
	 **/
	var sel = $("select[class*='required']");
	for ( var i = 0; i < sel.length; i++) {
		var id = sel.eq(i).attr("id");
		var obj = $("#" + id);
		var objError = $("#" + id + "Error");
		if (obj.val() == 0) {
			objError.html("请选择 . . . .");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	/**
	 * 实数[小数点]
	 */
	var real = $("input[class*='real']");
	for ( var i = 0; i < real.length; i++) {
		var id = real.eq(i).attr("id");
		var obj = real.eq(i);
		var objError = $("#" + id + "Error");
		if (realReg.test(obj.val()) == false) {
			objError.html("该项只能填实数");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	/**
	 * 数字
	 */
	var digit = $("input[class*='digit']");
	for ( var i = 0; i < digit.length; i++) {
		var id = digit.eq(i).attr("id");
		var obj = digit.eq(i);
		var objError = $("#" + id + "Error");
		if (digitReg.test(obj.val()) == false) {
			objError.html("该项只能填数字");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	/**
	 *  邮政编码
	 */
	var postCode = $("input[class*='postCode']");
	for ( var i = 0; i < postCode.length; i++) {
		var id = postCode.eq(i).attr("id");
		var obj = postCode.eq(i);
		var objError = $("#" + id + "Error");
		if (!nullReg.test(obj.val())) {//如果邮编不为空
			if (postCodeReg.test(obj.val()) == false) {//数字验证
				objError.html("该项只能填数字");
				objError.css("color", "red");
				obj.focus();
				obj.one("propertychange", function() {
					objError.html("");
				});
				return false;
			} else {// 长度[6位数]
				if (obj.val().length != 6) {
					objError.html("请输入6位数字");
					objError.css("color", "red");
					obj.focus();
					obj.one("propertychange", function() {
						objError.html("");
					});
					return false;
				}
			}
		}
	}
	/**
	 * 身份证号
	 */
	var idCard = $("input[class*='idCard']");
	for ( var i = 0; i < idCard.length; i++) {
		var id = idCard.eq(i).attr("id");
		var obj = idCard.eq(i);
		var objError = $("#" + id + "Error");
		if (!nullReg.test(obj.val())) {//如果身份证不为空
			if (idCardReg.test(obj.val()) == false) {//数字验证
				objError.html("该项只能填数字");
				objError.css("color", "red");
				obj.focus();
				obj.one("propertychange", function() {
					objError.html("");
				});
				return false;
			} else {// 长度[6位数]
				if (obj.val().length != 18) {
					objError.html("请输入18位数字");
					objError.css("color", "red");
					obj.focus();
					obj.one("propertychange", function() {
						objError.html("");
					});
					return false;
				}
			}
		}
	}
	return validateTextarea();
}
/**
 * 验证 password
 */
function validatePassword() {
	var pwd = $("input[type='password']");
	for ( var i = 0; i < pwd.length; i++) {
		var id = pwd.eq(i).attr("id");
		var obj = pwd.eq(i);
		var objError = $("#" + id + "Error");
		if (nullReg.test(obj.val())) {
			objError.html("密码不能为空");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	return false;
}
/**
 * 验证textarea
 */
function validateTextarea() {
	var ta = $("form textarea");
	for ( var i = 0; i < ta.length; i++) {
		var obj = $("#" + ta.eq(i).attr("id"));
		var objError = $("#" + ta.eq(i).attr("id") + "Error");
		if (ta.eq(i).text().length > 3000) {
			objError.html("文本框的内容长度不超过3000");
			objError.css("color", "red");
			obj.focus();
			obj.one("propertychange", function() {
				objError.html("");
			});
			return false;
		}
	}
	return true;
}

function checkImgType() {
	var pic = $("#urlpic");
	var picError = $("#urlpicError");
	if (/^\s*$/.test(pic.val()) == false) {
		if(/.+\.jpg|\.jpeg|\.gif|\.png$/.test(pic.val())) {
			picError.html(" ");
			return true;
		}
	}
	picError.html("支持的图片格式为:jpg | jpeg | gif | png");
	picError.css("color", "red");
	return false;
}

/**检验图片类型格式是否合法
 * @param {Object} id 传入参数 id
 * @return {TypeName} 
 */
function checkImgType_2(id) {
	var pic = $("#"+id);
	var picError = $("#"+id+"Error");
	if (/^\s*$/.test(pic.val()) == false) {
		if(/.+\.jpg|\.jpeg|\.gif|\.png$/.test(pic.val())) {
			picError.html(" ");
			return true;
		}
	}
	picError.html("支持的图片格式为:jpg | jpeg | gif | png");
	picError.css("color", "red");
	return false;
}
/**
 * 检验视频类型是否合法
 * @param {Object} id
 * @return {TypeName} 
 */
function checkVideoType_2(id) {
	var video = $("#"+id);
	var videoError = $("#"+video.attr("id")+"Error");
	if (/.+\.wmv|\.flv$/.test(video.val())) {
		videoError.html(" ");
	} else {
		videoError.html("视频的图片格式为:wmv|flv");
		videoError.css("color", "red");
		return false;
	}
	return true;
}

/**
 * 验证字段是否为空
 * 若为空；给予提示 且返回false
 * @param {Object} obj
 * @return {TypeName} 
 */
function validateRequiredField(obj){
	var objError = $("#"+obj.attr("id")+"Error");
	if (/^\s*$/.test(obj.val())) {
		objError.html("该项不能为空");
		objError.css("color", "red");
		obj.focus();
		obj.one("propertychange", function() {
			objError.html("");
		});
		return false;
	}
	return true;
}

