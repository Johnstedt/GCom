package group_management;

import client.Test;
import communication.Multicast;
import communication.ReliableMultiCast;
import communication.TreeMulticast;
import communication.UnreliableMulticast;
import message.Message;
import message_ordering.Causal;
import message_ordering.Fifo;
import message_ordering.Order;
import message_ordering.Unordered;
import rmi.Receiver;

import java.util.*;

public class GroupManager extends Observable implements Observer {

	private User self;
	private HashMap<String, Group> groups;
	private Receiver receiver;
	private boolean haveNameServer = true;

	public GroupManager(User u){
		this.self = u;
		this.groups = new HashMap<>();

		this.receiver = Test.receiver;
	}

	public Group createGroup(String name, MessageOrderingType sort_order, CommunicationType ct) {
		Group g = createGroup(self, name, sort_order, ct);
		g.addObserver(this);
		return g;
	}
	public Group createGroup(User u, String name, MessageOrderingType sort_order, CommunicationType ct) {

		Multicast multicast;
		switch (ct) {
			case UNRELIABLE_MULTICAST:
				multicast = new UnreliableMulticast(u);
				break;
			case RELIABLE_MULTICAST:
				multicast = new ReliableMultiCast(u);
				break;
			case TREE_MULTICAST:
				multicast = new TreeMulticast(u);
				break;
			default:
				multicast = new UnreliableMulticast(u);
		}

		Order order;
		switch (sort_order) {
			case UNORDERED:
				order = new Unordered(multicast);
				break;
			case CAUSAL:
				order = new Causal(multicast);
				break;
			case FIFO:
				order = new Fifo(multicast);
				break;
			default:
				order = new Causal(multicast);
				break;
		}

		Group g = new Group(order, multicast, name);

		g.addUser(self);
		g.join(self);
		groups.put(name, g);
		updateReceiverToGroup(g);
		return g;
	}

	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof Message) {
			Message msg = (Message) o;

			switch (msg.getType()) {
				case ASK_GROUPS:
					Group g = groups.get(msg.getGroupName());
					if (haveNameServer) {
						List<User> sendTo = new LinkedList();
						sendTo.add(msg.getFrom());
						g.sendGroups(this.groups, self, sendTo);
					}
					g.leave(msg.getFrom());

					break;
				case SEND_GROUPS:
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case JOIN:
					addUserToGroup(msg.getGroupName(), msg.getFrom());
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case LEAVE:
					User u = (User) msg.getMsg();
					if (u.equals(self)) {
						groups.remove(msg.getGroupName());
					}
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case INTERNAL:
					this.setChanged();
					this.notifyObservers(msg);
					break;
				default:
					//Prob TEXT
					break;
			}
		}
	}

	private void updateReceiverToGroup(Group g) {
		receiver.addOrder(g.getComm(), g.getGroupName());
		g.setNewReceiver(self, receiver);
	}

	private void addUserToGroup(String groupName, User u){

		groups.get(groupName).addUser(u);
	}

	public void send(Message msg){
		groups.get(msg.getGroupName()).send(msg);
	}


	public void askForGroups(String connectionHost, int connectionPort) {
		User otherUser = new User("connect_point", connectionHost, connectionPort);
		addUserToGroup("init", otherUser);
		List<User> userList = new LinkedList<>();
		userList.add(otherUser);
		groups.get("init").askGroups(self, userList);
	}

	public User getSelf() {
		return self;
	}

	public void addGroup(Group g) {
		if (!groups.containsKey(g.getGroupName())) {
			groups.put(g.getGroupName(), g);
			g.removeStubs();
			updateReceiverToGroup(g);
			g.addObserver(this);
			g.join(this.self);
		}
	}

	public boolean alreadyInGroup(String s) {
		return groups.values().contains(s);
	}

	/**
	 * Can only remove groups that the owner owns.
	 * @param groupname
	 */
	public void remove_group(String groupname) {
		if (groups.containsKey(groupname)) {
			groups.remove(groupname);
		}

	}

	public void haveActiveNameServer(boolean b) {
		haveNameServer = b;
	}
}
