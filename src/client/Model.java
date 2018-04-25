package client;

import group_management.Group_Manager;
import group_management.User;

import java.util.Scanner;

public class Model {

	public static void main(String args[]) {

		int port = Integer.parseInt(args[0]);
		int connectPort = Integer.parseInt(args[1]);
		String nick = args[2];

		User u = new User(nick, "1.2.3.4.5", port);
		Group_Manager gm = new Group_Manager(u, connectPort);

		Scanner in = new Scanner(System.in);
		String input;

		while(true) {

			input = in.nextLine();
			gm.send(input);
		}

	}

}
