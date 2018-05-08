package message_ordering;

import clock.Vector;
import group_management.User;

import java.io.Serializable;

import java.util.List;

public class TMessage extends Message implements Serializable {

	private List<User> ul;
	private String gn;

	public TMessage(String msg, Vector cl, User from) {
		super(msg, cl, from);
	}

	public void setUl(List<User> ul) {
		this.ul = ul;
	}

	public Integer getIndexOfUser(User u){
		return this.ul.indexOf(u);
	}

	public Integer getIndexOfOrigin() {
		return this.ul.indexOf(this.getFrom());
	}

	public Integer getLength(){
		return this.ul.size();
	}

	public User getUser(int i) {

		if(ul.size() < i){
			return null;
		}
		return ul.get(i);
	}
}
