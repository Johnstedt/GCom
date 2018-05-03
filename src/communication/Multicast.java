package communication;

import clock.Clock;
import group_management.Group;
import group_management.User;

import message_ordering.Message;
import message_ordering.Notify_Order;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Multicast extends Observable implements Observer {

	public abstract Notify_Order getListener();

	public abstract void send(String gn, List<User> ul, Message msg);

	public abstract void askGroup(User u, String groupName, Group g);

	public abstract void sendGroups(String gn, List<User> users, HashMap<String, Group> hm);

	public abstract void join(String gn, List<User> users, User u);

	public abstract void removeStubs();
}
