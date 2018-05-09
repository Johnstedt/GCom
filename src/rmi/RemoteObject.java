package rmi;

import message.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteObject extends Remote {
	boolean transferMessage(Message msg) throws RemoteException;
	void setOrderObservable(Receiver no) throws RemoteException;
}

