package communication;

import debugger.DebuggerController;
import group_management.User;
import message.Message;
import message.MessageType;
import rmi.Sender;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

public abstract class Multicast extends Observable implements Observer, Serializable {

	private Sender sender;
	private BlockingQueue<Message> fromReceiverBeforeDebugger;
	private BlockingQueue<Message> fromReceiverAfterDebugger;
	private BlockingQueue<Message> toSenderBeforeDebugger;
	private BlockingQueue<Message> toSenderAfterDebugger;
	private Thread fdfr, fdts;

	Multicast(User u) {
		this.sender = new Sender(u);
		fromReceiverBeforeDebugger = new LinkedBlockingQueue<>();
		fromReceiverAfterDebugger = new LinkedBlockingQueue<>();
		toSenderBeforeDebugger = new LinkedBlockingQueue<>();
		toSenderAfterDebugger = new LinkedBlockingQueue<>();
		DebuggerController.getDebugger().setQueues(fromReceiverBeforeDebugger, fromReceiverAfterDebugger, toSenderBeforeDebugger, toSenderAfterDebugger);
		fdfr = new Thread(this::fromDebuggerFromReceiver);
		fdts = new Thread(this::fromDebuggerToSender);
		fdfr.start();
		fdts.start();
	}

	private void fromDebuggerFromReceiver() {
		try {
			Message msg = fromReceiverAfterDebugger.take();
			receiveFromReceiver(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void fromDebuggerToSender() {
		try {
			Message msg = toSenderAfterDebugger.take();
			sender.send(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void setObservableReceiver(Observable observableReceiver) {
		this.deleteObservers();
		observableReceiver.addObserver(this);
	}

	public void removeStubs() {
		this.sender.remove();
	}

	/**
	 * Message from Receiver. Will go through debugger before subclass touch it.
	 * @param observable
	 * @param o
	 */
	@Override
	public final void update(Observable observable, Object o) {
		if (o instanceof Message) {
			Message msg = (Message) o;
			fromReceiverBeforeDebugger.add(msg);
		} else {
			System.err.println("Got some update in communication: "+o.getClass());
		}
	}

	/**
	 * Message from MessageOrdering/the Client.
	 * @param msg
	 */
	public final void send(Message msg) {
		if (msg.getType().equals(MessageType.INTERNAL_SET_NEW_RECEIVER)) {
			System.err.println("Multicast, setting new internal receiver.");
			//TODO: REMOVE THIS ALL THE WAY UP SINCE GOING AROUND JAVA OBSABLE IMPL.
			//setObservableReceiver((Observable) msg.getMsg());
			//Resetting.
			//msg = new Message(MessageType.INTERNAL_SET_NEW_RECEIVER, msg.getGroupName(), msg.getFrom(), msg.getSendTo(), this);
		} else {
			sendToSender(msg);
		}
	}


	/**
	 * Subclass method, receives a message, will have been through debugger first.
	 * @param msg The message received.
	 * NOTE: call toGroupManagement to send to user.
	 */
	abstract void receiveFromReceiver(Message msg);

	/**
	 * Subclass method, will send message to Sender,
	 * will need to go through debugger after.
	 * @param msg
	 * NOTE: Call toSender when handled the message.
	 */
	abstract void sendToSender(Message msg);


	protected final void toSender(Message msg) {
		toSenderBeforeDebugger.add(msg);
	}

	/**
	 * To send up to client
	 * @param msg
	 * NOTE: Will need to be used from subclass.
	 */
	protected final void toGroupManagement(Message msg) {
		this.setChanged();
		this.notifyObservers(msg);

	}


}
