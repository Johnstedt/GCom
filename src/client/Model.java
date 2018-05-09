package client;

public class Model {

	public static void main(String args[]) {

		/*int port = Integer.parseInt(args[0]);
		String connectHost = args[1];
		int connectPort = Integer.parseInt(args[2]);
		String nick = args[3];

		String[] arguments = new String[] {"/bin/bash", "-c", "hostname --ip-address"};
		String ip = null;
		try {
			Process proc = new ProcessBuilder(arguments).start();
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(proc.getInputStream()));

			String line = "";
			while((line = reader.readLine()) != null) {
				ip = line.substring(0,line.indexOf(" "));
				System.out.println(ip);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		User u = new User(nick, ip, port);
		GroupManager gm = new GroupManager(u, connectHost, connectPort);

		Scanner in = new Scanner(System.in);
		String input;

		while(true) {

			input = in.nextLine();
			gm.send( "thegroup", input);
		}
*/
	}

}
