package com.shuheng.weixin.entity;

import java.io.Serializable;

public class VoiceMessage extends BaseMessage implements Serializable {

	private static final long serialVersionUID = 4763903717622217014L;
	
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}

	

	
	
	
	
}
