package message_ordering;

import clock.Clock;
import clock.Vector;
import group_management.User;

import java.io.Serializable;

public class Message implements Serializable {
	private String msg;
	private Vector cl;
	private User from;

	Message(String msg, Vector cl, User from){
		this.msg = msg;
		this.cl = cl;
		this.from = from;
	}
	public Clock getCl() {
		return cl;
	}
	public String getMsg() {
		return msg;
	}

	public User getFrom() {
		return from;
	}
}