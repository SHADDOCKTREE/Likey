package com.lll.likey.enums;


public enum MsgType {
	
	CONNECT(1, "首次连接"),
	CHAT(2, "聊天消息"),	
	SIGNED(3, "消息签收"),
	KEEPALIVE(4, "客户端保持心跳"),
	PULL_FRIEND(5, "拉取好友");
	
	public final Integer type;
	public final String content;
	
	MsgType(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}
