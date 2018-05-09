package rmi;

import group_management.User;
import message.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Sender implements Serializable{

	private Sender sender;
	private HashMap<String, RemoteObject> stub; // make list later
	private String nickname;

    public Sender(User u) {
	    this.sender = new Sender(u);
	    this.nickname = u.getNickname();
	    this.stub = new HashMap<>();
	    addToGroup(u.getIp()+Integer.toString(u.getPort()), u);
    }

    void addToGroup(String name, User u){

    	Registry registry;
	    while(true) {
		    try {
			    sleep(1000);
			    registry = LocateRegistry.getRegistry(u.getIp(), u.getPort());

			    RemoteObject ro = (RemoteObject)registry.lookup("MessageService");

			    this.stub.put(name, ro);
			    System.out.println("found connection!");
			    break;
		    } catch (Exception e) {
			    System.out.println(u.getPort());
			    //Wait til connect
			    //e.printStackTrace();
		    }
		    System.out.println("Can't connect, trying again...");

	    }
    }

	public void send(Message msg){
	    try {
	    	for (User user : msg.getSendTo()){

			    if(!this.stub.containsKey(user.getIp()+Integer.toString(user.getPort()))) {
	    		    addToGroup(user.getIp()+Integer.toString(user.getPort()), user);
		        }
			    RemoteObject ro = this.stub.get(user.getIp()+Integer.toString(user.getPort()));

	    		ro.transferMessage(msg);
		    }
	    } catch (RemoteException e) {
		    e.printStackTrace();
	    }
    }


	public void remove() {
		this.stub.clear();
	}
}
