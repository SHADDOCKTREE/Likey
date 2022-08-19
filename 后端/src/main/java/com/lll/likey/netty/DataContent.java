package com.lll.likey.netty;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataContent implements Serializable {

	private static final long serialVersionUID = 8021381444738260454L;

	private Integer action;
	private ChatMsg chatMsg;
	private String extand;  //拓展字段, 代表要签收的消息id,逗号间隔

}
