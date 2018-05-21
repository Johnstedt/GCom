package message;

import clock.Clock;
import clock.Vector;
import group_management.User;

import java.util.LinkedList;
import java.util.List;

public class TMessage extends Message{

	private List<User> userList;

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public TMessage(MessageType type,
	                String groupName,
	                Vector clock,
	                User from,
	                List<User> sendTo,
	                Object msg){

		super(type, groupName, clock, from, sendTo, msg);
	}

	@Override
	public List<User> getSendTo() {
		return this.userList;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		List<User> newUserList = new LinkedList<>();
		for (User u : userList) {
			if (u != null)
				newUserList.add((User) u.clone());
		}

		List<User> sendToList = new LinkedList<>();
		for (User u : sendTo) {
			if (u != null)
				sendToList.add((User) u.clone());
		}
		TMessage m2 = new TMessage(this.getType(), this.getGroupName(),(Vector) this.getClock(), (User) this.getFrom().clone(), sendToList, this.getMsg());

		m2.setUserList(newUserList);

		return m2;
	}

	public Integer getIndexOfUser(User u){
		return super.sendTo.indexOf(u);
	}

	public Integer getIndexOfOrigin() {
		return super.sendTo.indexOf(this.getFrom());
	}

	public Integer getLength(){
		return super.sendTo.size();
	}

	public User getUser(int i) {

		if(super.sendTo.size() < i){
			return null;
		}
		return super.sendTo.get(i);
	}
}
