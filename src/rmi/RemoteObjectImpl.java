package rmi;


import message.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class RemoteObjectImpl extends UnicastRemoteObject implements RemoteObject {
	private Receiver notify_order;

	RemoteObjectImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean transferMessage(Message msg) {
		this.notify_order.notifyObservers(msg);
		return true;
	}

	@Override
	public void setOrderObservable(Receiver no) {
		this.notify_order =  no;
	}
}
