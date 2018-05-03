package communication;

import clock.Clock;
import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.io.Serializable;
import java.util.*;

public class Reliable_Multicast extends Multicast implements Serializable, Observer {

	private final String gn;
	private Sender s;
	private List<Clock> clocks;

	public Reliable_Multicast(User u, String gn){

		this.s = new Sender(u.getNickname());
		this.clocks = new LinkedList<>();
		this.gn = gn;

	}

	public Notify_Order getListener(){
		Notify_Order no = new Notify_Order();
		no.addObserver(this);

		return no;
	}

	public void send(String gn, List<User> ul, Message msg) {
		this.clocks.add(msg.getCl());
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

		Boolean shouldSend = false;
		if(o instanceof Message) {
			for (Clock c : clocks) {
				if (c.equals(((Message) o).getCl())) {

					clocks.remove(c);
					this.setChanged();
					this.notifyObservers(o);

				} else {
					shouldSend = true;
				}
			}
			if(shouldSend){
				clocks.add(((Message)o).getCl());
				s.broadcast((Message) o, gn);
			}
		}
	}
}