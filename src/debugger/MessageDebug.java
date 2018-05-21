package debugger;

import message.Message;

public class MessageDebug {
	public Message msg;
	public Boolean isHold = false;

	public MessageDebug(Message msg) {
		this.msg = msg;
		this.isHold = false;
	}

	@Override
	public String toString() {
		return msg.toString();
	}
}
