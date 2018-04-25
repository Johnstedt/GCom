package message_ordering;

public interface Order {

	public void send(String msg);
	public void receive(String msg);

}
