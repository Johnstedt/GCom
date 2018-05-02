package message_ordering;

import group_management.Group;
import group_management.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public abstract class Order extends Observable implements Serializable{

	public void send(List<User> ul, String msg, User self){
		//hej
	}

	public abstract void receive(String msg);

	public abstract void askGroups(User u, String groupName, Group g);

	public abstract void sendGroups(List<User> users, HashMap<String, Group> hm);

	public abstract void join(List<User> users, User u);

	public abstract void removeStubs();
}
