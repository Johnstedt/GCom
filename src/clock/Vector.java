package clock;

import java.io.Serializable;
import java.util.HashMap;

public class Vector implements Clock, Serializable{
	HashMap<String, Long> clock;

	public Vector() {
		this.clock = new HashMap<>();
	}

	@Override
	public HashMap<String, Long> IncrementAndGet() {
		return null;
	}

	@Override
	public void receive(HashMap<String, Long> givenClock) {

	}
}
