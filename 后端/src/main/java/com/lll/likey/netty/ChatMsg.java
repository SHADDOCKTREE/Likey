package com.lll.likey.netty;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMsg implements Serializable {

	private static final long serialVersionUID = 3611169682695799175L;
	
	private String senderId;
	private String receiverId;
	private String msg;
	private String msgId;
	
}
