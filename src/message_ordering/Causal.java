package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.MessageOrderingType;
import group_management.User;
import message.Message;

public class Causal extends Order {

	public Causal(User u, Multicast comm) {
		super(u, comm, MessageOrderingType.CAUSAL);
	}

	@Override
	public void send(Message msg) {

	}

	@Override
	public void removeStubs() {

	}

	@Override
	public void queueAdd(Message m) {

		this.setChanged();
		queue.add(m);

		if(super.vectorClock.nextInLine(m.getFrom(),(Vector)m.getClock())){
			super.vectorClock.incrementEveryone((Vector) m.getClock());
			notifyObservers(queue.remove());
			loopThroughQueue();
		}
	}

	private void loopThroughQueue() {

		for (Message m : queue){
			if(super.vectorClock.nextInLine(m.getFrom(),(Vector)m.getClock())){
				super.vectorClock.incrementEveryone((Vector) m.getClock());
				notifyObservers(queue.remove());
				loopThroughQueue();
				break;
			}
		}
	}
}
