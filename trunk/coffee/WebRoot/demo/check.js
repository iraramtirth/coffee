	function goSub(){
		  if(checkForm() == false){
			  return;
		  }
		  if($("#info").val() != "true"){
				setInfo();
				return false;
		  }
		  document.getElementById('f1').submit();
	}
	// 检查网站名称存不存在
	function check_username(){
		$.post('ajax/user/checkUsername.action',{'model.username':$("#username").val()},
			function(data,textStatus){
				data = eval("("+data+")");
				if(textStatus == "success"){
					$("#info").val("true");
				}else{
					setInfo(data.info);
					return false;
				}
		});
	}
	
	function setInfo(info){
		$("#usernameError").html(info);
		//标识
		$("#info").val("false");
		$("#usernameError").css("color", "red");
		$("#username").focus();
		$("#username").one("propertychange", function() {
			$("#usernameError").html("");
			$("#info").val("true");
		});
	}