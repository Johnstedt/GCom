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
	private User u;

	public Causal(User u,Multicast comm) {
		super(comm, MessageOrderingType.CAUSAL);
		this.receiveClock = new Vector();
		this.u = u;
	}

	@Override
	public void send(Message msg) {

		if(msg.getType().equals(MessageType.INTERNAL)){
			System.out.println("IAM YOUR INTERNAL FATHER");
			this.u = msg.getFrom();
		}
		else {
			System.out.println("SENDING JOIN, I HAVE CLOCK BEFORE CLONE!: " + this.vectorClock.toString());
			this.receiveClock.increment(msg.getFrom());
			Vector v = this.receiveClock.getClone();
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
			
				if (super.vectorClock.nextInLine(m.getFrom(), (Vector) m.getClock())) {
					super.vectorClock.increment(m.getFrom());
					this.receiveClock.incrementEveryone(super.vectorClock);
					this.setChanged();
					notifyObservers(m);
					loopThroughQueue();
				} else {
					queue.add(m);
					System.out.println("NOT NEXT MESSAGE");
					System.out.println("MY-CLOCK: "+ super.vectorClock.toString());
					System.out.println("GOT-CLOCK: "+ m.getClock().toString());
			}
		}
	}

	private void loopThroughQueue() {

		for (Message m : queue){
			if(super.vectorClock.nextInLine(m.getFrom(),(Vector) m.getClock())){
				super.vectorClock.increment(m.getFrom());
				this.receiveClock.incrementEveryone(super.vectorClock);
				queue.remove(m);
				this.setChanged();
				notifyObservers(m);
				loopThroughQueue();
				break;
			}
		}
	}


}
