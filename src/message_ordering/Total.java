package message_ordering;

import communication.Multicast;
import group_management.CommunicationType;
import group_management.User;
import message.Message;

public class Total extends Order{

	public Total(User u, Multicast com) {
		super(u, com);
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
