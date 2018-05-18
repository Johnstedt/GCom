package message_ordering;

import communication.Multicast;
import group_management.CommunicationType;
import group_management.User;
import message.Message;

public class Causal extends Order {

	public Causal(User u, Multicast comm) {
		super(u, comm);
	}

	@Override
	public void send(Message msg) {

	}

	@Override
	public void removeStubs() {

	}

	@Override
	public void queueAdd(Message m) {

	}
}
