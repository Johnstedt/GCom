package group_management;

import communication.Multicast;
import message.InternalMessage;
import message.Message;
import message.MessageType;
import message_ordering.Order;

import java.io.Serializable;
import java.util.*;

import static message.MessageType.INTERNAL;
import static message.MessageType.TEXT;

public class Group extends Observable implements Observer, Serializable {
	private List<Message> messages;
	private List<User> users;
	private String groupName;
	private Order order;
	private Multicast comm;

	Group(Order o, Multicast m, String groupName) {
		this.order = o;
		this.order.addObserver(this);
		this.groupName = groupName;
		this.messages = new LinkedList<>();
		this.users = new LinkedList<>();
		this.comm = m;
	}


	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) {
			Message msg = (Message) o;
			System.out.println("Group got message:"+msg.getType());
			messages.add(msg);
			if(msg.getType().equals(MessageType.JOIN)){
				this.addUser((User) msg.getMsg());
			}
			System.out.println(msg.getFrom().getNickname() + ": "+ msg.getMsg());
		}
		else if (o instanceof HashMap){


		} else if(o instanceof Group) {
			System.out.println("I RECEIVED ASK FOR GROUP IN GROUP");
		}

		setChanged();
		notifyObservers(o);
	}

	public void send(String msgTxt, User self) {
		Message msg = new Message(TEXT, groupName, self, users, msgTxt);
		send(msg);
	}


	public void send(Message msg) {
		System.out.println("I WILL SEND MESSAGE IN GROUP, type:"+msg.getType());
		this.order.send(msg);
	}

	public void sendGroups(HashMap<String, Group> hm, User self, List<User> from){
		Message msg = new Message(MessageType.SEND_GROUPS, groupName, self, from, hm);
		this.order.send(msg);
		System.out.println("I WILL SEND GROUPS IN GROUP");
	}

	public void addUser(User newUser) {
		if (!users.contains(newUser)) {
			users.add(newUser);
		}
	}

	public void askGroups(User self, List<User> to) {
		Message msg = new Message(MessageType.ASK_GROUPS, groupName, self, to, null);
		this.order.send(msg);
	}

	public void join(User u) {
		order.addObserver(this);

		List<User> newUserList = new LinkedList<>();
		for (User user : users) {
			if (user != null)
				try {
					newUserList.add((User) user.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
		}
		newUserList.add(u);
		Message msg = new Message(MessageType.JOIN, groupName, u, newUserList, u);
		this.order.send(msg);
		this.order.setSelf(u);
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

	void removeStubs() {
		this.order.removeStubs();
	}

	public void setNewReceiver(User self, Observable newReceiver) {
		List<User> to = new LinkedList<>();
		to.add(self);
		InternalMessage im = new InternalMessage(newReceiver, order.orderType, comm.comType);
		Message msg = new Message(INTERNAL, groupName, self, to, im);
		send(msg);
	}

	public Observer getComm() {
		comm.addObserver(this.order);
		return comm;
	}
}