package message_ordering;

import communication.Communicator;
import group_management.User;

import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class Unordered extends Observable implements Order, Observer {

	private Queue<Message> queue;
	private Communicator communicator;

	public Unordered(User u, Integer p){

		this.queue = new SynchronousQueue<>();

		Notify_Order no = new Notify_Order();
		no.addObserver(this);
		this.communicator = new Communicator(u, p, no);
	}

	@Override
	public void send(String msg) {
		communicator.send(msg);

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
		Message m = this.queue.poll();
		notifyObservers(m.getMsg());
	}

}
