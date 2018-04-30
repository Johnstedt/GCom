package communication;

import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;


public interface RemoteObject extends Remote {

	public boolean sendMessage(Message msg) throws RemoteException;

	public void setOrderObservable(Notify_Order no) throws RemoteException;

	boolean askGroup(Group g) throws RemoteException;

	void sendGroups(HashMap<String, Group> hm) throws RemoteException;

	void join(User u) throws RemoteException;
}

