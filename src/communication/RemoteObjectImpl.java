package communication;


import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;


public class RemoteObjectImpl extends UnicastRemoteObject implements RemoteObject {

	private Receiver notify_order;

	public RemoteObjectImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean sendMessage(String group, Message msg) {

		this.notify_order.notifyObservers(group, msg);

		return true;
	}

	public boolean askGroup(String group, Group g){
		System.out.println("asked for groups");

		this.notify_order.notifyObservers(group, g);
		return true;
	}

	@Override
	public void sendGroups(String group, HashMap<String, Group> hm) {
		System.out.println("received groups");
		this.notify_order.notifyObservers(group, hm);
	}

	@Override
	public void join(String group, User u) throws RemoteException {

		this.notify_order.notifyObservers(group, u);
	}

	@Override
	public void setOrderObservable(Receiver no) {

		this.notify_order =  no;
	}



}
