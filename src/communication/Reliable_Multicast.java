package communication;

import clock.Clock;
import clock.Vector;
import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.io.Serializable;
import java.util.*;

public class Reliable_Multicast extends Multicast implements Serializable, Observer {

	private final String gn;
	private Sender s;
	private List<Vector> clocks;
	private List<Vector> sent;

	public Reliable_Multicast(User u, String gn){

		this.s = new Sender(u);
		this.clocks = new LinkedList<>();
		this.sent = new LinkedList<>();
		this.gn = gn;
	}

	public Notify_Order getListener(){
		Notify_Order no = new Notify_Order();
		no.addObserver(this);

		return no;
	}

	public void send(String gn, List<User> ul, Message msg) {
		this.clocks.add((Vector) msg.getCl());
		s.send(gn, ul, msg);
	}

	public void askGroup(User u, String groupName, Group g) {
		s.askGroup(u, groupName, g);
	}

	public void sendGroups(String gn, List<User> users, HashMap<String, Group> hm) {
		this.s.sendGroups(gn, users, hm);
	}

	public void join(String gn, List<User> users, User u) {
		this.s.join(gn, users, u);
	}

	public void removeStubs() {
		this.s.remove();
	}

	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) { // Check clock for join also later

			Boolean shouldSend = true;
			Boolean shouldDeliver = true;

			System.out.println("MESSAGE IS: "+((Message)o).getMsg());

			for (Vector c : this.clocks) {

				if (c.equalsQ(((Vector) ((Message) o).getCl()))) {
					shouldSend = false;
				}
			}
			if(shouldSend){
				System.out.println("Add clock, length: "+this.clocks.size());
				this.clocks.add((Vector) ((Message)o).getCl());
				s.broadcast((Message) o, gn);
			}else {
				for (Vector c : this.sent) {
					if (c.equalsQ(((Vector) ((Message) o).getCl()))) {
						shouldDeliver = false;
					}
				}
				if(shouldDeliver){
					System.out.println("WILL DELIVER");
					this.sent.add(((Vector) ((Message) o).getCl()));
					this.setChanged();
					this.notifyObservers(o);
				}else {
					System.out.println("WILL NOT DELIVER");
				}
			}
		} else {
			this.setChanged();
			this.notifyObservers(o);
		}
	}
}