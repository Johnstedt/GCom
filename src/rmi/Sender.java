package rmi;

import group_management.User;
import message.Message;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Sender implements Serializable{
	private HashMap<String, RemoteObject> stub; // make list later
	private String nickname;

    public Sender(User u) {
	    this.nickname = u.getNickname();
	    this.stub = new HashMap<>();
	    addToGroup(u.getIp()+Integer.toString(u.getPort()), u);
    }

    private void addToGroup(String name, User u){
    	Registry registry;
	    while(true) {
		    try {
			    registry = LocateRegistry.getRegistry(u.getIp(), u.getPort());
			    RemoteObject ro = (RemoteObject)registry.lookup("MessageService");
			    this.stub.put(name, ro);
			    System.out.println("found connection!");
			    break;
		    } catch (NotBoundException | RemoteException e) {
			    System.out.println(u.getPort());
			    //Wait til connect
			    //e.printStackTrace();
		    }
		    System.out.println("Can't connect, trying again...");
		    try {
			    sleep(1000);
		    } catch (InterruptedException ignore) {}
	    }
    }

	public void send(Message msg){
		try {
			msg = (Message) msg.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		try {
	    	for (User user : msg.getSendTo()){
			    System.out.println("Sender.send(msg): Sending "+msg);//.toString()+" "+msg.getType()+" to "+user.toString()+":"+user.getIp()+":"+user.getPort());

			    if(!this.stub.containsKey(user.getIp()+Integer.toString(user.getPort()))) {
	    		    addToGroup(user.getIp()+Integer.toString(user.getPort()), user);
		        }
			    RemoteObject ro = this.stub.get(user.getIp()+Integer.toString(user.getPort()));
			    if (ro == null) {
				    System.err.println("Sender: ro is null?");
			    }
			    try {
				    msg = (Message) msg.clone();
			    } catch (CloneNotSupportedException e) {
				    e.printStackTrace();
			    }
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
