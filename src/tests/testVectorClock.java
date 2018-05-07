package tests;

import clock.Vector;
import group_management.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class testVectorClock {

	private User user1,user2;
	private Vector v1, v2;

	@Before
	public void setUp(){

		user1 = new User("user1", "host1", 10000 );
		user2 = new User("user1", "host1", 10000 );
		v1 = new Vector();
		v2 = new Vector();
	}

	@Test
	public void testVectorShouldNotEquals(){

		v1.increment(user1);

		Assert.assertFalse(v2.equals(v1));
		Assert.assertFalse(v1.equals(v2));
	}

	@Test
	public void testVectorShouldEquals(){

		Assert.assertTrue(v2.equals(v1));
		Assert.assertTrue(v1.equals(v2));

		v1.increment(user1);
		v2.increment(user1);

		Assert.assertTrue(v2.equals(v1));
		Assert.assertTrue(v1.equals(v2));

		v1.increment(user1);
		v2.increment(user1);

		Assert.assertTrue(v2.equals(v1));
		Assert.assertTrue(v1.equals(v2));

		v1.increment(user2);
		v2.increment(user2);

		Assert.assertTrue(v2.equals(v1));
		Assert.assertTrue(v1.equals(v2));
	}

	@Test
	public void testVectorClone(){

		v1.increment(user1);
		Vector v3 = null;
		try {
			v3 = (Vector) v1.clone();
			v3.increment(user2);
			v1.increment(user2);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		Assert.assertTrue(v1.equals(v3));
	}

	@Test
	public void testVectorUpdateByOtherVector(){

		v1.increment(user1);
		v1.increment(user1);
		v1.increment(user1);
		v1.increment(user2);

		Assert.assertFalse(v2.equals(v1));
		Assert.assertFalse(v1.equals(v2));

		v2.incrementEveryone(v1);
		Assert.assertTrue(v2.equals(v1));
		Assert.assertTrue(v1.equals(v2));
	}
}