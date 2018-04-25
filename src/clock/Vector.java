package clock;

import java.util.HashMap;

public class Vector implements Clock{
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
