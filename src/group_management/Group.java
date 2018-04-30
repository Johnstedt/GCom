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
<<<<<<< HEAD
			System.out.println(((Message) o).getMsg());
			setChanged();
			notifyObservers(o);
=======
			System.out.println(((Message) o).getFrom().getNickname()+ ": " + ((Message) o).getMsg());
>>>>>>> afa89efdc12463134d6e5477be6b7e241c7ec26e
		}
		else if (o instanceof HashMap){
			HashMap hm = (HashMap)o;
			this.setChanged();
			notifyObservers(hm);

		} else if(o instanceof Group) {
			setChanged();
			notifyObservers(o);
		}
		else if(o instanceof User) {
			this.users.add((User)o);
		}
		setChanged();
		notifyObservers(o);
	}

	public void send(String msg, User self) {
		this.order.send(users, msg, self);
	}

	public void sendGroups(HashMap<String, Group> hm){
		this.order.sendGroups(this.users, hm);
	}

	void addUser(User u) {
		this.users.add(u);
	}

	public void askGroups(String groupName, Group g) {
		this.order.askGroups(this.users.get(1) ,groupName, g);
	}

	public String getGroupName() {
		return groupName;
	}

	public void join(User u) {
		this.order.join(this.users, u);
		this.users.add(u);
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
