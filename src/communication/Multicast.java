package communication;

import group_management.User;
import message.Message;
import message.MessageType;
import rmi.Sender;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public abstract class Multicast extends Observable implements Observer, Serializable {
	Sender sender;

	Multicast(User u) {
		this.sender = new Sender(u);
	}

	public void send(Message msg) {
		if (msg.getType().equals(MessageType.INTERNAL_SET_NEW_RECEIVER)) {
			System.err.println("Multicast, setting new internal receiver.");
			//TODO: REMOVE THIS ALL THE WAY UP SINCE GOING AROUND JAVA OBSABLE IMPL.
			//setObservableReceiver((Observable) msg.getMsg());
			//Resetting.
			//msg = new Message(MessageType.INTERNAL_SET_NEW_RECEIVER, msg.getGroupName(), msg.getFrom(), msg.getSendTo(), this);
		} else {
			sender.send(msg);
		}

	}

	public void removeStubs() {
		this.sender.remove();
	}


	private void setObservableReceiver(Observable observableReceiver) {
		this.deleteObservers();
		observableReceiver.addObserver(this);
	}

	@Override
	public void update(Observable observable, Object o) {
		System.out.println("In Multicast");
		this.setChanged();
		this.notifyObservers(o);
	}
}
