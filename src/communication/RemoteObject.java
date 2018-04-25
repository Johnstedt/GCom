package communication;

import message_ordering.Notify_Order;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteObject extends Remote {

	public boolean sendMessage(String type, String msg) throws RemoteException;

	public void setOrderObservable(Notify_Order no) throws RemoteException;
}

