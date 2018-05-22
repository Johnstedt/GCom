package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.MessageOrderingType;
import group_management.User;
import message.Message;
import message.MessageType;

public class Causal extends Order {

	public Causal(Multicast comm) {
		super(comm, MessageOrderingType.CAUSAL);
	}

	@Override
	public void send(Message msg) {

		System.out.println(this.vectorClock.toString());

		Vector v = this.vectorClock.getClone();
		v.increment(msg.getFrom());

		msg.setClock(v);
		communicator.send(msg);
	}

	@Override
	public void removeStubs() {

	}

	@Override
	public void queueAdd(Message m) {

		this.setChanged();
		queue.add(m);

		if(m.getType() == MessageType.JOIN){
			System.out.println("I GOT JOIN IN CAUSAL");
			System.out.println(this.vectorClock.toString());
		}

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
