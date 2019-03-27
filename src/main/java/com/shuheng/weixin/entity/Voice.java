package com.shuheng.weixin.entity;

public class Voice {
	private String MediaId;

	public Voice(String mediaId) {
		this.setMediaId(mediaId);
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	
}
