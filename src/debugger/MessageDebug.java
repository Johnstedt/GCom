package debugger;

import message.Message;

public class MessageDebug {
	Message msg;
	Boolean isHold = false;
	int beforeToSender = 0;
	int afterToSender = 0;
	int afterReceiver = 0;
	int beforeReceiver = 0;

	public MessageDebug(Message msg) {
		this.msg = msg;
		this.isHold = false;
	}

	@Override
	public String toString() {
		return msg.toString();
	}

	public void toSenderIncrementBefore() {
		this.beforeToSender += msg.getSendTo().size();
	}

	public void toSenderAfterIncrement() {
		this.afterToSender += msg.getSendTo().size();
	}

	public void fromReceiverAfterIncrement() {
		this.afterReceiver += 1;
	}

	public void fromReceiverIncrementBefore() {
		this.beforeReceiver += 1;
	}
}
