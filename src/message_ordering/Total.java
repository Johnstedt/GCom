package message_ordering;

import group_management.CommunicationType;
import group_management.Group;
import group_management.User;

import java.util.HashMap;
import java.util.List;

public class Total extends Order{

	public Total(User u, CommunicationType ct) {

	}

	@Override
	public void receive(String msg) {

	}

	@Override
	public void askGroups(User u, String groupName, Group g) {

	}

	@Override
	public void sendGroups(String gn, List<User> users, HashMap<String, Group> hm) {

	}

	@Override
	public void join(String gn, List<User> users, User u) {

	}

	@Override
	public void removeStubs() {

	}

	@Override
	public Notify_Order getNo() {
		return null;
	}

	@Override
	public void rebindObserver() {

	}
}
