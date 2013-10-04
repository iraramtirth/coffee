package com.fetion.util.sqlite;

public class Sql {

	/**
	 * 
	 * @return
	 */
	public String getCreate() {
		StringBuilder sb = new StringBuilder();
		/**
		 * message
		 */
		sb.append("create table message");
		sb.append("(");
		sb.append("_id integer primary key autoincrement,");
		sb.append("fetionId varchat(20) not null,");// 联系人表关联
		sb.append("userId varchat(20),");// 联系人userId 详见Contact#getId 
		sb.append("userName varchat(20),");//  联系人姓名  详见Contact#getLocalName 
		sb.append("type integer default 0,");// 消息类型 #
		sb.append("content varchar(255) not null,");
		sb.append("time integer not null,");
		sb.append("picLocalSmall varcha(255),");
		sb.append("picLocalBig varcha(255),");
		sb.append("picNet varchar(255),");
		sb.append("failed integer default 0,"); // 信息是否发送失败 - 0未失败 1失败
		sb.append("read integer default 0,"); // 信息是否已读- 0未失读 1读
		sb.append("sendOrRecv integer default 0");// 消息是发送的还是接收的
		sb.append(");");
		/**
		 * conversation
		 */
		sb.append("create table conversation");
		sb.append("(");
		sb.append("_id integer primary key autoincrement,");
		sb.append("fetionId varchat(20) not null,");// 联系人表关联
		sb.append("uri varchat(50) not null,");// 联系人uri
		sb.append("userId varchat(20),");// 联系人userId 详见Contact#getId 
		sb.append("userName varchat(20),");// 联系人姓名  详见Contact#getLocalName
		sb.append("type integer default 0,");// 消息类型 #
		sb.append("content varchar(255) not null,");
		sb.append("time integer not null");
		sb.append(");");
		return sb.toString();
	}

	/**
	 * 目前不接受param为null的情况
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static String parse(String sql, Object... params) {
		if (params.length == 0) {
			return sql;
		}
		StringBuilder sb = new StringBuilder(sql);
		int index = -1;
		for (Object param : params) {
			index = sb.indexOf("?", index);
			if (param instanceof String) {
				sb.replace(index, index + 1, "'" + param + "'");
			} else if (param instanceof Integer || param instanceof Long
					|| param instanceof Double) {
				sb.replace(index, index + 1, String.valueOf(param));
			} else {
				continue;
			}
			index += param.toString().length();
		}
		return sb.toString();
	}
}
