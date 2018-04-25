package communication;

import message_ordering.Notify_Order;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Receiver implements Runnable {

	private Integer port;
	private Notify_Order notify_order;

	Receiver(Integer port, Notify_Order no){
		super();
		this.port = port;
		this.notify_order = no;
	}

	@Override
	public void run() {
		try {
			RemoteObject impl = new RemoteObjectImpl();
			impl.setOrderObservable(this.notify_order);
			Registry registry = LocateRegistry.createRegistry(port);

			registry.rebind("MessageService", impl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
