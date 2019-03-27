package com.shuheng.weixin.entity;

import java.io.Serializable;

public class VideoMessage extends BaseMessage implements Serializable {

	private static final long serialVersionUID = 7448790391046077686L;
	
	private Video Video;

	public Video getVideo() {
		return Video;
	}

	public void setVideo(Video video) {
		Video = video;
	}
	
	

}
