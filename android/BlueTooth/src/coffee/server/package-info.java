/**
 * tcp协议通信规范<br>
 * ------------------------------------
 * ------------------------------------
 * 指令:消息来源:消息去向:消息体:time
 * ------------------------------------
 * ------------------------------------
 * 其中time会经过server端添加
 * 如果消息去向是服务端,则用serv表示
 * ------------------------------------
 * 1:用户上线
 * 客户端coffee:发送指令给服务端进行用户注册
 * 		online:coffee:serv:在线状态:
 * 服务器收到指令广播给当前在线的客户端 (消息原样转发)
 * 		online:coffee:client:在线状态:time
 * 其他客户端client将自己的状态告知新上线的客户端coffee
 * 		应答格式为:
 * 		online-ack:client:coffee:在线状态:
 * 服务器端接收后进行消息的转发--to coffee
 * 		online-ack:client:coffee:在线状态:time
 * 客户端coffee接收
 * 		online-ack:client:coffee:在线状态:time
 * 此时整个上线过程结束
 * coffee —> server -> client -> server -> coffee
 * 其中在线状态:
 * -1代表离线
 * 1表示在线
 *------------------------------------------ 
 *
 * 2:消息的发送
 * 发送端格式为:
 * 		message:coffee:client:body:
 * 服务器端转发:
 * 		message:offee:client:body:time
 * 接收端接收:
 * 		message:coffee:client:body:time
 */

/**
 * @author coffee
 * <br> 2014年4月11日上午11:21:42
 */
package coffee.server;

