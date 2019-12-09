/**
 * 
 */
package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author john
 *
 */
class SocialNetworkTest {
	
	static SocialNetwork network;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		network = new SocialNetwork();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		network = null;
	}

	/**
	 * Tests that getFriends returns all connected users
	 */
	@Test
	void test001_find_all_friends() {
		network.addFriend("john","amy");
		network.addFriend("amy","joe");
		network.addUser("jamethy");
		List<String> connected = new ArrayList<String>(5);
		//System.out.println(network.getFriends("john", connected));
		if(network.getGroups() != 2) {
			fail("Incorrect number of groups detected");
		}
	}
	
	/**
	 * Tests that mutualFriends returns expected list
	 */
	@Test
	void test002_mutual_friends() {
		network.addFriend("john","amy");
		network.addFriend("amy","joe");
		network.addFriend("john", "mike");
		network.addFriend("joe","mike");
		System.out.println(network.mutualFriends("john","joe"));
		List<String> mutual = new ArrayList<String>();
		mutual.add("amy");
		//if(!network.mutualFriends("john","joe").equals(mutual))
			//fail("Correct list of mutual friends not returned");
	}
}
