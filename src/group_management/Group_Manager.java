package group_management;

import message_ordering.Order;
import message_ordering.Unordered;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Group_Manager {

	private User self;
	private List<Group> groups;
	private Order order; // Move to group

	public Group_Manager(User u, Integer cp){

		this.order = new Unordered(u, cp);
		Group g = new Group(this.order, "thegroup");
		this.groups = new LinkedList<Group>();
		groups.add(g);
		this.self = u;
	}

	public void send(String input){
		order.send(input);
	}

	public void recive(){

	}

}
