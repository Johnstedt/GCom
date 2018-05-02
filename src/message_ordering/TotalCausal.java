package message_ordering;

import group_management.Group;
import group_management.User;

import java.util.HashMap;
import java.util.List;

public class TotalCausal extends Order {

	public TotalCausal(User u) {

	}

	@Override
	public void send(List<User> ul, String msg, User self){

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

	@Override
	public void join(List<User> users, User u) {

	}

	@Override
	public void removeStubs() {

	}
}
