package communication;

import clock.Vector;
import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ReliableMultiCast extends Multicast implements Serializable, Observer {

	private List<Vector> clocks;
	private List<Vector> haveDelivered;

	public ReliableMultiCast(User u){
		super(u);
		this.clocks = new LinkedList<>();
		this.haveDelivered = new LinkedList<>();
	}

	public void send(Message msg) {
		this.clocks.add((Vector) msg.getClock());
		super.send(msg);
	}


	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) { // Check clock for join also later
			Message msg = (Message) o;
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
				sender.send(msg);
			}else {
				for (Vector c : this.haveDelivered) {
					if (c.equalsQ(((Vector) msg.getClock()))) {
						shouldDeliver = false;
					}
				}
				if(shouldDeliver){
					System.out.println("WILL DELIVER");
					this.haveDelivered.add(((Vector) msg.getClock()));
					super.update(observable, o);
				} else {
					System.out.println("WILL NOT DELIVER");
				}
			}
		}
	}
}