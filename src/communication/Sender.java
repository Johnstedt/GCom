package communication;

import group_management.Group;
import group_management.User;
import message_ordering.Message;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

public class Sender implements Serializable{

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

	public void send(List<User> ul, Message msg){
	    try {
	    	for (User user : ul){

		        if(!this.stub.containsKey(user.getIp()+Integer.toString(user.getPort()))) {
	    		    addToGroup(user.getIp()+Integer.toString(user.getPort()), user.getPort());
		        }
			    RemoteObject ro = this.stub.get(user.getIp()+Integer.toString(user.getPort()));

	    		ro.sendMessage(msg);
		    }
	    } catch (RemoteException e) {
		    e.printStackTrace();
	    }
    }

	public void askGroup(User u, String groupName, Group g) {
		try {
			if (!this.stub.containsKey(u.getIp() + Integer.toString(u.getPort()))) {
				addToGroup(u.getIp() + Integer.toString(u.getPort()), u.getPort());
			}
			RemoteObject ro = this.stub.get(u.getIp() + Integer.toString(u.getPort()));
			ro.askGroup(g);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void sendGroups(List<User> users, HashMap<String, Group> hm) {
		try {
			for (User user : users) {

				if (!this.stub.containsKey(user.getIp() + Integer.toString(user.getPort()))) {
					addToGroup(user.getIp() + Integer.toString(user.getPort()), user.getPort());
				}
				if(this.nickname.equals(user.getNickname())) {
					RemoteObject ro = this.stub.get(user.getIp() + Integer.toString(user.getPort()));
					ro.sendGroups(hm);
				}
			}
		}catch (RemoteException e) {
				e.printStackTrace();
			}
	}

	public void join(List<User> users, User u) {
		try {
			for (User user : users) {

				if (!this.stub.containsKey(user.getIp() + Integer.toString(user.getPort()))) {
					addToGroup(user.getIp() + Integer.toString(user.getPort()), user.getPort());
				}

				RemoteObject ro = this.stub.get(user.getIp() + Integer.toString(user.getPort()));
				ro.join(u);

			}
		}catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
