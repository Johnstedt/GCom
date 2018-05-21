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

		Assert.assertFalse(v2.equalsQ(v1));
		Assert.assertFalse(v1.equalsQ(v2));
	}

	@Test
	public void testVectorShouldEquals(){

		Assert.assertTrue(v2.equalsQ(v1));
		Assert.assertTrue(v1.equalsQ(v2));

		v1.increment(user1);
		v2.increment(user1);

		Assert.assertTrue(v2.equalsQ(v1));
		Assert.assertTrue(v1.equalsQ(v2));

		v1.increment(user1);
		v2.increment(user1);

		Assert.assertTrue(v2.equalsQ(v1));
		Assert.assertTrue(v1.equalsQ(v2));

		v1.increment(user2);
		v2.increment(user2);

		Assert.assertTrue(v2.equalsQ(v1));
		Assert.assertTrue(v1.equalsQ(v2));
	}

	@Test
	public void testVectorClone(){

		v1.increment(user1);
		v2.increment(user2);
		Vector v3 = null;
		try {
			v3 = v1.getClone();
			v3.increment(user2);
			v1.increment(user2);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		Assert.assertFalse(v3.equalsQ(v2));
		Assert.assertTrue(v1.equalsQ(v3));
	}

	@Test
	public void testVectorUpdateByOtherVector(){

		v1.increment(user1);
		v1.increment(user1);
		v1.increment(user1);
		v1.increment(user2);

		Assert.assertFalse(v2.equalsQ(v1));
		Assert.assertFalse(v1.equalsQ(v2));

		v2.incrementEveryone(v1);
		Assert.assertTrue(v2.equalsQ(v1));
		Assert.assertTrue(v1.equalsQ(v2));
	}

	@Test
	public void testNextInLine(){
		v2.increment(user2);
		Assert.assertTrue(v1.nextInLine(user2, v2));

		v1.incrementEveryone(v2);
		v1.increment(user1);
		Assert.assertTrue(v2.nextInLine(user1, v1));

		v1.increment(user1);
		Assert.assertFalse(v2.nextInLine(user1, v1));

		v2.increment(user1);
		Assert.assertTrue(v2.nextInLine(user1, v1));

	}

}