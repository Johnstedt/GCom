package clock;

import java.util.HashMap;

public interface Clock {

	HashMap<String, Long> IncrementAndGet();
	void receive(HashMap<String, Long> givenClock);

}
