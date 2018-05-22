package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.MessageOrderingType;
import group_management.User;
import message.Message;
import message.MessageType;

import java.util.Queue;

public class Causal extends Order {

	private Vector receiveClock;

	public Causal(Multicast comm) {
		super(comm, MessageOrderingType.CAUSAL);
		this.receiveClock = new Vector();
	}

	@Override
	public void send(Message msg) {

		if(msg.getType().equals(MessageType.INTERNAL)){
			System.out.println("IAM YOUR INTERNAL FATHER");
		}
		else {
			System.out.println("SENDING JOIN, I HAVE CLOCK BEFORE CLONE!: " + this.vectorClock.toString());
			this.vectorClock.increment(msg.getFrom());
			Vector v = this.vectorClock.getClone();
			System.out.println("SENDING JOIN, I HAVE CLOCK AFTER CLONE!: " + this.vectorClock.toString());

			msg.setClock(v);
		}
		communicator.send(msg);
	}

	@Override
	public void removeStubs() {

	}

	@Override
	public void queueAdd(Message m) {
		if(!m.getType().equals(MessageType.INTERNAL)) {

			if (m.getType() == MessageType.JOIN) {
				System.out.println("I GOT JOIN IN CAUSAL");
				System.out.println("RECEIVED JOIN: " + this.receiveClock.toString());
			}

			if (this.receiveClock.nextInLine(m.getFrom(), (Vector) m.getClock())) {
				this.receiveClock.incrementEveryone((Vector) m.getClock());
				super.vectorClock.incrementEveryone((Vector) m.getClock());
				this.setChanged();
				notifyObservers(m);
				loopThroughQueue();
			} else {
				queue.add(m);
				System.out.println("NOT NEXT MESSAGE");
			}
		}
	}

	private void loopThroughQueue() {

		for (Message m : queue){
			if(this.receiveClock.nextInLine(m.getFrom(),(Vector)m.getClock())){
				super.vectorClock.incrementEveryone((Vector) m.getClock());
				this.receiveClock.incrementEveryone((Vector) m.getClock());
				queue.remove(m);
				this.setChanged();
				notifyObservers(m);
				loopThroughQueue();
				break;
			}
		}
	}


}
