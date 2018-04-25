package communication;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Receiver implements Runnable {

	private Integer port;

	Receiver(Integer port){
		super();
		this.port = port;
	}

	@Override
	public void run() {
		try {
			RemoteObject impl = new RemoteObjectImpl();
			Registry registry = LocateRegistry.createRegistry(port);

			registry.rebind("MessageService", impl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
