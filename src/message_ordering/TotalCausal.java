package message_ordering;

import group_management.CommunicationType;
import group_management.User;
import message.Message;

public class TotalCausal extends Order {

	public TotalCausal(User u, CommunicationType communicationType) {
		super(u, communicationType);
	}

	@Override
	public void send(Message msg) {

	}

	@Override
	public void removeStubs() {

	}
}
