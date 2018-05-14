package communication;

import clock.Vector;
import group_management.Group;
import group_management.User;
import message_ordering.Message;
import message_ordering.Notify_Order;
import message_ordering.TMessage;

import java.io.Serializable;
import java.util.*;

public class Tree_Multicast extends Multicast implements Serializable, Observer {

	private Sender s;
	private User self;
	private String gn;

	public Tree_Multicast(User u, String gn){

		this.s = new Sender(u);
		this.gn = gn;
		this.self = u;
	}

	public Notify_Order getListener(){
		Notify_Order no = new Notify_Order();
		no.addObserver(this);

		return no;
	}

	public void send(String gn, List<User> ul, Message msg) {
		TMessage tMsg = new TMessage(msg.getMsg(), (Vector) msg.getCl(), msg.getFrom());
		tMsg.setUl(ul);
		List<User> listWithMe = new LinkedList<>();
		listWithMe.add(self);
		s.send(gn, listWithMe, tMsg);
	}

	public void askGroup(User u, String groupName, Group g) {
		s.askGroup(u, groupName, g);
	}

	public void sendGroups(String gn, List<User> users, HashMap<String, Group> hm) {
		this.s.sendGroups(gn, users, hm);
	}

	public void join(String gn, List<User> users, User u) {
		this.self = u;
		this.s.join(gn, users, u);
	}

	public void removeStubs() {
		this.s.remove();
	}

	@Override
	public void update(Observable observable, Object o) {

		if(o instanceof TMessage) {
			if(whoSend((TMessage) o).size() > 0) {
				s.send(gn, whoSend((TMessage) o), ((TMessage) o));
			}
		}
		this.setChanged();
		this.notifyObservers(o);

	}
	private static int log2(double num)
	{
		return (int)Math.floor((Math.log(num)/Math.log(2)));
	}

	private int mod(int x, int y)
	{
		int result = x % y;
		return result < 0? result + y : result;
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

	private List<User> whoSend(TMessage tMsg){
		int me = tMsg.getIndexOfUser(self);
		System.out.println("me: "+ me );
		int from = tMsg.getIndexOfOrigin();
		System.out.println("from: "+from);
		int len = tMsg.getLength();
		System.out.println("length: " +len);

		LinkedList<User> ul = new LinkedList<>();

		if( (tree(me, from, len)) >= Math.pow(log2(len), 2) ){

			return ul;
		}

		boolean hasPeopleToSendTo = false;

		if(tMsg.getUser(first(me, from, len)) != null && me != first(me,from,len) ){

			if(tree(me, from, len) <  tree( first(me,from,len), from, len)) {

				ul.add(tMsg.getUser(first(me, from, len)));
				hasPeopleToSendTo = true;
			}
		}

		if(tMsg.getUser(first(me, from, len)) != null && me != second(me,from,len) ){

			if(tree(me, from, len) <  tree( second(me, from, len), from, len)) {

				ul.add(tMsg.getUser(second(me, from, len)));
				hasPeopleToSendTo = true;
			}
		}


		if(hasPeopleToSendTo) {
			return ul;
		} else {
			return ul;
		}
	}
}