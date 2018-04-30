package communication;


import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.rmi.*;
import java.rmi.server.*;
import java.util.HashMap;


public class RemoteObjectImpl extends UnicastRemoteObject implements RemoteObject {

	private Notify_Order notify_order;

	public RemoteObjectImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean sendMessage(Message msg) {
		this.notify_order.hello();
		this.notify_order.notifyObservers(msg);

		return true;
	}

	public boolean askGroup(Group g){
		System.out.println("asked for groups");
		this.notify_order.hello();
		this.notify_order.notifyObservers(g);
		return true;
	}

	@Override
	public void sendGroups(HashMap<String, Group> hm) {
		this.notify_order.hello();
		this.notify_order.notifyObservers(hm);
	}

	@Override
	public void join(User u) throws RemoteException {
		this.notify_order.hello();
		this.notify_order.notifyObservers(u);
	}

	@Override
	public void setOrderObservable(Notify_Order no) {

		this.notify_order =  no;
	}



}
