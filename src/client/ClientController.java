package client;

import debugger.DebuggerController;
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
import message.Message;
import utils.TimeFormat;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ClientController implements Observer {

	//Menu - File
	public CheckMenuItem fileMenuDebugItem;
	public MenuItem fileMenuCreateGroup;
	public MenuItem fileMenuConnectToNameServer;
	public MenuItem fileMenuCloseItem;

	//Toolbar
	public CheckBox isNameServer;
	public ToggleButton offDebuggerBtn;
	public ToggleButton debuggerOnBtn;
	public TreeView serverTree;
	private ListView<Node> systemTextList;

	//Tab
	public TabPane tabPane;
	public Tab systemTab;
	private BorderPane systemPane;

	//Other
	private LinkedList<GroupClientTab> tabChat = new LinkedList<>();
	private int textSize = 12;
	private User user;
	private GroupManager groupManager;


	public void initialize() {
		String host = Test.ipaddress.getHostName();
		String ip = Test.ipaddress.getHostAddress();

		user = new User(Test.username, ip, Test.port);
		groupManager = new GroupManager(user);
		groupManager.addObserver(this);

		//systemScroll = new ScrollPane();
		systemTextList = new ListView<>();
		//systemScroll.setContent(systemTextList);


		fileMenuCreateGroup.setAccelerator(
					KeyCombination.keyCombination("ALT+n")
		);
		fileMenuConnectToNameServer.setAccelerator(
					KeyCombination.keyCombination("ALT+c")
		);
		//Platform.runLater(()-> chatInputField.requestFocus());
		tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				//TODO: When focus remove "!" in tabname.
			}
		});


		//System tab

		systemPane = new BorderPane();

		//System tab - Tree servers
		TreeItem<String> dummyRoot = new TreeItem<>("Dummy");
		TreeItem<String> ns = new TreeItem<>("My NameServer");
		dummyRoot.getChildren().add(ns);

		TreeItem<String> nsgroups = new TreeItem<>("Chat1");
		ns.getChildren().add(nsgroups);

		serverTree = new TreeView();
		serverTree.setRoot(dummyRoot);
		serverTree.setShowRoot(false);
		systemPane.setRight(serverTree);
		//System tab - TextList (text output)
		systemPane.setCenter(systemTextList);
		systemTab.closableProperty().setValue(false);
		systemTab.setContent(systemPane);
		setTextInChat(TimeFormat.getTimestamp(),"System","Welcome "+Test.username+"");
		setTextInChat(TimeFormat.getTimestamp(),"System","Currently served at "+ ip+":"+String.valueOf(Test.port)+" ("+host+")");
		isNameServer(null);

	}


	public ClientController() {
		System.out.println("Create debugger");
		Parent root;
		try {
			root = FXMLLoader.load(new File("src/debugger/debugger.fxml").toURL());
			Stage stage = new Stage();
			DebuggerController.getDebugger().start(stage);
			stage.setTitle("My New Stage Title");
			stage.setScene(new Scene(root, 450, 450));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



	private synchronized void setTextInChat(String timestamp, String user, String msg) {
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
		Text text3 = new Text(": " + msg);
		text3.setFill(Color.BLACK);
		text3.setFont(Font.font("Helvetica", textSize));


		FlowPane field = new FlowPane();
		field.setPrefWrapLength(systemTextList.getWidth());
		field.getChildren().addAll(text1, text2, text3);
		Platform.runLater(()->systemTextList.getItems().addAll(field));


	}

	public void terminateProgram() {
		//TODO: gracefully close the connections etc.
		System.exit(0);
	}




	public void createGroup(ActionEvent actionEvent) {
		TwoFieldDialog cgd = new TwoFieldDialog();
		boolean valid = cgd.show("Create group", "Create group", "Name", "Description");
		//TODO: select communication, ordering etc.
		if (valid) {

			//TODO: Description?
			Group g = groupManager.createGroup(user, cgd.val1, MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
			Tab tab = addNewGroupTab(cgd.val1, cgd.val1);
			GroupClientTab gct = new GroupClientTab(g, groupManager.getSelf(), tab);
			tabChat.add(gct);
		}
	}

	public void connectToNameServer(ActionEvent actionEvent) {
		TwoFieldDialog cgd = new TwoFieldDialog();
		boolean valid = cgd.show("Connect to NameServer", "Connect to NameServer", "IP-address", "Port");
		if (valid) {
			//TODO: get groupname.
			Platform.runLater(()->setTextInChat(TimeFormat.getTimestamp(),"System","Connecting to NameServer "+cgd.val1+":"+cgd.val2));
			groupManager.askForGroups(cgd.val1, Integer.parseInt(cgd.val2));

		}
	}

	private Tab addNewGroupTab(String name, String id) {
		Tab newGroup = new Tab();
		newGroup.setText(name);
		newGroup.setId(id);
		try {
			newGroup.setContent(FXMLLoader.load(new File("src/client/chattab.fxml").toURL()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BorderPane outer = (BorderPane)newGroup.getContent().lookup("#chatGroupPane");
		tabPane.getTabs().add(newGroup);
		tabPane.getSelectionModel().select(newGroup);
		Platform.runLater(outer::autosize);

		return newGroup;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == null) {
			System.err.println("CLIENTCONTROLLER, update, arg is null?");
			return;
		}
		System.out.println("ClientControl update:"+arg.getClass().toString());
		if (arg instanceof Message) {
			Message msg = (Message) arg;
			switch (msg.getType()) {
				case SEND_GROUPS:
					nameServerDialog((HashMap<String, Group>) msg.getMsg());
					break;
				case ASK_GROUPS:
					setTextInChat(TimeFormat.getTimestamp(), "NameServer", String.valueOf(msg.getFrom())+ " is asking for groups");
				case TEXT:
					//setTextInChat(TimeFormat.getTimestamp(), String.valueOf(msg.getFrom()), (String) msg.getMsg());
					break;
				default:
					setTextInChat(TimeFormat.getTimestamp(), String.valueOf(msg.getFrom()), msg.getType() +":"+msg.getMsg().getClass().toString());
					break;
			}



		}
	}

	private void nameServerDialog(HashMap<String, Group> hm) {
		List<String> dialogData = new ArrayList<>();
		Platform.runLater(()->setTextInChat(TimeFormat.getTimestamp(),"NameServer","Returned with "+(hm.size()-1)+" groups."));
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
					groupManager.addGroup(g);
					Tab tab = addNewGroupTab(g.getGroupName(), g.getGroupName());
					GroupClientTab gct = new GroupClientTab(g, groupManager.getSelf(), tab);
					tabChat.add(gct);
				}
			});
		}
	}

	public void debugCheckbox(ActionEvent actionEvent) {
		if (fileMenuDebugItem.isSelected()) {
			debugOn(actionEvent);
		} else {
			debugOff(actionEvent);
		}
	}

	public void debugOn(ActionEvent actionEvent) {
		offDebuggerBtn.setSelected(false);
		debuggerOnBtn.setSelected(true);
		DebuggerController.getDebugger().show();
	}

	public void debugOff(ActionEvent actionEvent) {
		offDebuggerBtn.setSelected(true);
		debuggerOnBtn.setSelected(false);
		DebuggerController.getDebugger().hide();
	}

	public void isNameServer(ActionEvent actionEvent) {
		if (isNameServer.isSelected()) {
			setTextInChat(TimeFormat.getTimestamp(),"System","Initialize NameServer");
			groupManager.createGroup("init", MessageOrderingType.UNORDERED, CommunicationType.UNRELIABLE_MULTICAST);
		} else {
			groupManager.remove_group("init");
			setTextInChat(TimeFormat.getTimestamp(),"System","Closing down NameServer");
		}
	}
}
