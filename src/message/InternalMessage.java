package message;

import group_management.CommunicationType;
import group_management.MessageOrderingType;

import java.io.Serializable;
import java.util.Observable;

public class InternalMessage implements Serializable{
	public Observable receiver;
	public MessageOrderingType ot;
	public CommunicationType ct;

	public InternalMessage(Observable receiver, MessageOrderingType ot, CommunicationType ct) {
		this.receiver = receiver;
		this.ot = ot;
		this.ct = ct;
	}
}
