package com.shuheng.weixin.entity;

import java.io.Serializable;

public class TextMessage extends BaseMessage implements Serializable {

	private static final long serialVersionUID = 7907613348683936613L;

	private String Content;       //文本消息内容
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
}
