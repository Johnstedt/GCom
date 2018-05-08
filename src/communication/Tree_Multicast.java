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

		s.send(gn, whoSend(tMsg), tMsg);
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

	private List<User> whoSend(TMessage tMsg){
		Integer me = tMsg.getIndexOfUser(self);
		System.out.println("me: "+me);
		Integer from = tMsg.getIndexOfOrigin();
		System.out.println("from: "+from);
		Integer len = tMsg.getLength();
		System.out.println("length: " +len);

		LinkedList<User> ul = new LinkedList<>();

		if( (me+from % len) +1 >= Math.pow(log2(len), 2) ){
			System.out.println("IM A LEAF DUDE");
			return ul;
		}

		boolean hasPeopleToSendTo = false;
		if(tMsg.getUser((me*2 + 1 + from) % len) != null && (me != (me*2 + 1 + from) % len)){
			System.out.println("FIRST SEND: "+ (me*2 + 1 + from) % len);
			ul.add(tMsg.getUser((me*2 + 1 + from) % len));
			hasPeopleToSendTo = true;
		}
		if(tMsg.getUser((me*2 + 2 + from) % len) != null && (me != (me*2 + 2 + from) % len)){
			System.out.println("SECOND SEND: "+ (me*2 + 2 + from) % len);
			ul.add(tMsg.getUser((me*2 + 2 + from) % len));
			hasPeopleToSendTo = true;
		}
		if(hasPeopleToSendTo) {
			return ul;
		} else {
			return ul;
		}
	}
}