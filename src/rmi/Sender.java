package rmi;

import clock.Vector;
import group_management.User;
import message.Message;
import message.TMessage;

import java.io.Serializable;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;
import static message.MessageType.INTERNAL;

public class Sender implements Serializable{
	private HashMap<String, RemoteObject> stub; // make list later

    public Sender(User u) {

	    this.stub = new HashMap<>();
	    try {
		    addToGroup(u.getIp()+Integer.toString(u.getPort()), u);
	    } catch (CantConnectException e) {
		    e.printStackTrace();
	    }
    }

    private void addToGroup(String name, User u) throws CantConnectException {
    	Registry registry;
    	int timesToTry = 3;
	    while(timesToTry != 0) {
		    try {
			    registry = LocateRegistry.getRegistry(u.getIp(), u.getPort());
			    RemoteObject ro = (RemoteObject)registry.lookup("MessageService");
			    this.stub.put(name, ro);
			    return;
		    } catch (NotBoundException | RemoteException e) {
			    //Wait til connect
			    //e.printStackTrace();
		    }
		    System.out.println("Can't connect, trying again...");
		    try {
			    sleep(1000);
		    } catch (InterruptedException ignore) {}
		    timesToTry--;
	    }
	    throw new CantConnectException(u);
    }

	public void send(Message msg) {
		try {
			if(msg instanceof TMessage){
				msg = (TMessage) msg.clone();
			} else {
				msg = (Message) msg.clone();
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		try {
	    	for (User user : msg.getSendTo()){
			    if(!this.stub.containsKey(user.getIp()+Integer.toString(user.getPort()))) {
				    try {
					    addToGroup(user.getIp()+Integer.toString(user.getPort()), user);
				    } catch (CantConnectException e) {
					    System.err.println("Couldn't send to user "+user.getNickname()+", tried 3 times, will ignore for this message.");
				    }
			    }
			    RemoteObject ro = this.stub.get(user.getIp()+Integer.toString(user.getPort()));
			    try {
				    msg = (Message) msg.clone();
			    } catch (CloneNotSupportedException e) {
				    e.printStackTrace();
			    }
			    try {
	    		  ro.transferMessage(msg);
			    } catch (ConnectException e) {
				    System.err.println("We refuse connection, "+e.getMessage());
				    List<User> nlist = msg.getSendTo();
				    nlist.remove(user);
				    Message m = new Message(INTERNAL, msg.getGroupName(), user, nlist, user);
				    m.setClock((Vector) msg.getClock());
				    this.send(m);
			    }
		    }
	    } catch (RemoteException e) {
		    e.printStackTrace();
	    }
    }


	public void remove() {
		this.stub.clear();
	}
}
