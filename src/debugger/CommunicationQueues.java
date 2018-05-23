package debugger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import message.Message;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommunicationQueues {
	protected BlockingQueue<Message> fromReceiverBeforeDebugger;
	protected BlockingQueue<Message> fromReceiverAfterDebugger;
	protected BlockingQueue<Message> toSenderBeforeDebugger;
	protected BlockingQueue<Message> toSenderAfterDebugger;
	protected LinkedList<MessageDebug> fromReceiverInDebugger = new LinkedList<>();
	protected LinkedList<MessageDebug> toSenderInDebugger = new LinkedList<>();
	protected MessageCounter counts = new MessageCounter();
	protected Thread sendRunner, receiverRunner;

	protected AtomicBoolean holdToSender = new AtomicBoolean(false);
	protected AtomicBoolean holdFromReceiver = new AtomicBoolean(false);
	private ListView<MessageDebug> sendMsgList = null;
	private ListView<MessageDebug> recMsgList = null;
	private ListView<MessageCounter.Counts> msgCountList = null;


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
		System.out.println("DEBUGGER - RUNTOSENDER START");
		while (!Thread.interrupted()) {
			try {
				MessageDebug msgDebug = new MessageDebug(toSenderBeforeDebugger.take());
				counts.add(msgDebug, "SENDBEFORE");
				msgDebug.toSenderIncrementBefore();
				if (!holdToSender.get()) {
					System.out.println("no debugging");
					sendToSenderAfterDebug(msgDebug);
				} else {
					System.out.println("do debugging");
					toSenderInDebugger.add(msgDebug);
				}
				refreshLists();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println("DID EXIT CQ.runToSender");
	}


	private void runFromReceiver() {
		while(!Thread.interrupted()) {
			try {
				MessageDebug msgDebug = new MessageDebug(fromReceiverBeforeDebugger.take());
				counts.add(msgDebug, "RECEIVEBEFORE");
				msgDebug.fromReceiverIncrementBefore();
				if (!holdFromReceiver.get()) {
					fromReceiverAfterDebug(msgDebug);
				} else {
					fromReceiverInDebugger.add(msgDebug);
				}
				refreshLists();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println("DID EXIT CQ.runFromReceiver");
	}


	private void sendToSenderAfterDebug(MessageDebug msgDebug) {
		counts.add(msgDebug, "SENDAFTER");
		toSenderAfterDebugger.add(msgDebug.msg);
	}

	private void fromReceiverAfterDebug(MessageDebug msgDebug) {
		counts.add(msgDebug, "RECEIVEAFTER");
		fromReceiverAfterDebugger.add(msgDebug.msg);
	}


	void setHoldToSender(boolean holdToSender) {
		System.err.println("Change holdToSender"+holdToSender);
		this.holdToSender.set(holdToSender);
	}

	void setHoldFromReceiver(Boolean holdFromReceiver) {
		this.holdFromReceiver.set(holdFromReceiver);
	}

	public void setMsgLists(ListView<MessageDebug> recMsgList, ListView<MessageDebug> sendMsgList, ListView<MessageCounter.Counts> msgCount) {
		this.recMsgList = recMsgList;
		this.sendMsgList = sendMsgList;
		this.msgCountList = msgCount;
		refreshLists();
	}

	public void flushToSender() {
		LinkedList<MessageDebug> rem = new LinkedList<>();
		for (MessageDebug md : toSenderInDebugger) {
			if (!md.isHold) {
				sendToSenderAfterDebug(md);
				rem.add(md);
			}
		}
		toSenderInDebugger.removeAll(rem);
		refreshLists();

	}

	void refreshLists() {
		if (sendMsgList != null) {
			Platform.runLater(()->this.sendMsgList.setItems(FXCollections.observableArrayList(this.toSenderInDebugger)));
			Platform.runLater(() -> sendMsgList.refresh());
		}
		if (recMsgList != null) {
			Platform.runLater(()->this.recMsgList.setItems(FXCollections.observableArrayList(this.fromReceiverInDebugger)));
			Platform.runLater(() -> recMsgList.refresh());
		}
		if (msgCountList != null) {
			Platform.runLater(()->this.msgCountList.setItems(FXCollections.observableList(this.counts.counts)));
			Platform.runLater(() -> msgCountList.refresh());
		}
	}

	public void flushToReceiver() {
		LinkedList<MessageDebug> rem = new LinkedList<>();
		for (MessageDebug md : fromReceiverInDebugger) {
			if (!md.isHold) {
				fromReceiverAfterDebug(md);
				rem.add(md);
			}
		}
		fromReceiverInDebugger.removeAll(rem);
		refreshLists();
	}

	public void removeFromSendDebugger(MessageDebug item) {
		toSenderInDebugger.remove(item);
		refreshLists();

	}

	public void removeFromReceiverDebugger(MessageDebug item) {
		fromReceiverInDebugger.remove(item);
		refreshLists();
	}

	public void sendManuallyMessage(MessageDebug item) {
		sendToSenderAfterDebug(item);
		toSenderInDebugger.remove(item);
		refreshLists();
	}

	public void receiveManuallyMessage(MessageDebug item) {
		fromReceiverAfterDebug(item);
		fromReceiverInDebugger.remove(item);
		refreshLists();
	}
}
