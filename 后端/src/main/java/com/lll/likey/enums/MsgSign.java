package com.lll.likey.enums;

public enum MsgSign {
	
	unsign(0, "未签收"),
	signed(1, "已签收");	
	
	public final Integer type;
	public final String content;
	
	MsgSign(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}
