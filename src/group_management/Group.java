package group_management;

import message_ordering.Order;
import message_ordering.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Group extends Observable implements Observer {

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
		messages.add((Message) o);
		System.out.println(((Message)o).getMsg());
	}

	public void send(String msg) {
		this.order.send(users, msg);
	}

	void addUser(User u) {
		this.users.add(u);
	}
}
