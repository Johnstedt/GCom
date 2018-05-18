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
	protected LinkedList<Message> fromReceiverInDebugger = new LinkedList<>();
	protected LinkedList<Message> toSenderInDebugger = new LinkedList<>();
	protected Thread sendRunner, receiverRunner;

	protected AtomicBoolean holdToSender = new AtomicBoolean(false);
	protected AtomicBoolean holdFromReceiver = new AtomicBoolean(false);
	private ListView<Message> sendMsgList;
	private ListView<Message> recMsgList;


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
				Message msg = toSenderBeforeDebugger.take();
				System.out.println("CQ.runToSender: Got msg: "+msg.toString());
				if (!holdToSender.get()) {
					System.out.println("no debugging");
					toSenderAfterDebugger.put(msg);
				} else {
					System.out.println("do debugging");
					toSenderInDebugger.add(msg);
					Platform.runLater(()->sendMsgList.setItems(FXCollections.observableArrayList(toSenderInDebugger)));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println("DID EXIT CQ.runToSender");
	}
	private void runFromReceiver() {
		while(!Thread.interrupted()) {
			try {
				Message msg = fromReceiverBeforeDebugger.take();
				System.out.println("CQ.runToReceiver: Got msg: "+msg.toString());
				if (!holdFromReceiver.get()) {
					fromReceiverAfterDebugger.put(msg);
				} else {
					fromReceiverInDebugger.add(msg);
					Platform.runLater(()->recMsgList.setItems(FXCollections.observableArrayList(fromReceiverInDebugger)));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println("DID EXIT CQ.runFromReceiver");
	}


	void setHoldToSender(boolean holdToSender) {
		System.err.println("Change holdToSender"+holdToSender);
		this.holdToSender.set(holdToSender);
	}

	void setHoldFromReceiver(Boolean holdFromReceiver) {
		this.holdFromReceiver.set(holdFromReceiver);
	}

	public void setMsgLists(ListView<Message> recMsgList, ListView<Message> sendMsgList) {
		this.recMsgList = recMsgList;
		this.sendMsgList = sendMsgList;
		Platform.runLater(()->recMsgList.setItems(FXCollections.observableArrayList(fromReceiverInDebugger)));
		Platform.runLater(()->sendMsgList.setItems(FXCollections.observableArrayList(toSenderInDebugger)));
		//this.recMsgList.refresh();
		//this.sendMsgList.refresh();
	}

	public void deleteToSenderInDebug(Message item) {
		toSenderInDebugger.remove(item);
	}


	public void flushToSender() {
		toSenderAfterDebugger.addAll(toSenderInDebugger);
		toSenderInDebugger.clear();
		Platform.runLater(()->sendMsgList.setItems(FXCollections.observableArrayList(toSenderInDebugger)));
	}

	public void flushToReceiver() {
		fromReceiverAfterDebugger.addAll(fromReceiverInDebugger);
		fromReceiverInDebugger.clear();
		Platform.runLater(()->recMsgList.setItems(FXCollections.observableArrayList(fromReceiverInDebugger)));
	}
}
