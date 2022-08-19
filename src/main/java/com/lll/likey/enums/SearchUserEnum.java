package com.lll.likey.enums;

//添加好友的判断枚举
public enum SearchUserEnum {
	
	SUCCESS(0, "OK"),
	USER_NOT_EXIST(1, "查 无 从 人"),
	NOT_YOURSELF(2, "不能添加用户本人"),
	ALREADY_FRIENDS(3, "TA已经是你的好友,无需添加");


	public final Integer status;
	public final String msg;
	
	SearchUserEnum(Integer status, String msg){
		this.status = status;
		this.msg = msg;
	}
}
