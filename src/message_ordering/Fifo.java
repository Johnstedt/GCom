package message_ordering;

import communication.Multicast;
import group_management.User;
import message.Message;

import static group_management.MessageOrderingType.FIFO;

public class Fifo extends Order {

	public Fifo(Multicast com) {
		super(com, FIFO);
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
