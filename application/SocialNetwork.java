package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocialNetwork {

	// the user being displayed
	//TODO hardcoded
	private UserStruct selectedUser = new User("John Doe");

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
	 * adds the specified user to the network
	 * 
	 * @param user the name of the user to be added to the network
	 */
	void addUser(String user) {
		// TODO
		log.add(ADD_COMMAND + " " + user);
	}

	/**
	 * removes the specified user from the network
	 * 
	 * @param user the name of the user to be removed from the network
	 */
	void removeUser(String user) {
		// TODO
		log.add(REMOVE_COMMAND + " " + user);
	}

	/**
	 * Makes user1 and user2 friends in the network
	 * 
	 * @param user1 the name of one user to become friends
	 * @param user2 the name of the other user to become friends
	 */
	void addFriend(String user1, String user2) {
		// TODO
		log.add(ADD_COMMAND + " " + user1 + " " + user2);
	}

	/**
	 * ends the friendship between these two users
	 * 
	 * @param user1 the name of one user in the network
	 * @param user2 the name of another user in the network
	 */
	void removeFriend(String user1, String user2) {
		// TODO
		log.add(REMOVE_COMMAND + " " + user1 + " " + user2);
	}

	/**
	 * finds the number of connected groups in the network
	 * 
	 * @return returns the number of connected groups in the network
	 */
	public int getGroups() {
		// TODO
		return 0;
	}

	/**
	 * finds the list of mutual friends of user1 and user 2
	 * 
	 * @param user1 the name of a user in the graph
	 * @param user2 the name of another user in the graph
	 * @return returns a List of users who have user1 and user2 as friends
	 */
	public List<UserStruct> mutualFriends(String user1, String user2) {
		// TODO
		return null;
	}

	/**
	 * finds the shorted path between user1 and user2
	 * 
	 * @param user1 the name of a user in the graph
	 * @param user2 the name of another user in the graph
	 * @return returns a List which is the shortest path from user1 to user2 in the
	 *         network
	 */
	public List<UserStruct> friendLink(String user1, String user2) {
		// TODO
		return null;
	}

	/**
	 * finds all friends of the specified user
	 * 
	 * @param user the user whose friends will be found
	 * @return returns a list of people who are friends with user
	 */
	public List<UserStruct> allFriends(String user) {
		// TODO
		return null;
	}

	public String getSelected() {
		return selectedUser.getName();
	}

	//TODO we might want to select the user by the actual instance instead of their name.
	public boolean select(String user) {
		if(graph.contains(new User(user))) {
			//TODO
		}
		return false;
	}

	// This undo is cyclic. i.e. if you add a connection and keep calling undo(), it
	// will keep adding and removing that connection
	public String undo() {
		if (log.size() == 0)
			return "There was no action to undo";
		
		String lastCommand = log.get(log.size() - 1);
		String message = "";
		
		switch (lastCommand.charAt(0)) {
		case ADD_COMMAND:
			// the opposite of adding is removing
			lastCommand = REMOVE_COMMAND + lastCommand.substring(1);
			
			// TODO I don't check remove the person if they were added because of the
			// connection
			
			message = processCommand(lastCommand);

			// if the last command was invalid, it will be stopped here
			if (message.equals(""))
				log.add(lastCommand);
			return "The previous command was invalid: " + lastCommand;

		case REMOVE_COMMAND:
			// the opposite of removing is adding
			lastCommand = ADD_COMMAND + lastCommand.substring(1);
			message = processCommand(lastCommand);

			// if the last command was invalid, it will be stopped here
			if (message.equals(""))
				log.add(lastCommand);
			return "the previous command was invalid: " + lastCommand;

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
			//if I never found another set command in the rest of the log
			return "There was no one previously selected.";
		default:
			return "invalid previous command: " + lastCommand;
		}
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

	private String processCommand(String command) {
		return processCommand(command, this);
	}

	/**
	 * Performs the operation specified in command on network in case we only want
	 * to use a temporary network.
	 * 
	 * @author John Butler
	 * 
	 * @param command the command to be processed
	 * @param network the network to apply the command to
	 * @return returns an error message if the command is invalid, otherwise returns
	 *         an empty string
	 */
	private String processCommand(String command, SocialNetwork network) {

		String[] parameters = getParameters(command);

		// if the data does not get processed correctly,
		// getParameters(String) returns null
		if (parameters == null)
			return "unable to process parameters.";

		// I already checked for the number of parameters in getParameters(String), so I
		// don't have to worry about that here
		switch (command.charAt(0)) {
		case ADD_COMMAND:
			// the specific action taken is dependent on how many parameters there are
			if (parameters.length == 1)
				network.addUser(parameters[0]);
			else
				network.addFriend(parameters[0], parameters[1]);
			break;
		case REMOVE_COMMAND:
			// the specific action taken is dependent on how many parameters there are
			if (parameters.length == 1)
				network.removeUser(parameters[0]);
			else
				network.removeFriend(parameters[0], parameters[1]);

			break;
		case SET_COMMAND:
			selectedUser = new User(parameters[0]);
			break;
		default:
			// getParameters(String) already validates the command input, but just in case
			return command.charAt(0) + "' is an invalid comand";
		}
		return "";
	}

	/**
	 * replaces the current social network with what's found in the file. If the
	 * file is invalid, the social network is not overwritten. What is considered
	 * valid is specified here:
	 * https://canvas.wisc.edu/courses/170796/pages/team-project-social-network
	 * 
	 * @author John Butler
	 * 
	 * @param file the path to a file containing the commands in plain text
	 * @return returns an error message if the data in the file is invalid, an empty
	 *         string otherwise
	 */
	private String readFromFile(String filename) {
		// TODO I'm waiting to test this until SocialNetwork is done
		try {
			Scanner input = new Scanner(new File(filename));
			// I use these temporary variables so that I don't overwrite the real variables
			// if something goes wrong
			SocialNetwork tempNetwork = new SocialNetwork();
			ArrayList<String> tempLog = new ArrayList<String>();

			int currentLine = 1;
			while (input.hasNext()) {
				String command = input.nextLine();
				String invalidCommandMessage = "invalid command at line " + currentLine++ + " in inputted file: \""
						+ command + '"';
				tempLog.add(command);
				String message = processCommand(command, tempNetwork);
				if (!message.equals("")) {
					input.close();
					return invalidCommandMessage + ", " + message;
				}
			}

			// after we confirm that there were no errors, we can safely process the
			// commands on this instance
			log = tempLog;
			for (String command : log)
				processCommand(command);

			input.close();
		} catch (FileNotFoundException e) {
			return "File \"" + filename + "\" not found.";
		}
		return "";
	}

	/**
	 * 
	 * @author John Butler
	 * 
	 * @param filename
	 * @return returns true if the save was successful, false otherwise
	 */
	private boolean saveLog(String filename) {
		// TODO I'm waiting to test this until SocialNetwork is done

		PrintWriter writer;
		try {
			writer = new PrintWriter(filename);
			for (String command : log)
				writer.println(command);

			writer.close();
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
}