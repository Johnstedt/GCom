package message_ordering;

import group_management.User;

import java.util.List;

public class Causal extends Order {

	public Causal(User u) {
		super();
	}

	@Override
	public void send(List<User> ul, String msg){

	}

	@Override
	public void receive(String msg) {

	}
}
