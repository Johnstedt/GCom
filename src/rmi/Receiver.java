package rmi;

import message.Message;
import message.MessageType;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class Receiver extends Observable implements Serializable {

	private Integer port;
	private HashMap<String, Observer> communicationLayer;

	public Receiver(Integer port){
		super();
		this.port = port;
		this.communicationLayer = new HashMap<>();
		System.err.println("Start Reciever thread ERROR");
		new Thread(this::run).start();
		System.err.println("done start Reciever thread (remove this output, it works IGNORE ABOVE)!");
	}

	public void addOrder(Observer no, String groupName){
		System.out.println("THIS WILL NOTIFY ");
		if (this.communicationLayer.containsKey(groupName)) {
			System.out.println("DOES IT");
			this.communicationLayer.remove(groupName);
		}
		this.communicationLayer.put(groupName, no);
	}

	public void notifyObservers(Message msg) {
		System.out.println("RECEIVER IS SENDING: "+ msg.getGroupName());
		if (msg.getType().equals(MessageType.INTERNAL_SET_NEW_RECEIVER)) {
			System.err.println("Receiver got internal message");
		}
		if(this.communicationLayer.containsKey(msg.getGroupName())){
			// Go around Java implemented observable to get specific observer.
			Observer o = this.communicationLayer.get(msg.getGroupName());
			o.update(this, msg);
		} else {
			System.err.println("Receiver.notifyObserver ELSE - This should not run! FIX!" + msg.getGroupName());
			//this.communicationLayer.entrySet().iterator().next().getValue().changeAndNotifyObservers();
		}
	}

	private void run() {
		try {
			RemoteObject impl = new RemoteObjectImpl();
			impl.setOrderObservable(this);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind("MessageService", impl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
