package communication;

import message_ordering.Notify_Order;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Sender {

	private List<RemoteObject> stub; // make list later
	private String nickname;


    Sender(String nick) {

            this.nickname = nick;
            this.stub = new LinkedList<>();
    }

    public void addToGroup(Integer port){

    	Registry registry;
	    while(true) {
		    try {
			    sleep(1000);
			    registry = LocateRegistry.getRegistry(port);

			    RemoteObject ro = (RemoteObject)registry.lookup("MessageService");

			    this.stub.add(ro);
			    System.out.println("found connection!");
			    break;
		    } catch (Exception e) {
			    System.out.println(port);
			    //Wait til connect
			    //e.printStackTrace();
		    }
		    System.out.println("Can't connect, trying again...");

	    }
    }

	public void send(String msg){
	    try {
	    	for (RemoteObject ro : stub){
	    		ro.sendMessage("msg", nickname +": " +msg);
		    }
	    } catch (RemoteException e) {
		    e.printStackTrace();
	    }
    }

}
