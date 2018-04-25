package communication;

import java.rmi.*;
import java.rmi.server.*;
import java.util.Observable;


public class RemoteObjectImpl extends UnicastRemoteObject implements RemoteObject {

	public RemoteObjectImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean sendMessage(String type, String msg) {

		// DO a switchcase based on type, notify observers?

		switch (type){

			case "msg":

				System.out.println(msg);
				break;

			case "askGroup":
				System.out.println("yaaay");
				break;
		}


		return true;
	}

}
