package com.shuheng.weixin.entity;

public class Image {
	private String MediaId;

	public Image(String mediaId) {
		this.setMediaId(mediaId);
	}

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	
}
