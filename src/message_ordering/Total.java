package message_ordering;

import group_management.CommunicationType;
import group_management.User;
import message.Message;

public class Total extends Order{

	public Total(User u, CommunicationType communicationType) {
		super(u, communicationType);
	}

	@Override
	public void send(Message msg) {

	}

	@Override
	public void removeStubs() {

	}
}
