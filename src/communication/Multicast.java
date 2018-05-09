package communication;

import group_management.User;
import message.Message;
import rmi.Sender;

import java.util.Observable;
import java.util.Observer;

public abstract class Multicast extends Observable implements Observer {
	protected Sender sender;
	public Multicast(User u) {
		this.sender = new Sender(u);
	}

	public abstract void send(Message msg);

	public void removeStubs() {
		this.sender.remove();
	}


	public void setObservableReceiver(Observable observableReceiver) {
		this.deleteObservers();
		observableReceiver.addObserver(this);
	}
}
