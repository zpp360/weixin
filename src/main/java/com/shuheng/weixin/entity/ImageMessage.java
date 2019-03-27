package com.shuheng.weixin.entity;

import java.io.Serializable;

public class ImageMessage extends BaseMessage implements Serializable {
	
	private static final long serialVersionUID = 3345939474919598723L;
	
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}

	
	
}
