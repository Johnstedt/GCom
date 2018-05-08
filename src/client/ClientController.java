package client;

import group_management.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import utils.TimeFormat;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ClientController implements Observer {

	public CheckMenuItem fileMenuDebugItem;
	public MenuItem fileMenuCreateGroup;
	public MenuItem fileMenuConnectToGroup;
	public MenuItem fileMenuCloseItem;
	public TabPane tabPane;
	public Tab systemTab;
	private ListView<Node> systemTextFlow;

	private DebuggerController debugger = null;
	private int textSize = 13;
	private User user;
	private Group_Manager groupManager;
	private LinkedList<GroupClientTab> tabChat = new LinkedList<>();

	public void initialize() {
		String host = Test.ipaddress.getHostName();
		String ip = Test.ipaddress.getHostAddress();

		user = new User(Test.username, ip, Test.port);
		groupManager = new Group_Manager(user);
		groupManager.addObserver(this);

		//systemScroll = new ScrollPane();
		systemTextFlow = new ListView<>();
		//systemScroll.setContent(systemTextFlow);


		fileMenuCreateGroup.setAccelerator(
					KeyCombination.keyCombination("ALT+n")
		);
		fileMenuConnectToGroup.setAccelerator(
					KeyCombination.keyCombination("ALT+c")
		);
		//Platform.runLater(()-> chatInputField.requestFocus());
		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				//TODO: When focus remove "!" in tabname.
			}
		});
		Platform.runLater(()->systemTab.setContent(systemTextFlow));
		systemTextFlow.setPrefSize(440, 440);


		Platform.runLater(()->setTextInChat(systemTextFlow, TimeFormat.getTimestamp(),"System","Welcome "+Test.username+""));
		Platform.runLater(()->setTextInChat(systemTextFlow, TimeFormat.getTimestamp(),"System","Currently served at "+ ip+":"+String.valueOf(Test.port)+" ("+host+")"));
		}


	public ClientController() {

		}



	private synchronized void setTextInChat(ListView<Node> pane, String timestamp, String user, String msg) {
		msg = msg.trim();
		if (msg.length() == 0)
			return;
		Text text1 = new Text(timestamp+" ");
		text1.setFill(Color.GREEN);
		text1.setFont(Font.font("Helvetica", textSize));

		Text text2 = new Text(user);
		if (user.length() == 0)
			text2 = new Text();
		text2.setFill(Color.BLUE);
		if (user.equals("Client") || user.equals("System") || user.equals(Test.username))
			text2.setFill(Color.RED);
		text2.setFont(Font.font( "Helvetica", FontWeight.BOLD, textSize));
		Text text3 = new Text("  " + msg);
		text3.setFill(Color.BLACK);
		text3.setFont(Font.font("Helvetica", textSize));

		FlowPane field = new FlowPane();
		field.getChildren().addAll(text1, text2, text3);
		pane.getItems().addAll(field);
		//Platform.runLater(()->systemScroll.vvalueProperty().bind(systemTextFlow.heightProperty()));
		//Platform.runLater(()->systemScroll.setFitToWidth(true));

	}

	public void terminateProgram() {
		//TODO: gracefully close the connections etc.
		System.exit(0);
	}


	public void debugCheckbox(ActionEvent actionEvent) {
		if (fileMenuDebugItem.isSelected()) {
			if (debugger == null) {
				debugger = new DebuggerController();
				System.err.println("Do it");
				Parent root;
				try {
					root = FXMLLoader.load( new File("src/client/debugger.fxml").toURL());
					Stage stage = new Stage();
					stage.setTitle("My New Stage Title");
					stage.setScene(new Scene(root, 450, 450));
					stage.show();
					// Hide this current window (if this is what you want)
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			debugger.terminate();
			debugger = null;
		}
	}

	public void createGroup(ActionEvent actionEvent) {
		TwoFieldDialog cgd = new TwoFieldDialog();
		boolean valid = cgd.show("Create group", "Create group", "Name", "Description");
		//TODO: select communication, ordering etc.
		if (valid) {

			//TODO: Description?
			Group g = groupManager.create_group(user, cgd.val1, MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
			Tab tab = addNewGroupTab(cgd.val1, cgd.val1);
			GroupClientTab gct = new GroupClientTab(g, groupManager.getSelf(), tab);
			tabChat.add(gct);
		}
	}

	public void connectToGroup(ActionEvent actionEvent) {
		TwoFieldDialog cgd = new TwoFieldDialog();
		boolean valid = cgd.show("Connect to group", "Connect to group", "IP-address", "Port");
		if (valid) {
			//TODO: get groupname.
			System.out.println("I WILL ASK FOR GROUP");
			groupManager.askForGroups(cgd.val1, Integer.parseInt(cgd.val2));
			//tabChat.add(gct);
		}
	}

	private Tab addNewGroupTab(String name, String id) {
		Tab newGroup = new Tab();
		newGroup.setText(name);
		newGroup.setId(id);
		try {
			newGroup.setContent(FXMLLoader.load(this.getClass().getResource("chattab.fxml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BorderPane outer = (BorderPane)newGroup.getContent().lookup("#chatGroupPane");
		Platform.runLater(()->outer.setPrefHeight(0.9));
		Platform.runLater(()->outer.setPrefWidth(0.9));
		tabPane.getTabs().add(newGroup);
		tabPane.getSelectionModel().select(newGroup);
		return newGroup;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("ClientControl update:"+arg.getClass().toString());
		if (arg instanceof HashMap) {
			HashMap<String, Group> hm = (HashMap) arg;
			List<String> dialogData = new ArrayList<>();
			for (String s : hm.keySet()) {
				if (!s.equals("init")) {
					Group g = hm.get(s);
					if (!groupManager.alreadyInGroup(s))  {
						dialogData.add(s);
					}
				}
			}
			if (dialogData.size()>0) {
				Platform.runLater(()-> {
					ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
					dialog.setTitle("Select group");
					dialog.setHeaderText("Select your choice");
					dialog.setWidth(400);
					dialog.setHeight(400);

					Optional<String> result = dialog.showAndWait();

					if (result.isPresent()) {
						Group g = hm.get(result.get());
						groupManager.addGroup(g.getGroupName(), g);
						Tab tab = addNewGroupTab(g.getGroupName(), g.getGroupName());
						GroupClientTab gct = new GroupClientTab(g, groupManager.getSelf(), tab);
						tabChat.add(gct);
					}
				});
			}

		}
	}
}
