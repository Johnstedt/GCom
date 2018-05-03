package communication;

import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Unreliable_Multicast implements Serializable{

	private Sender s;

	public Unreliable_Multicast(User u, Notify_Order r){

		//this.r = new Receiver(u.getPort(), notify_order);
		//r.run();

		this.s = new Sender(u.getNickname());
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
}
