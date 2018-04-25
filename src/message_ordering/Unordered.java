package message_ordering;

import clock.Clock;
import communication.Communicator;
import group_management.User;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

class Message{
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

public class Unordered implements Order{

	private Queue<Message> queue;
	private Communicator communicator;

	public Unordered(User u, Integer p){
		this.queue = new SynchronousQueue<>();
		this.communicator = new Communicator(u, p);
	}

	@Override
	public void send(String msg) {
		communicator.send(msg);

	}

	@Override
	public void receive(String msg) {
		Message m = new Message(msg, null);
		queue.add(m);
	}
}
