package group_management;

import message_ordering.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class Group_Manager implements Observer {

	private User self;
	private HashMap<String, Group> groups;


	public Group_Manager(User u, Integer cp){

		this.self = u;

		//User otherUser = new User("Stranger", "localhost", cp);
		//addUserToGroup("thegroup", otherUser);

		if(cp == 1338){

			//Initiate communication
			this.groups = new HashMap<String, Group>();
			create_group(u, "init", MessageOrderingType.UNORDERED,
					CommunicationType.UNRELIABLE_MULTICAST);

			User otherUser = new User("connect_point", "localhost", cp);
			addUserToGroup("init", otherUser);
			askForGroups("init", this.groups.get("init"));

		} else {

			this.groups = new HashMap<String, Group>();
			create_group( u, "thegroup",
					MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
		}

	}

	public void create_group(User u, String name, MessageOrderingType sort_order, CommunicationType ct) {

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
		g.addObserver(this);
		g.addUser(self);
		groups.put(name, g);
	}

	private void addUserToGroup(String groupName, User u){
		groups.get(groupName).addUser(u);
	}

	public void send(String groupName, String input){
		groups.get(groupName).send(input);
	}

	public void askForGroups(String groupName, Group g){
		groups.get(groupName).askGroups(groupName, g);
	}

	@Override
	public void update(Observable observable, Object o) {
		if(o instanceof Group) {
			System.out.println("SENDING GROUPS");
			Group g = (Group) o;
			g.sendGroups(this.groups);
		} else if(o instanceof HashMap){
			HashMap hm = (HashMap)o;

			Iterator it = hm.entrySet().iterator();
			while (it.hasNext()) {
				HashMap.Entry pair = (HashMap.Entry)it.next();
				System.out.println(pair.getKey());
				it.remove(); // avoids a ConcurrentModificationException
			}
			System.out.println();
		}
	}

}
