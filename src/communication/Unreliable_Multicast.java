package communication;

import group_management.User;
import message.Message;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public class Unreliable_Multicast extends Multicast implements Serializable, Observer {

	public Unreliable_Multicast(User u){
		super(u);
	}

	public void send(Message msg) {
		sender.send(msg);
	}

	@Override
	public void update(Observable observable, Object o) {
		this.setChanged();
		this.notifyObservers(o);

	}
}