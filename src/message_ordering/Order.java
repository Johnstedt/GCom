package message_ordering;

import group_management.User;

import java.util.List;
import java.util.Observable;

public abstract class Order extends Observable{

	public void send(List<User> ul, String msg){
		//hej
	}

	public abstract void receive(String msg);

}
