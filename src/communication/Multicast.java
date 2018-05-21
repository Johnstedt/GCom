package communication;

import debugger.DebuggerController;
import group_management.CommunicationType;
import group_management.User;
import message.InternalMessage;
import message.Message;
import message.MessageType;
import rmi.Sender;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Multicast extends Observable implements Observer, Serializable {

	private Sender sender;
	private BlockingQueue<Message> fromReceiverBeforeDebugger;
	private BlockingQueue<Message> fromReceiverAfterDebugger;
	private BlockingQueue<Message> toSenderBeforeDebugger;
	private BlockingQueue<Message> toSenderAfterDebugger;
	public CommunicationType comType;
	protected User self;
	//private Thread fdfr, fdts;

	Multicast(User u, CommunicationType comType) {
		this.sender = new Sender(u);
		this.comType = comType;
		this.self = u;
		fromReceiverBeforeDebugger = new LinkedBlockingQueue<>();
		fromReceiverAfterDebugger = new LinkedBlockingQueue<>();
		toSenderBeforeDebugger = new LinkedBlockingQueue<>();
		toSenderAfterDebugger = new LinkedBlockingQueue<>();
	}

	private void fromDebuggerFromReceiver() {
		System.out.println("FROM DEBUGGER TO RECEIVER THREAD");
		while (!Thread.interrupted()) {
			try {
				Message msg = fromReceiverAfterDebugger.take();
				System.out.println("multicast.fromDebuggerFromReceiver - got msg:"+msg.toString());
				receiveFromReceiver(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void fromDebuggerToSender() {
		System.out.println("FROM DEBUGGER TO SENDER THREAD");
		while (!Thread.interrupted()) {
			try {
				Message msg = toSenderAfterDebugger.take();
				System.out.println("FROM DEBUGGER TO SENDER THREAD - got msg:" + msg.toString());
				sender.send(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
	public abstract void send(Message msg);


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
		if (msg.getType().equals(MessageType.INTERNAL)) {
			System.err.println("TO SENDER SET CQ");
			DebuggerController.getDebugger().setQueues((InternalMessage) msg.getMsg(), msg.getGroupName(), fromReceiverBeforeDebugger, fromReceiverAfterDebugger, toSenderBeforeDebugger, toSenderAfterDebugger);
			Thread fdfr, fdts;
			fdfr = new Thread(this::fromDebuggerFromReceiver);
			fdts = new Thread(this::fromDebuggerToSender);
			fdfr.start();
			fdts.start();
		}
		System.out.println("Multicast - add msg to toSenderBeforeDebugger"+msg.toString());
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


	public void setSelf(User self) {
		this.self = self;
	}
}
