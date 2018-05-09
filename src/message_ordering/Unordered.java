package message_ordering;

import clock.Vector;
import communication.*;
import group_management.CommunicationType;
import group_management.Group;
import group_management.User;
import message.Message;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Unordered extends Order {


	private Vector vectorClock;

	public Unordered(User u, CommunicationType ct, String gn){
		super(u, ct, gn);
		this.vectorClock = new Vector();
	}

	@Override
	public void send(String gn, List<User> ul, String msg, User self) {

		Vector v = null;
		try {
			this.vectorClock.increment(self);
			v = (Vector) this.vectorClock.getClone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("WHY U NOT CLONE!?");
		}

		Message m = new Message(msg, v, self);
		communicator.send(gn, ul, m);
	}

	@Override
	public void receive(String msg) {

	}

	@Override
	public void rebindObserver() {
		this.communicator.addObserver(this);
	}

	@Override
	public void askGroups(User u, String groupName, Group g) {

		this.communicator.askGroup(u, groupName, g);
		System.out.println("I WILL ASK FOR GROUPS IN ORDER");
	}

	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) {
			Message m = (Message) o;

			this.vectorClock.incrementEveryone((Vector) m.getClock());

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

	public void sendGroups(String gn, List<User> users, HashMap<String, Group> hm){
		System.out.println("I WILL SEND TO GROUPS IN UNORDERED");
		this.communicator.sendGroups(gn, users, hm);
	}

	@Override
	public void join(String gn, List<User> users, User u) {
		this.communicator.join(gn, users, u);
	}

	@Override
	public void removeStubs() {
		this.communicator.removeStubs();
	}
}
