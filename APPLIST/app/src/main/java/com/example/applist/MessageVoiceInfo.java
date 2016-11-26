package com.example.applist;

public class MessageVoiceInfo {
	
	public  boolean state;
	public String source;
	public String time;
	
	
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "MessageVoiceInfo [state=" + state + ", source=" + source
				+ ", time=" + time + "]";
	}
	
}
