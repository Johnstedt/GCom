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
	private Vector vectorClock;

	public Unordered(User u){

		this.queue = new LinkedBlockingQueue<>();
		this.vectorClock = new Vector();

		Notify_Order no = new Notify_Order();
		no.addObserver(this);
		this.communicator = new Unreliable_Multicast(u, no);
	}

	@Override
	public void send(List<User> ul, String msg, User self) {

		Vector v = null;
		try {
			this.vectorClock.increment(self);
			v = (Vector) this.vectorClock.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("WHY U NOT CLONE!?");
		}

		Message m = new Message(msg, v, self);
		communicator.send(ul, m);
	}

	@Override
	public void receive(String msg) {

	}

	@Override
	public void askGroups(User u, String groupName, Group g) {
		this.communicator.askGroup(u, groupName, g);
	}

	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) {
			Message m = (Message) o;
			this.vectorClock.incrementEveryone((Vector) m.getCl());
			queue.add(m);
			this.setChanged();
			sort();
		}
		else if (o instanceof HashMap){
			HashMap hm = (HashMap)o;
			this.setChanged();
			notifyObservers(hm);
		}
		else if(o instanceof Group) {

			this.setChanged();
			notifyObservers(o);
		}
		else if (o instanceof User){

			User u = (User)o;
			Message m = new Message(u.getNickname()+" joined the group!", new Vector(), u);
			queue.add(m);
			this.setChanged();
			sort();
			this.setChanged();
			notifyObservers(u);
		}
	}

	private void sort(){
		Message m = queue.remove();
		notifyObservers(m);
	}

	public void sendGroups(List<User> users, HashMap<String, Group> hm){
		this.communicator.sendGroups(users, hm);
	}

	@Override
	public void join(List<User> users, User u) {
		this.communicator.join(users, u);
	}

	@Override
	public void removeStubs() {
		this.communicator.removeStubs();
	}
}
