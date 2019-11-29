package application;

import java.util.List;

public class SocialNetwork {
	
	/**
	 * adds the specified user to the network
	 * 
	 * @param user the name of the user to be added to the network
	 */
	void addUser(String user) {
		//TODO
	}
	
	/**
	 * removes the specified user from the network
	 * 
	 * @param user the name of the user to be removed from the network
	 */
	void removeUser(String user) {
		//TODO
	}
	
	/**
	 * Makes user1 and user2 friends in the network
	 * 
	 * @param user1 the name of one user to become friends
	 * @param user2 the name of the other user to become friends
	 */
	void addFriend(String user1, String user2) {
		//TODO
	}
	
	/**
	 * finds the number of connected groups in the network
	 *  
	 * @return returns the number of connected groups in the network
	 */
	public int getGroups() {
		//TODO
		return 0;
	}
	
	/**
	 * finds the list of mutual friends of user1 and user 2
	 * 
	 * @param user1 the name of a user in the graph
	 * @param user2 the name of another user in the graph
	 * @return returns a List of users who have user1 and user2 as friends
	 */
	public List<UserStruct> mutualFriends(String user1, String user2){
		//TODO
		return null;
	}
	
	/**
	 * finds the shorted path between user1 and user2
	 * 
	 * @param user1 the name of a user in the graph
	 * @param user2 the name of another user in the graph
	 * @return returns a List which is the shortest path from user1 to user2 in the network
	 */
	public List<UserStruct> friendLink(String user1, String user2){
		//TODO
		return null;
	}
	
	/**
	 * finds all friends of the specified user
	 * 
	 * @param user the user whose friends will be found
	 * @return returns a list of people who are friends with user
	 */
	public List<UserStruct> allFriends(String user){
		//TODO
		return null;
	}
}
