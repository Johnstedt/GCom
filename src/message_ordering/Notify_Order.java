package message_ordering;

import java.io.Serializable;
import java.util.Observable;


public class Notify_Order extends Observable implements Serializable {

	Notify_Order(){

	}

	public void hello() {
		this.setChanged();
	}
}
