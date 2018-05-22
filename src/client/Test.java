package client;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class Test extends Application {
	static String username = "";
	static int port = 0;
	static InetAddress ipaddress;

	public static void main(String[] args) {
		if (args.length == 0) {
			int tmpport = 1337;
			while (port == 0) {
				try {
					ServerSocket s = new ServerSocket(tmpport);
					port = s.getLocalPort();
					s.close();
				} catch (IOException e) {}
				tmpport++;
			}
			try {
				ipaddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			username = "U:"+ipaddress.getHostAddress()+":"+port;
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
