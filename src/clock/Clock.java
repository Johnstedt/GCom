package clock;

import group_management.User;

import java.util.HashMap;

public interface Clock extends Cloneable {

	HashMap<String, Long> IncrementAndGet();

	void receive(HashMap<String, Long> givenClock);

	public Object clone() throws CloneNotSupportedException;

	public void increment(User self);

	public Long getLampert();

	public HashMap<String, Long> getClock();

	public void incrementEveryone(Vector cl);

	public boolean equals(Vector comparison);
}