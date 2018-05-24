package message_ordering;

import communication.Multicast;

import static group_management.MessageOrderingType.FIFO;

/**
  * Note that fifo message ordering isn't necessarily as strict as
  * Causal but Causal satisfies all requirements for FIFO.
  */
public class Fifo extends Causal {

	public Fifo(Multicast com) {
		super(com, FIFO);
	}
}