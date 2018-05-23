package message_ordering;

import clock.Vector;
import communication.Multicast;
import group_management.MessageOrderingType;
import message.Message;
import message.MessageType;

/**
 *  Causal ordering.
 *
 *  See pdf report in repo for info: https://github.com/Johnstedt/GCom
 */
public class Fifo extends Order {

	private Vector receiveClock;

	public Fifo(Multicast comm) {
		super(comm, MessageOrderingType.FIFO);
		this.receiveClock = new Vector();
	}

	/**
	 * Increments own vector in clock before send.
	 * @param msg to send.
	 */
	@Override
	public void send(Message msg) {

		if(!msg.getType().equals(MessageType.INTERNAL)){
			this.receiveClock.increment(msg.getFrom());
			Vector v = this.receiveClock.getClone();
			msg.setClock(v);
		}
		communicator.send(msg);
	}

	/**
	 * On receive check if next message in causal ordering.
	 * if not next add to queue,
	 * if next notify and call function to loop through old message in queue
	 * to see if they are next message in line.
	 * @param m received message
	 */
	@Override
	public void queueAdd(Message m) {
		if(!m.getType().equals(MessageType.INTERNAL)) {

			if (!super.vectorClock.getClock().containsKey(m.getFrom())) {
				super.vectorClock.increment(m.getFrom());
			}
			if (super.vectorClock.getClock().get(m.getFrom()).equals(    m.getClock().getClock().get(m.getFrom()) + 1L    ) ) {
				super.vectorClock.increment(m.getFrom());
				this.receiveClock.incrementEveryone(super.vectorClock);
				this.setChanged();
				notifyObservers(m);
				loopThroughQueue();
			} else {
				queue.add(m);
			}
		}
	}

	/**
	 * Loops through queue to see if next in order.
	 */
	private void loopThroughQueue() {

		for (Message m : queue){
			if (super.vectorClock.getClock().get(m.getFrom()).equals(    m.getClock().getClock().get(m.getFrom()) + 1L    ) ) {
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