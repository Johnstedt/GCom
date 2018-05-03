package communication;

import message_ordering.Notify_Order;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Receiver implements Runnable, Serializable {

	private Integer port;
	private HashMap<String, Notify_Order> notify_orders;

	public Receiver(Integer port){
		super();
		this.port = port;
		this.notify_orders = new HashMap<>();

	}

	public void addOrder(Notify_Order no, String GroupName){

		this.notify_orders.put(GroupName, no);
	}

	@Override
	public void run() {
		try {

			RemoteObject impl = new RemoteObjectImpl();
			impl.setOrderObservable(this);
			Registry registry = LocateRegistry.createRegistry(port);

			registry.rebind("MessageService", impl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void notifyObservers(String group, Object o) {
		if(this.notify_orders.containsKey(group)){

			this.notify_orders.get(group).hello();
			this.notify_orders.get(group).notifyObservers(o);
		} else {

			this.notify_orders.entrySet().iterator().next().getValue().hello();
			this.notify_orders.entrySet().iterator().next().getValue().notifyObservers(o);
		}

	}


}
