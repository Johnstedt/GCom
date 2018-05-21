package communication;

import clock.Vector;
import group_management.CommunicationType;
import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

public class ReliableMultiCast extends Multicast implements Serializable, Observer {

	private List<Vector> clocks;
	private List<Vector> haveDelivered;

	public ReliableMultiCast(User u){
		super(u, CommunicationType.RELIABLE_MULTICAST);
		this.clocks = new LinkedList<>();
		this.haveDelivered = new LinkedList<>();
	}

	public void send(Message msg) {
		this.clocks.add((Vector) msg.getClock());
		super.toSender(msg);
	}

	@Override
	void receiveFromReceiver(Message msg) {
		Boolean shouldSend = true;
		Boolean shouldDeliver = true;

		System.out.println("MESSAGE IS: "+msg.getMsg());

		for (Vector c : this.clocks) {
			if (c.equalsQ(((Vector) msg.getClock()))) {
				shouldSend = false;
			}
		}
		if(shouldSend){
			System.out.println("Add clock, length: "+this.clocks.size());
			this.clocks.add((Vector) msg.getClock());
			sendToSender(msg);
		}else {
			for (Vector c : this.haveDelivered) {
				if (c.equalsQ(((Vector) msg.getClock()))) {
					shouldDeliver = false;
				}
			}
			if(shouldDeliver){
				System.out.println("WILL DELIVER");
				this.haveDelivered.add(((Vector) msg.getClock()));
				toGroupManagement(msg);
			} else {
				System.out.println("WILL NOT DELIVER");
			}
		}
	}

	@Override
	void sendToSender(Message msg) {
		toSender(msg);
	}


}