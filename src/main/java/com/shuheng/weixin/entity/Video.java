package com.shuheng.weixin.entity;

public class Video {
	
	private String MediaId;
	private String Title;
	private String Description;
	public Video(String mediaId, String title, String description) {
		setMediaId(mediaId);
		setTitle(title);
		setDescription(description);
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	
	
}
