package group_management;

import communication.Multicast;
import communication.ReliableMultiCast;
import communication.TreeMulticast;
import communication.Unreliable_Multicast;
import message.Message;
import message_ordering.*;
import rmi.Receiver;

import java.util.*;

public class GroupManager extends Observable implements Observer {

	private User self;
	private HashMap<String, Group> groups;
	private Receiver receiver;

	public GroupManager(User u){
		this.self = u;
		this.groups = new HashMap<>();
		this.receiver = new Receiver(u.getPort());
		//We do this somewhere else?
		//createGroup("init", MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
	}

	public Group createGroup(String name, MessageOrderingType sort_order, CommunicationType ct) {
		return createGroup(self, name, sort_order, ct);
	}
	public Group createGroup(User u, String name, MessageOrderingType sort_order, CommunicationType ct) {

		Multicast multicast;
		switch (ct) {
			case UNRELIABLE_MULTICAST:
				multicast = new Unreliable_Multicast(u);
				break;
			case RELIABLE_MULTICAST:
				multicast = new ReliableMultiCast(u);
				break;
			case TREE_MULTICAST:
				multicast = new TreeMulticast(u);
				break;
			default:
				multicast = new Unreliable_Multicast(u);
		}

		Order order;
		switch (sort_order) {
			case UNORDERED:
				order = new Unordered(u, multicast);
				break;
			case TOTAL:
				order = new Total(u, multicast);
				break;
			case CAUSAL:
				order = new Causal(u, multicast);
				break;
			case FIFO:
				order = new Fifo(u, multicast);
				break;
			case TOTALCAUSAL:
				order = new TotalCausal(u, multicast);
				break;
			default:
				order = new TotalCausal(u, multicast);
				break;
		}

		Group g = new Group(order, multicast, name, self);
		g.addObserver(this);
		//TODO: Below is retarded, should do by constructor?
		g.addUser(self);
		groups.put(name, g);
		//g.addReceiver(this.receiver);
		updateReceiverToGroup(g);
		return g;
	}

	@Override
	public void update(Observable observable, Object o) {
		System.out.println("GroupManager update:"+o.getClass().toString());
		if(o instanceof Message) {
			Message msg = (Message) o;
			switch (msg.getType()) {
				case ASK_GROUPS:
					System.out.println("I WILL SEND GROUPS IN GROUPMANAGER");
					Group g = groups.get(msg.getGroupName());
					List<User> sendTo = new LinkedList();
					sendTo.add(msg.getFrom());
					g.sendGroups(this.groups, self, sendTo);
					break;
				case SEND_GROUPS:
					System.err.println("GM update SEND_GROUPS!");
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case TEXT:
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case JOIN:
					addUserToGroup(msg.getGroupName(), msg.getFrom());
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case LEAVE:
					//TODO
					this.setChanged();
					this.notifyObservers(msg);
					break;
				case INTERNAL_SET_NEW_RECEIVER:
					this.setChanged();
					this.notifyObservers(msg);
					break;
				default:
					System.err.println("Groupmanager.update() switch default why?"+msg.getType());
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
		g.removeStubs();
		updateReceiverToGroup(g);
		g.addObserver(this);
		g.join(this.self);

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
			//TODO: disjoin all users.
			groups.remove(groupname);
		}

	}
}
