package communication;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Receiver {
  public static void main(String args[]) {
    try {
      RemoteObject impl = new RemoteObjectImpl();
      Registry registry = LocateRegistry.getRegistry();

      registry.rebind("MessageService", impl);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
