package communication;

import group_management.User;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

public class Sender {

	private HashMap<String, RemoteObject> stub; // make list later
	private String nickname;


    Sender(String nick) {

            this.nickname = nick;
            this.stub = new HashMap<>();
    }

    void addToGroup(String name, Integer port){

    	Registry registry;
	    while(true) {
		    try {
			    sleep(1000);
			    registry = LocateRegistry.getRegistry(port);

			    RemoteObject ro = (RemoteObject)registry.lookup("MessageService");

			    this.stub.put(name, ro);
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

	public void send(List<User> ul, String msg){
	    try {
	    	for (User user : ul){

		        if(!this.stub.containsKey(user.getIp()+Integer.toString(user.getPort()))) {
	    		    addToGroup(user.getIp()+Integer.toString(user.getPort()), user.getPort());
		        }
			    RemoteObject ro = this.stub.get(user.getIp()+Integer.toString(user.getPort()));

	    		ro.sendMessage("msg", nickname +": " +msg);
		    }
	    } catch (RemoteException e) {
		    e.printStackTrace();
	    }
    }

}
