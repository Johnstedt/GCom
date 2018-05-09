package communication;

import group_management.User;

import java.io.Serializable;
import java.util.Observer;

public class Unreliable_Multicast extends Multicast implements Serializable, Observer {

	public Unreliable_Multicast(User u){
		super(u);
	}

}