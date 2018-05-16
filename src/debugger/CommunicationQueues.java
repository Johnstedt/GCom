package debugger;

import message.Message;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommunicationQueues {
	protected BlockingQueue<Message> fromReceiverBeforeDebugger;
	protected BlockingQueue<Message> fromReceiverAfterDebugger;
	protected BlockingQueue<Message> toSenderBeforeDebugger;
	protected BlockingQueue<Message> toSenderAfterDebugger;
	protected LinkedList<Message> fromReceiverInDebugger = new LinkedList<>();
	protected LinkedList<Message> toSenderInDebugger = new LinkedList<>();
	protected Thread sendRunner, receiverRunner;

	protected AtomicBoolean holdToSender = new AtomicBoolean(false);
	protected AtomicBoolean holdFromReceiver = new AtomicBoolean(false);


	CommunicationQueues(BlockingQueue<Message> fromReceiverBeforeDebugger, BlockingQueue<Message> fromReceiverAfterDebugger, BlockingQueue<Message> toSenderBeforeDebugger, BlockingQueue<Message> toSenderAfterDebugger) {
		this.fromReceiverBeforeDebugger = fromReceiverBeforeDebugger;
		this.fromReceiverAfterDebugger = fromReceiverAfterDebugger;
		this.toSenderBeforeDebugger = toSenderBeforeDebugger;
		this.toSenderAfterDebugger = toSenderAfterDebugger;
		sendRunner = new Thread(this::runToSender);
		receiverRunner = new Thread(this::runFromReceiver);
		sendRunner.start();
		receiverRunner.start();
	}

	private void runToSender() {
		while (Thread.interrupted()) {
			try {
				Message msg = toSenderBeforeDebugger.take();
				System.err.println("CQ.runToSender: Got msg: "+msg.toString());
				if (!holdToSender.get()) {
					toSenderAfterDebugger.put(msg);
				} else {
					toSenderInDebugger.add(msg);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void runFromReceiver() {
		while(Thread.interrupted()) {
			try {
				Message msg = fromReceiverBeforeDebugger.take();
				System.err.println("CQ.runToReceiver: Got msg: "+msg.toString());
				if (!holdFromReceiver.get()) {
					fromReceiverAfterDebugger.put(msg);
					fromReceiverInDebugger.add(msg);
				} else {
					fromReceiverInDebugger.add(msg);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
