package message;

import clock.Clock;
import clock.Vector;
import group_management.User;

import java.io.Serializable;
import java.util.List;

public abstract class Message implements Serializable {
	private MessageType type;
	private String groupName;
	private Vector clock;
	private User from;
	private Object msg;
	private List<User> sendTo;


	Message(MessageType type, String groupName, Vector clock, User from, List<User> sendTo, Object msg){
		this.type = type;
		this.groupName = groupName;
		this.msg = msg;
		this.clock = clock;
		this.from = from;
		this.sendTo = sendTo;
	}

	public Clock getClock() {
		return clock;
	}

	public Object getMsg() {
		return msg;
	}

	public User getFrom() {
		return from;
	}

	public MessageType getType() {
		return type;
	}

	public String getGroupName() {
		return groupName;
	}

	public List<User> getSendTo() {
		return sendTo;
	}

	public void setClock(Vector clock) {
		this.clock = clock;
	}

}