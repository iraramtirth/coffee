<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="pg" uri="http://jsptags.com/tags/navigation/pager"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String path = request.getContextPath();
	pageContext.setAttribute("path", path);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>

<link href="${path}/res/css/admin.css" rel="stylesheet" type="text/css" />
<link href="${path}/res/css/theme.css" rel="stylesheet" type="text/css" />
<link href="${path}/res/common/css/jquery.validate.css" rel="stylesheet" type="text/css" />
<link href="${path}/res/common/css/jquery.treeview.css" rel="stylesheet" type="text/css" />
<link href="${path}/res/common/css/jquery.ui.css" rel="stylesheet" type="text/css" />

<script src="${path}/res/common/js/jquery.js" type="text/javascript"></script>
<script src="${path}/res/common/js/jquery.ext.js" type="text/javascript"></script>
<script src="${path}/res/js/admin.js" type="text/javascript"></script>
<script src="${path}/res/common/js/pony.js" type="text/javascript"></script>

<script type="text/javascript">var types = [];	types[0] = {hasImage:false,imgWidth:100,imgHeight:100};	types[1] = {hasImage:true,imgWidth:143,imgHeight:98};	types[2] = {hasImage:true,imgWidth:280,imgHeight:200};	types[3] = {hasImage:false,imgWidth:0,imgHeight:0};var channels = [];	channels[0] = {		id:1,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[1] = {		id:11,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[2] = {		id:12,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[3] = {		id:13,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[4] = {		id:14,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[5] = {		id:15,		hasTitleImg:true,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[6] = {		id:40,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[7] = {		id:41,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[8] = {		id:42,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[9] = {		id:43,		hasTitleImg:true,		titleImgWidth:67,		titleImgHeight:50,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[10] = {		id:44,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[11] = {		id:45,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[12] = {		id:46,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[13] = {		id:49,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[14] = {		id:50,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[15] = {		id:51,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[16] = {		id:52,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[17] = {		id:9,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[18] = {		id:37,		hasTitleImg:true,		titleImgWidth:48,		titleImgHeight:48,		hasContentImg:true,		contentImgWidth:139,		contentImgHeight:98	};	channels[19] = {		id:38,		hasTitleImg:true,		titleImgWidth:48,		titleImgHeight:48,		hasContentImg:true,		contentImgWidth:139,		contentImgHeight:98	};	channels[20] = {		id:39,		hasTitleImg:true,		titleImgWidth:48,		titleImgHeight:48,		hasContentImg:true,		contentImgWidth:139,		contentImgHeight:98	};	channels[21] = {		id:57,		hasTitleImg:true,		titleImgWidth:48,		titleImgHeight:48,		hasContentImg:true,		contentImgWidth:180,		contentImgHeight:120	};	channels[22] = {		id:47,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[23] = {		id:53,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[24] = {		id:54,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[25] = {		id:55,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[26] = {		id:48,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};	channels[27] = {		id:56,		hasTitleImg:false,		titleImgWidth:139,		titleImgHeight:139,		hasContentImg:false,		contentImgWidth:310,		contentImgHeight:310	};function typeChange(n) {	var tr = $('#tr-typeImg');	tr.toggle(types[n].hasImage);	$('#zoomWidth0').val(types[n].imgWidth);	$('#zoomHeight0').val(types[n].imgHeight);}function channelChange(n) {	if(n==0) {		return;	}	//0为请选择，所以必须减一。	n--;	var trt = $('#tr-titleImg');	var trc = $('#tr-contentImg');	trt.toggle(channels[n].hasContentImg);	$('#zoomWidth1').val(channels[n].titleImgWidth);	$('#zoomHeight1').val(channels[n].titleImgHeight);	trc.toggle(channels[n].hasTitleImg);	$('#zoomWidth2').val(channels[n].contentImgWidth);	$('#zoomHeight2').val(channels[n].contentImgHeight);	fetchTopics(channels[n].id);}function fetchTopics(channelId) {	$.getJSON("../topic/by_channel.do",{channelId:channelId},function(topics) {		var ts = $("#topics");		ts.empty();		var len = topics.length;		for(var i=0;i<len;i++) {			ts.append("<label><input type='checkbox' name='topicIds' value='"+topics[i].id+"'/>"+topics[i].name+"</label> ");		}		ts.parent().toggle(len>0);	});}$.validator.methods.leafChannel = function(value, element, param) {	var i = element.selectedIndex;	return $(element.options[i]).attr("class")!="sel-disabled";}$(function() {	$("#titleColor").colorPicker();	$("#jvForm").validate({		rules: {			channelId: {				required: true,				leafChannel: true			},			mediaType: {				required: function() {return $("#mediaPath").val()!=""}			}		},		messages:{			channelId: {				leafChannel: "请选择末级栏目"			},			mediaType:$.validator.messages.required		}	});	//副栏目对话框	$("#channelsDialog").dialog({		autoOpen: false,		modal: true,		width: 280,		height: 400,		position: ["center",20],		buttons: {			"OK": function() {				$("#channelsSelector input[name='channels']:checked").each(function(){					appendChannels(this);					$(this).removeAttr("checked");				});				$(this).dialog("close");			}		}	});	$('#channelsLink').click(function(){		$('#channelsDialog').dialog('open');		return false;	});		$("#channelsSelector").treeview({		url: "v_tree_channels.do"	});});function appendChannels(channel) {	var hasContain = false;	$("input[name=channelIds]").each(function() {		if($(this).val()==$(channel).val()) {			hasContain = true;		}	});	if(hasContain) {		return;	}	var nodeList = eval($(channel).attr("nodeList"));	var s = "<div style='padding-top:3px'>";	for(var i=0,len=nodeList.length;i<len;i++) {		s += nodeList[i];		if(i<len-1) {			s += " > ";		}	}	s += " <a href='javascript:void(0);' onclick='$(this).parent().remove();' class='pn-opt'>删除</a>";	s += "<input type='hidden' name='channelIds' value='"+$(channel).val()+"'/>";	s += "</div>";	$("#channelsContainer").append(s);}</script>
<style type="text/css">
.sel-disabled {
	background-color: #ccc
}
</style>
</head>
<body>
	<div class="box-positon">
		<div class="rpos">当前位置: 内容管理 - 添加</div>
		<form class="ropt">
			<input type="submit" value="返回列表"
				onclick="this.form.action='v_list.do';" class="return-button" /> <input
				type="hidden" name="cid" value="" />
		</form>
		<div class="clear"></div>
	</div>
	<div class="body-box">
		<form method="post" action="o_save.do" id="jvForm">
			<table width="100%" class="pn-ftable" cellpadding="2" cellspacing="1"
				border="0">
				<tr>
					<td width="10%" class="pn-flabel pn-flabel-h"><span
						class="pn-frequired">*</span>栏目:</td>
					<td colspan="3"  class="pn-fcontent"><div
							style="float: left">
							<div>
								<select id="channelId" name="channelId"
									onchange="channelChange(this.selectedIndex)">
									<option value="" class="sel-disabled">--请选择--</option>
									<option value="1" class="sel-disabled">新闻</option>
									<option value="11">&nbsp;&nbsp;>国内新闻</option>
									<option value="12">&nbsp;&nbsp;>国际新闻</option>
									<option value="13">&nbsp;&nbsp;>社会热点</option>
									<option value="14">&nbsp;&nbsp;>时事评论</option>
								</select> <input class="vice-channel" id="channelsLink" type="button"
									value="副栏目" /><span class="pn-fhelp">只能选择末级栏目</span>
							</div>
							<div>
								<label for="channelId" class="error" generated="true"></label>
							</div>
							<div id="channelsContainer"></div>
						</div>
						<div style="float: left; padding-left: 7px; display: none">
							&nbsp;专题: <span id="topics"></span>
						</div>
						<div style="clear: both"></div></td>
				</tr>
				<tr id="tr-title">
					<td width="10%" class="pn-flabel pn-flabel-h"><span
						class="pn-frequired">*</span>标题:</td>
					<td colspan="3"  class="pn-fcontent"><input
						type="text" maxlength="150" name="title" class="required"
						size="70" maxlength="150" /><label><input type="checkbox"
							onclick="$('#linkDiv').toggle(this.checked);if(!this.checked){$('#link').val('');}" />外部链接</label>
						<div id="linkDiv" style="display: none">
							url: <input type="text" id="link" name="link" size="35"
								maxlength="255" />
						</div></td>
				</tr>
				<tr id="tr-tagStr">
					<td width="10%" class="pn-flabel pn-flabel-h">Tag标签:</td>
					<td colspan="3" class="pn-fcontent"><input
						type="text" maxlength="50" name="tagStr" size="35" maxlength="50" />
						<span class="pn-fhelp">用","分开</span></td>
				</tr>
				<tr id="tr-description">
					<td width="10%" class="pn-flabel pn-flabel-h">摘要:</td>
					<td colspan="3" class="pn-fcontent"><textarea
							cols="70" rows="3" name="description" maxlength="255"></textarea></td>
				</tr>
				<tr id="tr-viewGroupIds">
					<td width="10%" class="pn-flabel pn-flabel-h">浏览权限:</td>
					<td colspan="3" class="pn-fcontent"><label><input
							type="checkbox" value="1" name="viewGroupIds" />普通会员</label> <label><input
							type="checkbox" value="2" name="viewGroupIds" />高级组</label> <span
						class="pn-fhelp">不选则继承栏目权限</span></td>
				</tr>
				<tr id="tr-topLevel">
					<td width="10%" class="pn-flabel pn-flabel-h">发布时间:</td>
					<td colspan="1" class="pn-fcontent"><input
						type="text" name="releaseDate" class="Wdate" style="width: 140px"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> <span
						class="pn-fhelp">留空则为当前时间</span></td>
				</tr>
			 
				<tr id="tr-txt">
					<td width="10%" class="pn-flabel pn-flabel-h">内容:</td>
					<td colspan="3" class="pn-fcontent">
					<script
							type="text/javascript">var txt = new FCKeditor("txt");txt.BasePath = "/thirdparty/fckeditor/";txt.Config["CustomConfigurationsPath"]="/thirdparty/fckeditor/myconfig.js";txt.Config["LinkBrowser"] = false ;txt.Config["ImageBrowser"] = false ;txt.Config["FlashBrowser"] = false ;txt.Config["MediaBrowser"] = false ;txt.Config["LinkBrowserURL"] = "/thirdparty/fckeditor/editor/filemanager/browser/default/browser.html?Connector=/jeeadmin/jeecms/fck/connector.do" ;txt.Config["ImageBrowserURL"] = "/thirdparty/fckeditor/editor/filemanager/browser/default/browser.html?Type=Image&Connector=/jeeadmin/jeecms/fck/connector.do" ;txt.Config["FlashBrowserURL"] = "/thirdparty/fckeditor/editor/filemanager/browser/default/browser.html?Type=Flash&Connector=/jeeadmin/jeecms/fck/connector.do" ;txt.Config["MediaBrowserURL"] = "/thirdparty/fckeditor/editor/filemanager/browser/default/browser.html?Type=Media&Connector=/jeeadmin/jeecms/fck/connector.do" ;txt.Config["LinkUpload"] = true ;txt.Config["ImageUpload"] = true ;txt.Config["FlashUpload"] = true ;txt.Config["MediaUpload"] = true ;txt.Config["LinkUploadURL"] = "/jeeadmin/jeecms/fck/upload.do" ;txt.Config["ImageUploadURL"] = "/jeeadmin/jeecms/fck/upload.do?Type=Image" ;txt.Config["FlashUploadURL"] = "/jeeadmin/jeecms/fck/upload.do?Type=Flash" ;txt.Config["MediaUploadURL"] = "/jeeadmin/jeecms/fck/upload.do?Type=Media" ;txt.ToolbarSet="My";txt.Height=230;txt.Value="";txt.Create();</script></td>
				</tr>
				<tr>
					<td colspan="4" class="pn-fbutton"><input type="hidden"
						name="cid" value="" /> <input type="submit" value="提交"
						class="submit" class="submit" /> &nbsp; <input type="reset"
						value="重置" class="reset" class="reset" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>