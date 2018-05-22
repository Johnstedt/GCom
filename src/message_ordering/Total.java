package message_ordering;

import communication.Multicast;
import group_management.MessageOrderingType;
import group_management.User;
import message.Message;

public class Total extends Order{

	public Total(Multicast com) {
		super(com, MessageOrderingType.TOTAL);
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
