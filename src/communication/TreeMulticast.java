package communication;

import group_management.User;
import message.Message;
import message.TMessage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TreeMulticast extends Multicast implements Serializable, Observer {
	public TreeMulticast(User u) {
		super(u);
	}

	@Override
	public void send(Message msg) {
		//shoudl do sumthing
	}

	@Override
	void receiveFromReceiver(Message msg) {

	}

	@Override
	void sendToSender(Message msg) {

	}

	private User self;


	/*@Override
	public void send(Message msg) {
		TMessage tMsg = (TMessage)msg;

		List<User> listWithMe = new LinkedList<>();
		listWithMe.add(self);
		tMsg.setUserList(listWithMe);
		super.sender.send(tMsg);
	}

	public void removeStubs() {
		super.sender.remove();
	}

	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof TMessage) {
			if(whoSend((TMessage) o).size() > 0) {
				TMessage tMsg = (TMessage) o;
				tMsg.setUserList(whoSend(tMsg));
				super.sender.send(tMsg);
			}
		}
		this.setChanged();
		this.notifyObservers(o);
	}*/

	private List<User> whoSend(TMessage tMsg){

		int me = tMsg.getIndexOfUser(self);
		int from = tMsg.getIndexOfOrigin();
		int len = tMsg.getLength();

		LinkedList<User> ul = new LinkedList<>();

		if( (tree(me, from, len)) >= Math.pow(log2(len), 2) ){
			return ul;
		}

		if(tMsg.getUser(first(me, from, len)) != null && me != first(me,from,len) ){

			if(tree(me, from, len) <  tree( first(me,from,len), from, len)) {

				ul.add(tMsg.getUser(first(me, from, len)));
			}
		}

		if(tMsg.getUser(first(me, from, len)) != null && me != second(me,from,len) ){

			if(tree(me, from, len) <  tree( second(me, from, len), from, len)) {

				ul.add(tMsg.getUser(second(me, from, len)));
			}
		}

		return ul;
	}

	private int tree (int me, int from, int len){
		return mod(me - from, len);
	}

	private int first(int me, int from, int len){
		return (tree(me, from, len) *2 + 1 + from) % len;
	}

	private int second(int me, int from, int len){
		return (tree(me, from, len) *2 + 2 + from) % len;
	}

	private static int log2(double num) {
		//why log2 not java native??
		return (int)Math.floor((Math.log(num)/Math.log(2)));
	}

	private int mod(int x, int y) {
		int result = x % y;
		return result < 0? result + y : result;
	}

}