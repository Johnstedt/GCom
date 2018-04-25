package group_management;

import message_ordering.Order;
import message_ordering.Unordered;

public class Group_Manager {

	private User self;
	private Order order; // Move to group

	public Group_Manager(User u, Integer cp){
		this.order = new Unordered(u, cp);
		this.self = u;
	}

	public void send(String input){
		order.send(input);
	}

	public void recive(){

	}

}
