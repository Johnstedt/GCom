package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.User;
import message.Message;

import static group_management.MessageOrderingType.UNORDERED;

public class Unordered extends Order {

	public Unordered(User u, Multicast m){
		super(u, m, UNORDERED);

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

	 public void queueAdd(Message m){
		this.vectorClock.incrementEveryone((Vector) m.getClock());
		this.setChanged();
		queue.add(m);
		notifyObservers(queue.remove());
	}

	@Override
	public void removeStubs() {
		this.communicator.removeStubs();
	}
}
