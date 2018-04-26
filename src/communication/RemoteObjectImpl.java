package communication;


import message_ordering.Notify_Order;

import java.rmi.*;
import java.rmi.server.*;


public class RemoteObjectImpl extends UnicastRemoteObject implements RemoteObject {

	private Notify_Order notify_order;

	public RemoteObjectImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean sendMessage(String type, String msg) {

		switch (type){

			case "msg":
				this.notify_order.hello();
				this.notify_order.notifyObservers(msg);
				break;

			case "askGroup":
				System.out.println("yaaay");
				break;
		}

		return true;
	}

	@Override
	public void setOrderObservable(Notify_Order no) {

		this.notify_order =  no;
	}

}
