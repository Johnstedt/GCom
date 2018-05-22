package debugger;

import clock.Vector;
import message.Message;
import message.MessageType;

import java.util.LinkedList;

public class MessageCounter {

	class Counts {
		Message msg;
		int sendBefore = 0;
		int sendAfter = 0;
		int receiveBefore = 0;
		int receiveAfter = 0;

		public Counts(Message msg) {
			this.msg = msg;
		}

		@Override
		public String toString() {
			String m =  "["+msg.getClock()+ "] "+ msg.getType() +
						" SB:"+ sendBefore +
						", SA:" + sendAfter +
						", RB:" + receiveBefore +
						", RA:" + receiveAfter;
			if (msg.getType().equals(MessageType.TEXT)) {
				m += " '"+msg.getMsg() + "'";
			}
			return m;
		}

	}
	LinkedList<Counts> counts;


	public MessageCounter() {
		counts = new LinkedList<>();
	}

	public void add(MessageDebug msgDebug, String when) {
		if (msgDebug == null || msgDebug.msg == null || msgDebug.msg.getClock() == null) {
			return;
		}
		Counts current = null;
		for (Counts c : counts) {
			if (msgDebug.msg.getClock().equalsQ((Vector)c.msg.getClock())) {
				current = c;
				break;
			}
		}
		if (current == null) {
			current = new Counts(msgDebug.msg);
			counts.addFirst(current);
		}

		switch(when) {
			case "SENDBEFORE":
				current.sendBefore += msgDebug.msg.getSendTo().size();
				break;
			case "SENDAFTER":
				current.sendAfter += msgDebug.msg.getSendTo().size();
				break;
			case "RECEIVEBEFORE":
				current.receiveBefore += 1;
				break;
			case "RECEIVEAFTER":
				current.receiveAfter += 1;
				break;

			default:
				System.err.println("THIS SHOULD NEVER HAPPEN!");
				break;
		}





		System.err.println("COUNTS current:"+counts.size());
	}

}
