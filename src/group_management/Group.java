package group_management;

import message_ordering.Order;
import message_ordering.Message;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Group extends Observable implements Observer {

	private List<Message> messages;
	private String groupName;
	private Order order;

	Group(Order o, String groupName){
		this.order = o;
		this.order.addObserver(this);
		this.groupName = groupName;
	}


	@Override
	public void update(Observable observable, Object o) {

	}
}
