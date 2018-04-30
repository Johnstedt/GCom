package group_management;

import message_ordering.Order;
import message_ordering.Message;

import java.io.Serializable;
import java.util.*;

public class Group extends Observable implements Observer, Serializable {

	private List<Message> messages;
	private List<User> users;
	private String groupName;
	private Order order;

	Group(Order o, String groupName){
		this.order = o;
		this.order.addObserver(this);
		this.groupName = groupName;
		this.messages = new LinkedList<>();
		this.users = new LinkedList<>();
	}


	@Override
	public void update(Observable observable, Object o) {
		if(o instanceof Message) {
			messages.add((Message) o);
			System.out.println(((Message) o).getMsg());
			setChanged();
			notifyObservers(o);
		}
		else if (o instanceof HashMap){
			HashMap hm = (HashMap)o;
			this.setChanged();
			notifyObservers(hm);

		} else if(o instanceof Group) {
			setChanged();
			notifyObservers(o);
		}
	}

	public void send(String msg) {
		this.order.send(users, msg);
	}

	public void sendGroups(HashMap<String, Group> hm){
		this.order.sendGroups(this.users, hm);
	}

	void addUser(User u) {
		this.users.add(u);
	}

	public void askGroups(String groupName, Group g) {
		this.order.askGroups(this.users.get(0) ,groupName, g);
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<User> getUsers() {
		return users;
	}

	public String getGroupName() {
		return groupName;
	}

	public Order getOrder() {
		return order;
	}

}