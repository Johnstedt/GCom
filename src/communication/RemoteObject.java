package communication;

import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface RemoteObject extends Remote {

	public boolean sendMessage(String s, Message msg) throws RemoteException;

	public void setOrderObservable(Receiver no) throws RemoteException;

	boolean askGroup(String s, Group g) throws RemoteException;

	void sendGroups(String s, HashMap<String, Group> hm) throws RemoteException;

	void join(String s, User u) throws RemoteException;
}

