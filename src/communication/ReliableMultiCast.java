package communication;

import clock.Vector;
import group_management.CommunicationType;
import group_management.User;
import message.Message;
import message.MessageType;

import java.io.Serializable;
import java.util.*;

/**
 * Implements Reliable Multicast.
 * Multicast received messages first time it's received.
 * Delivers the second time it receives the message and ignores after.
 */
public class ReliableMultiCast extends Multicast implements Serializable, Observer {

	private List<Vector> clocks;
	private List<Vector> haveDelivered;
	private List<User> group;

	public ReliableMultiCast(User u){
		super(u, CommunicationType.RELIABLE_MULTICAST);
		this.clocks = new LinkedList<>();
		this.haveDelivered = new LinkedList<>();
		this.group = new LinkedList<>();
	}

	public void send(Message msg) {

		if(msg.getType().equals(MessageType.INTERNAL)){
			this.group.add(msg.getFrom());
		}

		this.clocks.add((Vector) msg.getClock());
		super.toSender(msg);
	}

	@Override
	void receiveFromReceiver(Message msg) {
		Boolean shouldSend = true;
		Boolean shouldDeliver = true;

		for (Vector c : this.clocks) {
			if (c.equalsQ(((Vector) msg.getClock()))) {
				shouldSend = false;
			}
		}
		if(shouldSend){
			this.clocks.add((Vector) msg.getClock());
			sendToSender(msg);
		}else {
			for (Vector c : this.haveDelivered) {
				if (c.equalsQ(((Vector) msg.getClock()))) {
					shouldDeliver = false;
				}
			}
			if(shouldDeliver){
				this.haveDelivered.add(((Vector) msg.getClock()));
				toGroupManagement(msg);
			}
		}
	}

	@Override
	void sendToSender(Message msg) {
		msg.setSendTo(new ArrayList<>(this.group));
		toSender(msg);
	}

	@Override
	public final void update(Observable observable, Object o) {
		if (o instanceof Message) {
			Message msg = (Message) o;
			if(msg.getType().equals(MessageType.JOIN)){
				if(!this.group.contains(msg.getFrom())) {
					this.group.add(msg.getFrom());
				}
			}
			super.fromReceiverBeforeDebugger.add(msg);
		}
	}
}