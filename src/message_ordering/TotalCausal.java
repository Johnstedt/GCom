package message_ordering;

import communication.Multicast;
import group_management.MessageOrderingType;
import group_management.User;
import message.Message;

public class TotalCausal extends Order {

	public TotalCausal(Multicast com) {
		super(com, MessageOrderingType.TOTALCAUSAL);
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
