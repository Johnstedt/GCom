package message_ordering;

import clock.Clock;

import java.io.Serializable;

public class Message implements Serializable {
	private String msg;
	private Clock cl;

	Message(String msg, Clock cl){
		this.msg = msg;
		this.cl = cl;
	}
	public Clock getCl() {
		return cl;
	}
	public String getMsg() {
		return msg;
	}
}