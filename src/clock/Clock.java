package clock;

import group_management.User;

import java.util.HashMap;

public interface Clock extends Cloneable {

	HashMap<String, Long> IncrementAndGet();

	void receive(HashMap<String, Long> givenClock);

	public Clock getClone() throws CloneNotSupportedException;

	public void increment(User self);

	public HashMap<String, Long> getClock();

	public void incrementEveryone(Vector cl);

	public boolean equalsQ(Vector comparison);
}