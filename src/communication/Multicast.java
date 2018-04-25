package communication;

import java.util.List;

public interface Multicast {

	public void send(String msg, List<Integer> l);

	public void receive(String msg);

}
