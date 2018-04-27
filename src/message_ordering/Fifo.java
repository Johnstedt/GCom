package message_ordering;

import group_management.Group;
import group_management.User;

import java.util.HashMap;
import java.util.List;

public class Fifo extends Order {
	public Fifo(User u) {

	}

	@Override
	public void send(List<User> ul, String msg){

	}

	@Override
	public void receive(String msg) {

	}

	@Override
	public void askGroups(User u, String groupName, Group g) {

	}


	@Override
	public void sendGroups(List<User> users, HashMap<String, Group> hm) {

	}
}
