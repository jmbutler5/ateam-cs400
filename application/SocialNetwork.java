package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocialNetwork {

	// the user being displayed
	private String centerUser;

	// keeps track of the log so we can print it to a file
	// we have to actually keep track of operations (i.e. undo), so we can't just
	// print a representation of the network
	private ArrayList<String> log;

	private Graph graph;

	// these are the commands for the input file specified in the problem
	// description here:
	// https://canvas.wisc.edu/courses/170796/pages/team-project-social-network
	private static final char ADD_COMMAND = 'a';
	private static final char REMOVE_COMMAND = 'r';
	private static final char SET_COMMAND = 's';

	public SocialNetwork() {
		log = new ArrayList<String>();
		graph = new Graph();
	}

	/**
	 * creates a new SocialNetwork instance with the same data as the passed
	 * SocialNetwork. This is used when a file is read
	 * 
	 * @param toClone
	 */
	public SocialNetwork(SocialNetwork toClone) {
		centerUser = toClone.centerUser;

		log = new ArrayList<String>();
		for (int i = 0; i < toClone.log.size(); i++)
			log.add(toClone.log.get(i));

		graph = new Graph(toClone.graph);

	}

	/**
	 * adds the specified user to the network
	 * 
	 * @author Chase Flackey
	 * @param user the name of the user to be added to the network
	 */
	void addUser(String user) {
		graph.addVertex(user);
		log.add(ADD_COMMAND + " " + user);
	}

	/**
	 * removes the specified user from the network
	 * 
	 * @author Chase Flackey
	 * @param user the name of the user to be removed from the network
	 */
	void removeUser(String user) {
		graph.removeVertex(user);
		log.add(REMOVE_COMMAND + " " + user);
	}

	/**
	 * Makes user1 and user2 friends in the network
	 * 
	 * @author Chase Flackey
	 * @param user1 the name of one user to become friends
	 * @param user2 the name of the other user to become friends
	 */
	void addFriend(String user1, String user2) {
		graph.addEdge(user1, user2);
		log.add(ADD_COMMAND + " " + user1 + " " + user2);
	}

	/**
	 * ends the friendship between these two users
	 * 
	 * @author Chase Flackey
	 * @param user1 the name of one user in the network
	 * @param user2 the name of another user in the network
	 */
	void removeFriend(String user1, String user2) {
		graph.removeEdge(user1, user2);
		log.add(REMOVE_COMMAND + " " + user1 + " " + user2);
	}

	/**
	 * finds the number of connected groups in the network
	 * 
	 * @author Chase Flackey
	 * @return returns the number of connected groups in the network
	 */
	public int getGroups() {
		ArrayList<List<String>> conComponents = new ArrayList<List<String>>(graph.order());

		// First generates a list of connected vertices for each vertex in the
		// network.
		List<String> allUsers = graph.getAllVertices();
		for (String user : allUsers) {
			List<String> connected = new ArrayList<String>(graph.order());
			connected = getFriends(user, connected);
			// Each list is compared to a list of existing unique components. If the
			// list does not match an existing connected component, this list is
			// added to the unique components and the # of connected components is
			// incremented.
			Boolean duplicate = false;
			for (int i = 0; i < conComponents.size(); i++) {
				if (conComponents.get(i).containsAll(connected)) {
					duplicate = true;
				}
			}
			if (!duplicate)
				conComponents.add(connected);
		}
		return conComponents.size();
	}

	/**
	 * Private recursive method to a list of all names in a connected group
	 * 
	 * Example: If john -> amy -> joe (john is friends with amy, amy is friends with
	 * joe and john) then the returned list will contain john, amy, and joe.
	 * 
	 * @param user      the current vertex in the network
	 * @param connected a list of all previously visited users in the traversal
	 */
	public List<String> getFriends(String user, List<String> connected) {

		List<String> friends = allFriends(user);
		if (friends.size() == 0) {
			connected.add(user);
		}
		for (String friend : friends) {
			if (!connected.contains(friend)) {
				connected.add(friend);
				getFriends(friend, connected);
			}
		}
		return connected;
	}

	/**
	 * finds the list of mutual friends of user1 and user 2
	 * 
	 * @author Chase Flackey
	 * 
	 * @param user1 the name of a user in the graph
	 * @param user2 the name of another user in the graph
	 * @return returns a List of users who have user1 and user2 as friends
	 */
	public List<String> mutualFriends(String user1, String user2) {

		List<String> mutualFriends = new ArrayList<String>();
		List<String> user1friends = graph.getAdjacentVerticesOf(user1);
		List<String> user2friends = graph.getAdjacentVerticesOf(user2);

		for (String friend : user1friends) {
			if (user2friends.contains(friend))
				mutualFriends.add(friend);
		}
		return mutualFriends;
	}

	/**
	 * finds the shortest path between user1 and user2, using an implemenation of
	 * Dijkstra's algorithm
	 * 
	 * @author Chase Flackey
	 * 
	 * @param user1 the name of a user in the graph
	 * @param user2 the name of another user in the graph
	 * @return returns a List which is the shortest path from user1 to user2 in the
	 *         network
	 */
	public List<String> friendLink(String user1, String user2) {

		// HashMap<String, String>

		// TODO: Temporary implementation for GUI testing purposes
		return graph.getAdjacentVerticesOf(user1);

	}

	/**
	 * finds all friends of the specified user
	 * 
	 * @author Chase Flackey
	 * @param user the user whose friends will be found
	 * @return returns a list of people who are friends with user
	 */
	public List<String> allFriends(String user) {
		return graph.getAdjacentVerticesOf(user);
	}

	public String getCenterUser() {
		return centerUser;
	}

	/**
	 * makes the selected user the passed user if they're in the network
	 * 
	 * @author John Butler
	 * @param user the user to be selected
	 * @return returns true if the user was successfully selected, false otherwise
	 */
	public boolean select(String user) {
		List<String> allUsers = graph.getAllVertices();
		for (String currUser : allUsers) {

			if (currUser.equals(user)) {
				this.centerUser = user;

				// whoever is selected is supposed to be recorded in the log
				String command = SET_COMMAND + " " + user;
				log.add(command);
				return true;
			}
		}
		return false;
	}

	/**
	 * determines if the passed user was in the network before the last command
	 * executed. This is a helper function to undo()
	 * 
	 * @author John Butler
	 * @param user the user who may or may not have been added by the last command
	 * @return returns true if the user existed before the previous command, false
	 *         otherwise
	 */
	private boolean hasExisted(String user) {
		for (int i = log.size() - 2; i >= 0; i--) {
			String[] parameters = getParameters(log.get(i));

			if (parameters[0].equals(user)) {
				// if this character wasn't explicitly removed, the fact that he was referenced
				// means that he was in the graph because we check for that
				if (log.get(i).charAt(0) == REMOVE_COMMAND)
					if (parameters.length == 1)
						return false;
				return true;
			}

			if (parameters.length == 2 && parameters[1].equals(user))
				return true;
		}
		return false;
	}

	/**
	 * This looks at the last action logged and performs the opposite action. This
	 * undo is cyclic. i.e. if you add a connection and keep calling undo(), it will
	 * keep adding and removing that connection.
	 * 
	 * @return returns an empty string performed successfully, returns an error
	 *         message otherwise
	 */
	public String undo() {
		if (log.size() == 0)
			return "There was no action to undo";

		String lastCommand = log.get(log.size() - 1);
		String message = "";

		switch (lastCommand.charAt(0)) {
		case ADD_COMMAND:

			String[] parameters = getParameters(lastCommand);
			// if the last command was to add an edge
			if (parameters.length == 2) {

				// check whether adding the edge created each person. If it was, I can remove
				// the connection by removing the person
				boolean zeroExisted = hasExisted(parameters[0]);
				boolean oneExisted = hasExisted(parameters[1]);

				if (!zeroExisted)
					message += processCommand(REMOVE_COMMAND + " " + parameters[0]);

				if (!oneExisted) {
					if (!message.equals(""))
						message += "; ";
					message += processCommand(REMOVE_COMMAND + " " + parameters[1]);
				}

				// if both of those in the connection did exist, then I only have to remove the
				// connection
				if (oneExisted && zeroExisted) {
					String undoCommand = REMOVE_COMMAND + lastCommand.substring(1);
					message = processCommand(undoCommand);
				}

				if (!message.equals(""))
					message = "The previous command cannot be undone : " + lastCommand;
			} else {

				// the opposite of adding is removing
				String undoCommand = REMOVE_COMMAND + lastCommand.substring(1);
				message = processCommand(undoCommand);

				if (!message.equals(""))
					message = "The previous command cannot be undone: " + lastCommand;
			}
			break;
		case REMOVE_COMMAND:
			// the opposite of removing is adding
			lastCommand = ADD_COMMAND + lastCommand.substring(1);
			message = processCommand(lastCommand);

			if (message.equals(""))
				message = "the previous command cannot be undone: " + lastCommand;
			break;
		case SET_COMMAND:

			// I have to search for the last person who was selected
			for (int i = log.size() - 2; i >= 0; i--) {
				if (log.get(i).charAt(0) == SET_COMMAND) {

					// once found, select that person
					lastCommand = log.get(i);
					message = processCommand(lastCommand);

					// I don't actually check if the previous command was valid in this case, but I
					// don't think that there is any way for there to be an invalid command
					if (message.equals(""))
						log.add(lastCommand);
					return message;
				}
			}
			// if I never found another set command in the rest of the log
			message = "There was no one previously selected.";
			break;
		default:
			message = "invalid previous command: " + lastCommand;
			break;
		}
		return message;
	}

	/**
	 * helper function to processCommand(String), it parses a command and finds the
	 * parameters. This validates the input as well
	 * 
	 * @author John Butler
	 * 
	 * @param command the command to be parsed
	 * @return an array of the parameters if the command is valid, null otherwise
	 */
	private String[] getParameters(String command) {
		// makes sure there is a command to process
		if (command.equals(""))
			return null;
		String[] tokens = command.split(" ");

		// the command part must be one character only
		if (tokens[0].length() != 1)
			return null;

		// these are the only valid commands
		// I check for the correct number of parameters for the command as well:
		// add and remove can have one or two parameters, while set can only have one
		switch (tokens[0].charAt(0)) {
		case ADD_COMMAND:
		case REMOVE_COMMAND:
			if (tokens.length == 3) // command + two parameters
				return new String[] { tokens[1], tokens[2] };
		case SET_COMMAND:
			if (tokens.length == 2) // command + one parameter
				return new String[] { tokens[1] };
		default:
			break;
		}

		// this line is only reached if there is an invalid number of parameters
		return null;
	}

	/**
	 * Performs the operation specified in command on network in case we only want
	 * to use a temporary network.
	 * 
	 * @author John Butler
	 * @param command the command to be processed
	 * @return returns an error message if the command is invalid, otherwise returns
	 *         an empty string
	 */
	private String processCommand(String command) {
		
		String[] parameters = getParameters(command);
		if (parameters == null)
			return "unable to process parameters.";

		switch (command.charAt(0)) {
		case ADD_COMMAND:
			// the specific action taken is dependent on how many parameters there are
			if (parameters.length == 1)
				addUser(parameters[0]);
			else
				addFriend(parameters[0], parameters[1]);
			break;

		case REMOVE_COMMAND:
			// the specific action taken is dependent on how many parameters there are
			if (parameters.length == 1)
				removeUser(parameters[0]);
			else
				removeFriend(parameters[0], parameters[1]);
			break;

		case SET_COMMAND:
			select(parameters[0]);
			break;

		default:
			return command.charAt(0) + "' is an invalid comand";
		}
		return "";
	}

	/**
	 * replaces the current social network with what's found in the file. If the
	 * file is invalid, the social network is not affected. What is considered valid
	 * is specified here:
	 * https://canvas.wisc.edu/courses/170796/pages/team-project-social-network
	 * 
	 * @author John Butler
	 * @param file the path to a file containing the commands in plain text
	 * @return returns an error message if the data in the file is invalid, an empty
	 *         string otherwise
	 */
	public boolean readFromFile(String filename) {
		try {
			Scanner input = new Scanner(new File(filename));

			// I use a temporary variable so that I don't affect the real instance
			// if something goes wrong
			SocialNetwork tempNetwork = new SocialNetwork(this);

			// test that all of the commands don't cause errors
			int lineReading = 1;
			while (input.hasNext()) {

				String command = input.nextLine();
				String invalidCommandMessage = "invalid command at line " + lineReading++ + " in inputted file: \""
						+ command + '"';

				String message = tempNetwork.processCommand(command);

				// if there was an error message, return immediately
				if (!message.equals("")) {
					input.close();
					return false;
				}
			}

			// after we confirm that there were no errors, we can safely copy over the
			// temporary instances
			this.graph = tempNetwork.graph;
			this.log = tempNetwork.log;
			this.centerUser = tempNetwork.centerUser;

			
			//makes sure there was a selected user, even if there wasn't one specified
			if (centerUser == null) {
				List<String> people = graph.getAllVertices();
				if (!people.isEmpty())
					centerUser = people.get(0);
			}

			input.close();
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Getter for graph size
	 * 
	 * @return size
	 */
	public int getSize() {
	    return graph.order();
	}

	/**
	 * saves the log to the specified file name
	 * 
	 * @author John Butler
	 * @param filename the file name to save the log as
	 * @return returns true if the save was successful, false otherwise
	 */
	public boolean saveLog(String filename) {
		try {
			PrintWriter writer = new PrintWriter(filename);
			
			for (String command : log)
				writer.println(command);

			writer.close();
			
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
}
