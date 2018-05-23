package rmi;

import message.Message;
import message.MessageType;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Receiver extends Observable implements Serializable {

	private Integer port;
	private HashMap<String, Observer> communicationLayer;

	public Receiver(Integer port) throws RemoteException {
		super();
		this.port = port;
		this.communicationLayer = new HashMap<>();
		RemoteObject impl = new RemoteObjectImpl();
		impl.setOrderObservable(this);
		Registry registry = LocateRegistry.createRegistry(port);
		registry.rebind("MessageService", impl);

	}

	public void addOrder(Observer no, String groupName){

		if (this.communicationLayer.containsKey(groupName)) {
			this.communicationLayer.remove(groupName);
		}
		this.communicationLayer.put(groupName, no);
	}

	public void notifyObservers(Message msg) {

		if(this.communicationLayer.containsKey(msg.getGroupName())){
			// Go around Java implemented observable to get specific observer.
			Observer o = this.communicationLayer.get(msg.getGroupName());
			o.update(this, msg);
		}
	}

}
