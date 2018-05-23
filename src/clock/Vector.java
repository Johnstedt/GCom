package clock;

import group_management.User;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Implementation of a vector clock
 *
 * Uses a HashMap to keep track of user-elem in vector.
 *
 */
public class Vector implements Clock, Serializable, Cloneable{
	private HashMap<User, Long> clock;

	public Vector() {
		this.clock = new HashMap<>();
	}

	public Vector getClone() {

		Vector v = new Vector();
		v.incrementEveryone(this);

		return v;
	}

	/**
	 * Increment the vector clock for specific user
	 * @param u - User to increment
	 */
	public void increment(User u) {
		if(this.clock.containsKey(u)){
			this.clock.put(u, this.clock.get(u)+1L);
		}else {
			this.clock.put(u, 1L);
		}
	}

	/**
	 * Returns the vector clock as a hashmap
	 * @return HashMap
	 */
	public HashMap<User, Long> getClock(){
		return this.clock;
	}

	public void incrementEveryone(Vector cl) {
		for (Object o1 : cl.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){
				if((Long)pair.getValue() > this.clock.get(pair.getKey())){
					this.clock.put((User)pair.getKey(), new Long((Long)pair.getValue()));
				}
			}else {
				this.clock.put((User)pair.getKey(), new Long((Long)pair.getValue()));
			}
		}
	}

	/**
	 * Returns true if vector clocks are equal for all elements
	 * @param comparison vector to compare this with
	 * @return bool
	 */
	public boolean equalsQ(Vector comparison){
		for (Object o1 : comparison.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){
				if(!((Long)pair.getValue()).equals((Long)this.clock.get(pair.getKey()))){
					return false;
				}
			}else {
				return false;
			}
		}
		for (Object o1 : this.clock.entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(comparison.getClock().containsKey(pair.getKey())){
				if(!((Long)pair.getValue()).equals((Long)comparison.getClock().get(pair.getKey()))){
					return false;
				}
			}else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the message is next message.
	 * Meaning if there is a message that 'happened before -> ' this message it will return false
	 * because it's not the next message. This is necessary for causal message ordering and
	 * satisfies FIFO although FIFO isn't necessarily this strict.
	 * See pdf Report in git repo for further info: https://github.com/Johnstedt/GCom
	 * @param from user message is from
	 * @param comparison vector in message
	 * @return boolean
	 */
	@Override
	public boolean isNextMessage(User from, Vector comparison){

		for (Object o1 : comparison.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){

				Long reVal = (Long)pair.getValue();
				Long myVal = this.clock.get(pair.getKey());
				User userKey = (User)pair.getKey();

				if(!(userKey.equals(from) && reVal.equals(myVal+1L)) ) {
					if(! (!userKey.equals(from) &&  0 <= myVal.compareTo(reVal) ) ){
						return false;
					}
				}
			}else {
				if(!pair.getValue().equals(1L)) {
					return false;
				}
			}
		}
		for (Object o1 : this.clock.entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(comparison.getClock().containsKey(pair.getKey())){

				Long myVal = (Long)pair.getValue();
				Long reVal = comparison.getClock().get(pair.getKey());
				User userKey = (User)pair.getKey();

				if(!(userKey.equals(from) && reVal.equals(myVal + 1L)) ) {
					if(! (!userKey.equals(from) && 0 <= myVal.compareTo(reVal) ) ){
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder tot = new StringBuilder();
		for (Long l : clock.values()) {
			if (tot.length()>0) {
				tot.append(",");
			}
			tot.append(l.toString());
		}
		return tot.toString();
	}
}
