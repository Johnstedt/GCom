package message_ordering;

import clock.Vector;
import communication.Multicast;
import message.Message;
import message.MessageType;

import static group_management.MessageOrderingType.UNORDERED;

public class Unordered extends Order {

	public Unordered(Multicast m){
		super( m, UNORDERED);

	}

	@Override
	public void send(Message msg) {
		Vector v = null;
		if(!msg.getType().equals(MessageType.INTERNAL)) {
			this.vectorClock.increment(msg.getFrom());
		}
		v = (Vector) this.vectorClock.getClone();
		msg.setClock(v);
		communicator.send(msg);
	}

	 public void queueAdd(Message m){
		this.vectorClock.incrementEveryone((Vector) m.getClock());
		this.setChanged();
		queue.add(m);
		notifyObservers(queue.remove());
	}


}
