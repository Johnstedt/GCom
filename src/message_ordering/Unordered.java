package message_ordering;

import clock.Vector;
import communication.Unreliable_Multicast;
import group_management.Group;
import group_management.User;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Unordered extends Order implements Observer {

	private Queue<Message> queue;
	private Unreliable_Multicast communicator;

	public Unordered(User u){

		this.queue = new LinkedBlockingQueue<>();

		Notify_Order no = new Notify_Order();
		no.addObserver(this);
		this.communicator = new Unreliable_Multicast(u, no);
	}

	@Override
	public void send(List<User> ul, String msg) {

		Message m = new Message(msg, new Vector());
		communicator.send(ul, m);
	}

	@Override
	public void receive(String msg) {
		Message m = new Message(msg, null);
		queue.add(m);
	}

	@Override
	public void askGroups(User u, String groupName, Group g) {
		this.communicator.askGroup(u, groupName, g);
	}

	@Override
	public void update(Observable observable, Object o) {

		System.out.println("HEY");

		if(o instanceof Message) {
			Message m = (Message) o;
			queue.add(m);
			sort();
		}
		else if (o instanceof HashMap){
			HashMap hm = (HashMap)o;
			this.setChanged();
			notifyObservers(hm);
		}
		else if(o instanceof Group) {
			System.out.println("GROUP ASKED");
			this.setChanged();
			notifyObservers(o);
		}
	}

	private void sort(){
		Message m = queue.remove();
		notifyObservers(m);
	}

	public void sendGroups(List<User> users, HashMap<String, Group> hm){
		this.communicator.sendGroups(users, hm);
	}
}
