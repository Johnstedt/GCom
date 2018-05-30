package group_management;

import communication.Multicast;
import message.Message;
import message_ordering.Order;

import java.io.Serializable;
import java.util.*;

import static message.MessageType.*;

public class Group extends Observable implements Observer, Serializable {
	private List<Message> messages;
	private List<User> users;
	private String groupName;
	private Order order;
	private Multicast comm;

	Group(Order o, Multicast m, String groupName) {
		this.order = o;
		this.groupName = groupName;
		this.messages = new LinkedList<>();
		this.users = new LinkedList<>();
		this.comm = m;
	}


	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) {
			Message msg = (Message) o;
			messages.add(msg);
			switch (msg.getType()){
				case JOIN:
					this.addUser((User) msg.getMsg());
					break;
				case LEAVE:
					users.remove((User)msg.getMsg());
					break;
				case INTERNAL:
					if (msg.getMsg() instanceof User) {
						if (!users.contains(msg.getMsg())) {
							return;
						}
						users.remove((User)msg.getMsg());
					}
			}
		}
		setChanged();
		notifyObservers(o);
	}

	public void send(String msgTxt, User self) {
		Message msg = new Message(TEXT, groupName, self, getUsers(), msgTxt);
		send(msg);
	}


	public void send(Message msg) {
		this.order.send(msg);
	}


	public void addUser(User newUser) {
		if (!users.contains(newUser)) {
			users.add(newUser);
		}
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
		if(!newUserList.contains(u)) {
			newUserList.add(u);
		}
		Message msg = new Message(JOIN, groupName, u, newUserList, u);
		this.order.send(msg);
		this.order.setSelf(u);
	}

	public List<Message> getMessages() {
		return messages;
	}

	public List<User> getUsers() {
		return new ArrayList<>(users);
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

	public Observer getComm() {
		comm.addObserver(this.order);
		return comm;
	}
	public CommunicationType getCT() {
		return comm.comType;
	}

	public void leave(User self) {
		Message msg = new Message(LEAVE, groupName, self, getUsers(), self);
		send(msg);
		getUsers().remove(self);
	}
}