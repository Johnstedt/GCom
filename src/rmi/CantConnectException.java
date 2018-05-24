package rmi;

import group_management.User;

public class CantConnectException extends Throwable {
	public CantConnectException(User u) {
		super(u.toString() + " can not be connected to!");
	}
}
