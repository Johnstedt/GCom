package client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;

public class Test extends Application {
	static String username = "";
	static int port = 0;
	static InetAddress ipaddress;

	public static void main(String[] args) {
		if (args.length == 0) {
			/*Scanner reader = new Scanner(System.in);
			System.out.println("What is your username");
			username = reader.nextLine();
			reader.close();*/
			username = "JJ";
			try {
				ServerSocket s = new ServerSocket(0);
				port = s.getLocalPort();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				ipaddress = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = new File("src/client/client.fxml").toURL();
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root, 600, 400);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Hello You");
			primaryStage.setMinWidth(600);
			primaryStage.setMinHeight(400);
			primaryStage.setWidth(600);
			primaryStage.setHeight(400);
			primaryStage.setMaximized(false);
			primaryStage.setFullScreen(false);
			primaryStage.setResizable(true);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
