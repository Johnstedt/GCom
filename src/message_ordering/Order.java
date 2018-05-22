package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.MessageOrderingType;
import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Order extends Observable implements Serializable, Observer{
	public MessageOrderingType orderType;
	protected LinkedBlockingQueue<Message> queue;
	protected Multicast communicator;
	protected Vector vectorClock;

	Order(Multicast com, MessageOrderingType orderType) {
		this.queue = new LinkedBlockingQueue<>();
		this.orderType = orderType;
		this.communicator = com;
		this.communicator.addObserver(this);
		this.vectorClock = new Vector();
	}

	public abstract void send(Message msg);

	public abstract void removeStubs();

	public void removeObs(){
		this.deleteObservers();
	}

	public abstract void queueAdd(Message m);

	@Override
	public void update(Observable observable, Object o) {

		System.out.println("ORDER: RECEIVED AND UPDATE");

		if(o instanceof Message) {
			Message m = (Message) o;
			queueAdd(m);
		}
	}

	public void setSelf(User self) {
		this.communicator.setSelf(self);
	}
}
