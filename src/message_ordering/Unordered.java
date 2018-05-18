package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.CommunicationType;
import group_management.User;
import message.Message;

import java.util.Observable;

public class Unordered extends Order {


	private Vector vectorClock;

	public Unordered(User u, Multicast m){
		super(u, m);
		this.vectorClock = new Vector();
	}

	@Override
	public void send(Message msg) {

		Vector v = null;
		try {
			//TODO: Will always be from "above/client", therefore msg.getFrom() is self?
			this.vectorClock.increment(msg.getFrom());
			v = (Vector) this.vectorClock.getClone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("WHY U NOT CLONE!?");
		}

		msg.setClock(v);
		communicator.send(msg);
	}

	@Override
	public void update(Observable observable, Object o) {

		System.out.println("ORDER: RECEIVED AND UPDATE");

		if(o instanceof Message) {
			Message m = (Message) o;
			this.vectorClock.incrementEveryone((Vector) m.getClock());
			queueAdd(m);
		}
	}

	private void queueAdd(Message m){
		this.setChanged();
		queue.add(m);
		notifyObservers(queue.remove());
	}

	@Override
	public void removeStubs() {
		this.communicator.removeStubs();
	}
}
