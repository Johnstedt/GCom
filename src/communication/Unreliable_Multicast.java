package communication;

import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Unreliable_Multicast extends Multicast implements Serializable, Observer {

	private Sender s;

	public Unreliable_Multicast(User u){

		this.s = new Sender(u.getNickname());
	}

	public Notify_Order getListener(){
		Notify_Order no = new Notify_Order();
		no.addObserver(this);

		return no;
	}

	public void send(String gn, List<User> ul, Message msg) {
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
		this.setChanged();
		this.notifyObservers(o);

	}
}