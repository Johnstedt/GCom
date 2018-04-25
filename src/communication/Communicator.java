package communication;

import group_management.User;
import message_ordering.Notify_Order;

import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Communicator {

	Receiver r;
	Sender s;


	public Communicator(User u, Integer connectPort, Notify_Order notify_order){

		this.r = new Receiver(u.getPort(), notify_order);
		r.run();

		this.s = new Sender(u.getNickname());
		s.addToGroup(u.getPort());
		s.addToGroup(connectPort);


	}

	public void send(String msg) {
		s.send(msg);
	}
}
