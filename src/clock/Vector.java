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

	public Vector getClone() {

		Vector v = new Vector();
		v.incrementEveryone(this);

		return v;
	}

	public void increment(User self) {
		if(this.clock.containsKey(self.getIp()+self.getPort())){
			this.clock.put(self.getIp()+self.getPort(), this.clock.get(self.getIp()+self.getPort())+1L);
		}else {
			this.clock.put(self.getIp()+self.getPort(), 1L);
		}
	}

	public HashMap<String, Long> getClock(){
		return this.clock;
	}

	public void incrementEveryone(Vector cl) {
		for (Object o1 : cl.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){
				if((Long)pair.getValue() > this.clock.get(pair.getKey())){
					this.clock.put((String)pair.getKey(), new Long((Long)pair.getValue()));
				}
			}else {
				this.clock.put((String)pair.getKey(), new Long((Long)pair.getValue()));
			}
		}
	}

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

	public boolean nextInLine(User from, Vector comparison){
		for (Object o1 : comparison.getClock().entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(this.clock.containsKey(pair.getKey())){
				/*System.out.println("thisiskey: "+ (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L));
				System.out.println("this is pair value: "+ pair.getValue());
				System.out.println("this is my value : "+ this.clock.get(pair.getKey()));
				System.out.println("this is the sum: " + Long.toString(this.clock.get(pair.getKey()) + (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L) ));
				System.out.println("is 2 the same as 2: " + pair.getValue().equals(   (this.clock.get(pair.getKey()) + (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L) )                  )  );*/
				if(!pair.getValue().equals(   (this.clock.get(pair.getKey()) + (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L) )                  )){
				//	System.out.println("IM AINT SUPPOSE TO FAIL FIRST");
					return false;
				}
			}else {
			//	System.out.println("I AINT GOT NONE");
				if(!pair.getValue().equals(1L)) {
					System.out.println("in here?");
					return false;
				}
			}
		}
		for (Object o1 : this.clock.entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			if(comparison.getClock().containsKey(pair.getKey())){

			/*	System.out.println("__THIS IS IN SECOND__");
				System.out.println("thisiskey: "+ (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L));
				System.out.println("this is pair value: "+ pair.getValue());
				System.out.println("this is my value : "+ comparison.getClock().get(pair.getKey()));
				System.out.println("this is the sum: " + Long.toString(comparison.getClock().get(pair.getKey()) + (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L) ));
				System.out.println("is 3 the same as 3: " + comparison.getClock().get(pair.getKey()).equals(   ((Long)pair.getValue() + (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L) )                  )  );*/

				if(!comparison.getClock().get(pair.getKey()).equals(   ((Long)pair.getValue() + (pair.getKey().equals(from.getIp()+from.getPort()) ? 1L : 0L) )                  )){
				//	System.out.println("IM AINT SUPPOSE TO FAIL TWO");
					return false;
				}
			}
		}
		return true;
	}

	public void printClock(){
		for (Object o1 : this.clock.entrySet()) {
			HashMap.Entry pair = (HashMap.Entry) o1;

			System.out.println("ClockKey: "+pair.getKey() + " ClockValue: "+pair.getValue());
		}

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
