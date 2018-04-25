package communication;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Sender {
  public static void main(String args[]) {
    try {
      Registry registry = LocateRegistry.getRegistry("localhost");

      RemoteObject stub = (RemoteObject)registry.lookup("MessageService");

      System.out.println("Result: "+stub.printMessage("bingo"));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
