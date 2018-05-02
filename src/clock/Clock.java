package clock;

import java.util.HashMap;

public interface Clock extends Cloneable {

	HashMap<String, Long> IncrementAndGet();
	void receive(HashMap<String, Long> givenClock);

}
