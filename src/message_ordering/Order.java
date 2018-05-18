package message_ordering;

import communication.Multicast;
import communication.ReliableMultiCast;
import communication.TreeMulticast;
import communication.Unreliable_Multicast;
import group_management.CommunicationType;
import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Order extends Observable implements Serializable, Observer{

	protected LinkedBlockingQueue<Message> queue;
	protected Multicast communicator;

	Order(User u, Multicast com) {
		this.queue = new LinkedBlockingQueue<>();

		this.communicator = com;
		this.communicator.addObserver(this);
	}

	public abstract void send(Message msg);

	public abstract void removeStubs();

	public void removeObs(){
		this.deleteObservers();
	}

	@Override
	public void update(Observable observable, Object o) {


	}
}
