package message_ordering;

import communication.Unreliable_Multicast;
import group_management.User;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class Unordered extends Order implements Observer {

	private Queue<Message> queue;
	private Unreliable_Multicast communicator;

	public Unordered(User u){

		this.queue = new LinkedBlockingQueue<>();

		Notify_Order no = new Notify_Order();
		no.addObserver(this);
		this.communicator = new Unreliable_Multicast(u, no);
	}

	@Override
	public void send(List<User> ul, String msg) {
		communicator.send(ul, msg);

	}

	@Override
	public void receive(String msg) {
		Message m = new Message(msg, null);
		queue.add(m);
	}

	@Override
	public void update(Observable observable, Object o) {
		Message m = new Message((String) o, null);
		queue.add(m);
		this.setChanged();
		sort();

	}

	private void sort(){
		Message m = queue.remove();
		notifyObservers(m);
	}

}
