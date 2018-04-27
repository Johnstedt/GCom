package communication;

import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Unreliable_Multicast implements Serializable{

	private Receiver r;
	private Sender s;


	public Unreliable_Multicast(User u, Notify_Order notify_order){

		this.r = new Receiver(u.getPort(), notify_order);
		r.run();

		this.s = new Sender(u.getNickname());
	}

	public void send(List<User> ul, Message msg) {
		s.send(ul, msg);
	}

	public void askGroup(User u, String groupName, Group g) {
		s.askGroup(u, groupName, g);
	}

	public void sendGroups(List<User> users, HashMap<String, Group> hm) {
		this.s.sendGroups(users, hm);
	}
}
