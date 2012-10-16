<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>jeecms-left</title>
<#include "/jeecms_sys/head.html"/>
</head>
<body>
  	    <div class="box-positon">
        	 <h1><@s.m "global.position"/>: <@s.m "global.admin.home"/> - <@s.m "global.admin.index"/></h1>
        </div>
<div class="body-box">
        <div class="welcom-con">
        	 <div class="we-txt">
             	  <p>
                  欢迎使用JEECMS内容管理系统！<br />
                  JEECMS程序版本： jeecms-2012-sp1 【<a href="http://www.jeecms.com" target="_blank">查看最新版本</a>】<br />
                  您上次登录的时间是：${user.lastLoginTime?string('yyyy-MM-dd')}<br />
                  已用内存：<span style="color:#0078ff;">${(usedMemory/1024/1024)?string("0.##")}MB</span>&nbsp;&nbsp;&nbsp;&nbsp;剩余内存：<span style="color:#ff8400;">${(useableMemory/1024/1024)?string("0.##")}MB </span>&nbsp;&nbsp;&nbsp;&nbsp;最大内存：<span style="color:#00ac41;">${(maxMemory/1024/1024)?string("0.##")}MB</span>
                  </p>
             </div>
             <ul class="ms">
             	<li class="wxx">访问量</li><li class="attribute">　　　系统属性</li>
             </ul>
             <div class="ms-xx">
                 <div class="xx-xx">
             	      <table width="100%" border="0" cellspacing="0" cellpadding="0">
             	       <tr>
                        <td width="20%" height="30" align="right"></td>
                        <td width="25%"><b>PV</b></td>
                        <td width="25%"><b>IP</b></td>
                        <td width="30%"><b>独立访客</b></td>
                    </tr>
                      <tr>
                        <td height="30" align="right">今日：</td>
                        <#list flowMap['today'] as f>
                     	<td>${f.count}</td>
                     	</#list>
                    </tr>
                      <tr>
                        <td height="30" align="right">昨日：</td>
                        <#list flowMap['yesterday'] as f>
                     	<td>${f.count}</td>
                     	</#list>
                    </tr>
                      <tr>
                        <td height="30" align="right">本周：</td>
                        <#list flowMap['thisweek'] as f>
                     	<td>${f.count}</td>
                     	</#list>
                    </tr>
                      <tr>
                        <td height="30" align="right">本月：</td>
                        <#list flowMap['thismonth'] as f>
                     	<td>${f.count}</td>
                     	</#list>
                     </tr>
                     <tr>
                        <td height="30" align="right">累计：</td>
                        <#list flowMap['total'] as f>
                     	<td>${f.count}</td>
                     	</#list>
                     </tr>
               </table>
                 </div>
                 <div class="attribute-xx" style="float:left">
                 	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td width="30%" height="30" align="right">操作系统版本：</td>
                            <td height="30"><span class="black">${props['os.name']!} ${props['os.version']!}</span></td>
                        </tr>
                          <tr>
                            <td width="30%" height="30" align="right">操作系统类型：</td>
                            <td height="30"><span class="black">${props['os.arch']!} ${props['sun.arch.data.model']!}位</span> </td>
                        </tr>
                          <tr>
                            <td width="30%" height="30" align="right">用户、目录、临时目录：</td>
                            <td height="30"><span class="black">${props['user.name']!}, ${props['user.dir']!}, ${props['java.io.tmpdir']!}</span></td>
                        </tr><tr>
                            <td width="30%" height="30" align="right">JAVA运行环境：</td>
                            <td height="30"><span>${props['java.runtime.name']!} ${props['java.runtime.version']!}</span></td>
                          </tr>
                          <tr>
                            <td width="30%" height="30" align="right">JAVA虚拟机：</td>
                            <td height="30"> <span>${props['java.vm.name']!} ${props['java.vm.version']!}</span></td>
                        </tr>
                   </table>  
               </div>

             </div>
             
  </div>
</body>
</html>