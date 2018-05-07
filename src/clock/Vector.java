package clock;

import group_management.User;

import java.io.Serializable;
import java.util.HashMap;

public class Vector implements Clock, Serializable, Cloneable{
	HashMap<String, Long> clock;

	public Vector() {
		this.clock = new HashMap<>();
	}

	@Override
	public HashMap<String, Long> IncrementAndGet() {
		return null;
	}

	@Override
	public void receive(HashMap<String, Long> givenClock) {
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void increment(User self) {
		if(this.clock.containsKey(self.getIp()+self.getPort())){
			this.clock.put(self.getIp()+self.getPort(), this.clock.get(self.getIp()+self.getPort())+1L);
		}else {
			this.clock.put(self.getIp()+self.getPort(), 1L);
		}
	}

	public Long getLampert(){

		Long l = 0L;

		for (Object o1 : this.clock.entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

				l += (Long) pair.getValue();

		}
		return l;
	}

	public HashMap<String, Long> getClock(){
		return this.clock;
	}

	public void incrementEveryone(Vector cl) {
		for (Object o1 : cl.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){
				if((Long)pair.getValue() > this.clock.get(pair.getKey())){
					this.clock.put((String)pair.getKey(), (Long)pair.getValue());
				}
			}else {
				this.clock.put((String)pair.getKey(), (Long)pair.getValue());
			}
		}
	}

	public boolean equals(Vector comparison){
		for (Object o1 : comparison.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){
				if(pair.getValue() != this.clock.get(pair.getKey())){
					return false;
				}
			}else {
				return false;
			}
		}

		for (Object o1 : this.clock.entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(comparison.getClock().containsKey(pair.getKey())){
				if(pair.getValue() != comparison.getClock().get(pair.getKey())){
					return false;
				}
			}else {
				return false;
			}
		}

		return true;
	}


}
