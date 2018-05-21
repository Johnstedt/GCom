package communication;

import clock.Vector;
import group_management.User;
import message.Message;
import message.TMessage;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import static group_management.CommunicationType.TREE_MULTICAST;

public class TreeMulticast extends Multicast implements Serializable, Observer {
	public TreeMulticast(User u) {
		super(u, TREE_MULTICAST);

	}

	@Override
	void sendToSender(Message msg) {
		toSender(msg);
	}

	@Override
	public void send(Message msg) {

		TMessage tMsg = new TMessage(msg.getType(), msg.getGroupName(), (Vector) msg.getClock(), msg.getFrom(), msg.getSendTo(), msg.getMsg());
		List<User> listWithMe = new LinkedList<>();
		listWithMe.add(msg.getFrom());
		tMsg.setUserList(listWithMe);
		super.toSender(tMsg);
	}


	@Override
	void receiveFromReceiver(Message msg) {
		System.out.println("NEVER HERE!");
		if(msg instanceof TMessage) {
			System.out.println("ALWAYS HERE!");
			TMessage tMsg = (TMessage) msg;
			if(whoSend(tMsg).size() > 0) {
				System.out.println("longer than 0");
				tMsg.setUserList(whoSend(tMsg));
				super.toSender(tMsg);
			}
		}

		toGroupManagement(msg);
	}


	private List<User> whoSend(TMessage tMsg){
		int me = tMsg.getIndexOfUser(super.self);
		System.out.println(me);
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
		return (int)Math.floor((Math.log(num)/Math.log(2)));
	}

	private int mod(int x, int y) {
		int result = x % y;
		return result < 0? result + y : result;
	}

}