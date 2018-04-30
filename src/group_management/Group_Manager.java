package group_management;

import message_ordering.*;

import java.util.*;

public class Group_Manager implements Observer {

	private User self;
	private HashMap<String, Group> groups;

	public Group_Manager(User u){
		this.self = u;
		this.groups = new HashMap<>();
	}
	public Group_Manager(User u, Integer cp){
		this(u);

		if(cp == 1338){

			//Initiate communication
			create_group(u, "init", MessageOrderingType.UNORDERED,
					CommunicationType.UNRELIABLE_MULTICAST);

			User otherUser = new User("connect_point", "localhost", cp);
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

	@Override
	public void update(Observable observable, Object o) {
		if(o instanceof Group) {

			Group g = (Group) o;
			g.sendGroups(this.groups);




		} else if(o instanceof HashMap){

			HashMap hm = (HashMap)o;
			Scanner in = new Scanner(System.in);
			String input;

			LinkedList<Group> l = new LinkedList<Group>();

			for (Object o1 : hm.entrySet()) {
				HashMap.Entry pair = (HashMap.Entry) o1;
				System.out.println(pair.getKey() + " join?");

				input = in.nextLine();
				if (input.equals("yes")) {
					Group g = (Group) pair.getValue();
					l.add(g);
				}
			}

			for(Group g: l){
				this.groups.put(g.getGroupName(), g);
				g.join(this.self);
			}

		}
	}

	public User getSelf() {
		return self;
	}
}
