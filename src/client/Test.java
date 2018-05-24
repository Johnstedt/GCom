package client;


import group_management.CommunicationType;
import group_management.MessageOrderingType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmi.Receiver;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

public class Test extends Application {
	static String username = "";
	static int port = 0;
	static InetAddress ipaddress = null;
	static boolean isANameServer = true;

	static String autoNSName = "";
	static int autoNSPort = 0;
	static String autoCS = "";

	static String newGroup = "";
	static MessageOrderingType newGroupMO = null;
	static CommunicationType newGroupCO = null;
	public static Receiver receiver;

	/**
	 *
	 * @param args 0, 3, 6, 7
	 * 1 [0] [portnr] or FIND, FIND will find a new port from 1337 and up.
	 * 2 [1] [username] or GENERIC, Generic will give a generic username.
	 * 3 [2] [ns:True/False], True to use nameserver.
	 * 4 [3] [server:ip] not CREATE, ip or hostname for nameserver connecting to.
	 * 5 [4] [server:port], port for server connecting to.
	 * 6 [5] [groupname] (Optional) connect to group in given ns
	 *
	 * 1 [0] [portnr] or FIND, FIND will find a new port from 1337 and up.
	 * 2 [1] [username] or GENERIC, Generic will give a generic username.
	 * 3 [2] [ns:True/False], True to use nameserver.
	 * 4 [3] [CREATE], to create a chatgroup
	 * 5 [4] [groupname] name of chatgroup to create
	 * 6 [5] [MO] where MO is UO, CAUSAL, FIFO
	 * 7 [6] [CO] where CO is RM, UM, TREE
	 *
	 *             TODO: When not NS remove checkbox
	 */
	public static void main(String[] args) {

		if (args.length == 0 || args[0].equals("FIND")) {
			int tmpport = 1341;
			while (port == 0) {
				try {
					receiver = new Receiver(tmpport);
					port = tmpport;
				} catch (RemoteException e) {
					tmpport++;
				}
			}
		} else {
			port = Integer.valueOf(args[0]);
			ServerSocket s = null;
			try {
				receiver = new Receiver(port);
			} catch (IOException e) {
				System.err.println("Port "+port+" is already in use. Please select another, "+e.getMessage());
				System.exit(128);
			}
		}
		boolean b = false;
		while (!b) {
			try {
				ipaddress = InetAddress.getLocalHost();
				b = true;
			} catch (UnknownHostException e) {
				System.err.println("Waiting on Network to get Host data");
				try {
					Thread.sleep(500);
				} catch (InterruptedException ignore) {}
			}
		}
		username = "U:"+ipaddress.getHostName()+":"+port;

			if (args.length >= 2 && !args[1].equals("GENERIC")) {
				username = args[1];
			}
		if (args.length >= 3) {
			if (args[2].equals("False")) {
				isANameServer = false;
			}
			if (args.length >= 5 && !args[3].equals("CREATE")) {
				autoNSName = args[3];
				autoNSPort = Integer.valueOf(args[4]);
				if (args.length >= 6) {
					autoCS = args[5];
				}
				if (autoNSPort == 0 || autoNSName.length() == 0) {
					System.err.println("INVALID NS arguments "+args[3]+ " "+args[4]);
					System.exit(127);
				}
			} else if (args[3].equals("CREATE")) {
				if (args.length != 7) {
					System.err.println("Wrong number of arguments, should be 7 is "+args.length);
				}
				newGroup = args[4];
				switch (args[5]) {
					case "UO":
						newGroupMO = MessageOrderingType.UNORDERED;
						break;
					case "CASUAL":
						newGroupMO = MessageOrderingType.CAUSAL;
						break;
					case "FIFO":
						newGroupMO = MessageOrderingType.FIFO;
						break;
					default:
						System.err.println("Unknown Message Ordering Type "+args[5]);
						System.exit(127);
						break;
				}
				switch (args[6]) {
					case "RM":
						newGroupCO = CommunicationType.RELIABLE_MULTICAST;
						break;
					case "UM":
						newGroupCO = CommunicationType.UNRELIABLE_MULTICAST;
						break;
					case "TREE":
						newGroupCO = CommunicationType.TREE_MULTICAST;
						break;
					default:
						System.err.println("Got wrong CO for new group, "+args[6]);
						break;
				}
				if (newGroup.length() == 0 || newGroupCO == null || newGroupMO == null) {
					System.err.println("'CREATE new group' argument exception, "+args[4]+" "+args[5]+ " "+args[6]);
					System.exit(127);
				}
			}
		}
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(new File("src/client/client.fxml").toURL());
			Parent root = loader.load();
			Scene scene = new Scene(root, 750, 700);
			scene.getStylesheets().add(new File("src/client/style.css").toURL().toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ChatClient GCom " + username + "@" + ipaddress.getHostName()+":"+port);
			primaryStage.setMinWidth(750);
			primaryStage.setMinHeight(650);
			primaryStage.setWidth(750);
			primaryStage.setHeight(650);
			primaryStage.setMaximized(false);
			primaryStage.setFullScreen(false);
			primaryStage.setResizable(true);
			primaryStage.sizeToScene();
			ClientController controller = loader.getController();
			primaryStage.setOnCloseRequest(e -> {controller.closeAllTabs(); Platform.exit();System.exit(0);});


			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
