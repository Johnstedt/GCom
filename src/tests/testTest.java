package tests;

import junit.framework.TestCase;
import org.junit.Test;

public class testTest extends TestCase {

	// test that the test environment
	@Test
	public void testTest(){

		assertEquals(6, 3+3);
	}

	@Test
	public void testMath(){

		assertEquals( mod(3-4,7), 6 );

	}

	private int mod(int x, int y)
	{
		int result = x % y;
		return result < 0? result + y : result;
	}

}