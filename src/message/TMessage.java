package message;

import group_management.User;

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
	                User from,
	                List<User> sendTo,
	                Object msg){

		super(type, groupName, from, sendTo, msg);
	}

	@Override
	public List<User> getSendTo() {
		return userList;
	}

	public Integer getIndexOfUser(User u){
		return this.userList.indexOf(u);
	}

	public Integer getIndexOfOrigin() {
		return this.userList.indexOf(this.getFrom());
	}

	public Integer getLength(){
		return this.userList.size();
	}

	public User getUser(int i) {

		if(userList.size() < i){
			return null;
		}
		return userList.get(i);
	}
}
