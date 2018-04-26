package communication;

import group_management.User;
import message_ordering.Notify_Order;

import java.util.List;

public class Unreliable_Multicast {

	private Receiver r;
	private Sender s;


	public Unreliable_Multicast(User u, Notify_Order notify_order){

		this.r = new Receiver(u.getPort(), notify_order);
		r.run();

		this.s = new Sender(u.getNickname());
	}

	public void send(List<User> ul, String msg) {
		s.send(ul, msg);
	}
}
