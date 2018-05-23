package clock;

import group_management.User;

import java.util.HashMap;

public interface Clock extends Cloneable {

	Clock getClone() throws CloneNotSupportedException;

	void increment(User self);

	HashMap<User, Long> getClock();

	void incrementEveryone(Vector cl);

	boolean equalsQ(Vector comparison);

	public boolean isNextMessage(User from, Vector comparison);

}