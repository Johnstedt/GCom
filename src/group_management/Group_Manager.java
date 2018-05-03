package group_management;

import communication.Receiver;
import message_ordering.*;

import java.util.*;

public class Group_Manager extends Observable implements Observer {

	private User self;
	private HashMap<String, Group> groups;
	private Receiver r;

	public Group_Manager(User u){
		this.self = u;
		this.groups = new HashMap<>();
		this.r = new Receiver(u.getPort());
		this.r.run();
		create_group(u, "init", MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
	}
	public Group_Manager(User u, String ch, Integer cp){

		this(u);
		if(cp == 1338){

			//Initiate communication

			create_group(u, "init", MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
			User otherUser = new User("connect_point", ch, cp);
			System.out.println(ch + cp);
			addUserToGroup("init", otherUser);
			askForGroups("init", this.groups.get("init"));

		} else {

			create_group( u, "thegroup",
					MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
		}

	}

	public Group create_group(User u, String name, MessageOrderingType sort_order, CommunicationType ct) {

		Order order;
		switch (sort_order){
			case UNORDERED:
				order = new Unordered(u);
				break;
			case TOTAL:
				order = new Total(u);
				break;
			case CAUSAL:
				order = new Causal(u);
				break;
			case FIFO:
				order = new Fifo(u);
				break;
			case TOTALCAUSAL:
				order = new TotalCausal(u);
				break;
			default:
				order = new TotalCausal(u);
				break;
		}

		Group g = new Group(order, name);
		g.addReceiver(this.r);
		g.addObserver(this);
		g.addUser(self);
		groups.put(name, g);
		return g;
	}

	private void addUserToGroup(String groupName, User u){
		groups.get(groupName).addUser(u);
	}

	public void send(String groupName, String input){
		groups.get(groupName).send(input, this.self);
	}

	public void askForGroups(String groupName, Group g){
		groups.get(groupName).askGroups(groupName, g);
	}

	public void askForGroups(String connectionHost, int connectionPort) {
		User otherUser = new User("connect_point", connectionHost, connectionPort);
		addUserToGroup("init", otherUser);
		askForGroups("init", this.groups.get("init"));
	}

	@Override
	public void update(Observable observable, Object o) {
		System.out.println("GroupManager update:"+o.getClass().toString());
		if(o instanceof Group) {
			Group g = (Group) o;
			g.sendGroups(this.groups);
		} else if(o instanceof HashMap){
			this.setChanged();
			this.notifyObservers(o);
		}
	}

	public User getSelf() {
		return self;
	}

	public void addGroup(String groupName, Group g) {
		g.removeStubs();
		g.addReceiver(this.r);
		g.addObserver(this);
		g.join(this.self);

	}
}
