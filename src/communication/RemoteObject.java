package communication;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteObject extends Remote {

	public boolean sendMessage(String type, String msg) throws RemoteException;

}

