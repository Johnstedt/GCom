package communication;

import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.Observer;

import static group_management.CommunicationType.UNRELIABLE_MULTICAST;

public class Unreliable_Multicast extends Multicast implements Serializable, Observer {

	public Unreliable_Multicast(User u){
		super(u, UNRELIABLE_MULTICAST);
	}

	@Override
	public void send(Message msg) {
		System.out.println("Unreliable got:"+msg.toString());
		sendToSender(msg);
	}

	@Override
	void receiveFromReceiver(Message msg) {
		toGroupManagement(msg);
	}

	@Override
	void sendToSender(Message msg) {
		toSender(msg);
	}

}