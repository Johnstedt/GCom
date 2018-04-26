package group_management;

import message_ordering.*;

import java.util.HashMap;

public class Group_Manager {

	private User self;
	private HashMap<String, Group> groups;


	public Group_Manager(User u, Integer cp){

		this.self = u;



		//User otherUser = new User("Stranger", "localhost", cp);
		//addUserToGroup("thegroup", otherUser);

		if(cp == 1338){

			//Initiate communication
			this.groups = new HashMap<String, Group>();
			create_group(u, "init", "unordered");

			User otherUser = new User("connect_point", "localhost", cp);
			addUserToGroup("init", otherUser);
			askForGroups("init");

		} else {

			this.groups = new HashMap<String, Group>();
			create_group( u, "thegroup", "unordered");
		}

	}

	public void create_group(User u, String name, String sort_order) {

		Order order;
		switch (sort_order){
			case "unordered":
				order = new Unordered(u);
				break;
			case "total":
				order = new Total(u);
				break;
			case "causal":
				order = new Causal(u);
				break;
			case "fifo":
				order = new Fifo(u);
				break;
			case "totalcausal":
				order = new TotalCausal(u);
				break;
			default:
				order = new TotalCausal(u);
				break;
		}

		Group g = new Group(order, name);
		g.addUser(self);
		groups.put(name, g);
	}

	private void addUserToGroup(String groupName, User u){
		groups.get(groupName).addUser(u);
	}

	public void send(String groupName, String input){
		groups.get(groupName).send(input);
	}

	public void askForGroups(String groupName){
		//groups.get(groupName).askGroups(groupName);
	}

}
