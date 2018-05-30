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

/**
 * Implements tree based multicast.
 *
 * Uses a source tree, not a group-shared tree.
 * See pfd report in repo for further info: https://github.com/Johnstedt/GCom
 */
public class TreeMulticast extends Multicast implements Serializable, Observer {
	public TreeMulticast(User u) {
		super(u, TREE_MULTICAST);

	}

	@Override
	protected void sendToSender(Message msg) {
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
	protected void receiveFromReceiver(Message msg) {

		if(msg instanceof TMessage) {

				TMessage tMsg = (TMessage) msg;
				if (whoSend(tMsg).size() > 0) {
					tMsg.setUserList(whoSend(tMsg));
					super.toSender(tMsg);
				}

		}

		toGroupManagement(msg);
	}

	/**
	 * Determines the source tree based on sender.
	 * Determines own position in source tree.
	 * Determines which users to send to next.
	 *
	 * @param tMsg Received message
	 * @return Returns a list of users to send to next
	 */
	private List<User> whoSend(TMessage tMsg){
		int me = tMsg.getIndexOfUser(super.self);
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

	// Map user position in list to user position in source tree
	private int tree (int me, int from, int len){
		return mod(me - from, len);
	}

	// Get first index of user to send to
	private int first(int me, int from, int len){
		return (tree(me, from, len) *2 + 1 + from) % len;
	}

	// Get second index of user to send to
	private int second(int me, int from, int len){
		return (tree(me, from, len) *2 + 2 + from) % len;
	}

	// Why not native one could wonder :/
	private static int log2(double num) {
		return (int)Math.floor((Math.log(num)/Math.log(2)));
	}

	// % operator is remainder in Java, this needs modulus. rem and mod differ on negative numbers.
	private int mod(int x, int y) {
		int result = x % y;
		return result < 0? result + y : result;
	}
}