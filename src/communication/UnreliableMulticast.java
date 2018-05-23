package communication;

import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.Observer;

import static group_management.CommunicationType.UNRELIABLE_MULTICAST;

/**
 * Implements unreliable multicast.
 * Sends messages once and only once with no care in the world for
 * any problems whatsoever.
 */
public class UnreliableMulticast extends Multicast implements Serializable, Observer {

	public UnreliableMulticast(User u){
		super(u, UNRELIABLE_MULTICAST);
	}

	@Override
	public void send(Message msg) {
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