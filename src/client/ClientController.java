package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class ClientController {

	public CheckMenuItem fileMenuDebugItem;
	public MenuItem fileMenuCreateGroup;
	public MenuItem fileMenuConnectToGroup;
	public MenuItem fileMenuCloseItem;

	public TabPane tabPane;
	public Tab systemTab;


	private DebuggerController debugger = null;
	int textSize = 13;


	public void initialize() {
		ScrollPane systemScroll = new ScrollPane();
		FlowPane systemFlow = new FlowPane();
		systemScroll.setContent(systemFlow);

		Platform.runLater(()->systemTab.setContent(systemScroll));
		Platform.runLater(()->systemScroll.vvalueProperty().bind(systemFlow.heightProperty()));
		Platform.runLater(()->setTextInChat(systemFlow, TimeFormat.getTimestamp(),"System","Welcome "+Test.username));
		//Platform.runLater(()-> chatInputField.requestFocus());



	}
	public ClientController() {

	}



	private void setTextInChat(FlowPane pane, String timestamp, String user, String msg) {
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

		pane.getChildren().addAll(text1, text2, text3, new Text("\n"));
	}

	/*
	public void onSendButton() {
		setTextInChat(TimeFormat.getTimestamp(), Test.username, chatInputField.getText());
		chatInputField.setText("");
	}


	public void onEnter(javafx.event.ActionEvent actionEvent) {
		setTextInChat(TimeFormat.getTimestamp(), Test.username, chatInputField.getText());
		chatInputField.setText("");
	}

	public void clickOnUser(MouseEvent click) {
		if (click.getClickCount() == 2) {
			chatInputField.setText("@"+users.getSelectionModel().getSelectedItem()+" " + chatInputField.getText());
			chatInputField.requestFocus();
			chatInputField.end();
		}
	}



	private int tmpUsers = 1;
	public void addUser() {
		for (int i = 0; i < 10; i++) {
			users.getItems().add(tmpUsers - 1, "User"+ tmpUsers++);
		}
	}

	private int tmpchatmsgs = 1;
	public void AddChatText() {
		for (int i = 0; i < 20; i++) {
			String user = "";
			if (users.getItems().size() > 0) {
				user = (String) users.getItems().get(tmpchatmsgs % users.getItems().size());
			}
			String msg = "Chat a message "+tmpchatmsgs++;
			setTextInChat(TimeFormat.getTimestamp(), user, msg);
		}
	}
	*/

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
		cgd.show("Create group", "Create group", "Name", "Description");
		//TODO: select communication, ordering etc.

	}

	public void connectToGroup(ActionEvent actionEvent) {
		TwoFieldDialog cgd = new TwoFieldDialog();
		boolean valid = cgd.show("Connect to group", "Connect to group", "IP-address", "Port");
		if (valid) {
			//TODO: get groupname.
			Tab newGroup = new Tab();
			newGroup.setText(cgd.val1+":"+cgd.val2);

			/*SplitPane outerPane = new SplitPane();
			outerPane.setOrientation(Orientation.VERTICAL);
			outerPane.setDividerPositions(0.9);

			SplitPane innerPane = new SplitPane();
			innerPane.setOrientation(Orientation.HORIZONTAL);
			innerPane.setDividerPositions(0.75);

			ScrollPane chatScrollPane = new ScrollPane();
			FlowPane chatFlowPane = new FlowPane();
			chatScrollPane.setContent(chatFlowPane);

			ScrollPane userScrollPane = new ScrollPane();
			ObservableList<String> names = FXCollections.observableArrayList(
						"Julia", "Ian", "Sue", "Matthew", "Hannah", "Stephan", "Denise");
			ListView userList = new ListView(names);
			userScrollPane.setContent(userList);
			innerPane.getItems().add(chatScrollPane);
			innerPane.getItems().add(userScrollPane);
			outerPane.getItems().add(innerPane);

			HBox downer = new HBox();
			TextField input = new TextField();
			input.setPromptText("Write to chat!");
			Button sendBtn = new Button();
			sendBtn.setText("Send");
			downer.getChildren().add(input);
			downer.getChildren().add(sendBtn);

			outerPane.getItems().add(downer);



			Platform.runLater(()->newGroup.setContent(outerPane));
			Platform.runLater(()->userList.setMinHeight(innerPane.heightProperty().doubleValue()));

			Platform.runLater(()->chatScrollPane.vvalueProperty().bind(chatFlowPane.heightProperty()));
			Platform.runLater(()->userScrollPane.vvalueProperty().bind(userList.heightProperty()));
			Platform.runLater(()->setTextInChat(chatFlowPane, TimeFormat.getTimestamp(),"System","Connecting "+newGroup.getText()+".."));
			*/
			try {
				newGroup.setContent(FXMLLoader.load(this.getClass().getResource("chattab.fxml")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			BorderPane outer = (BorderPane)newGroup.getContent().lookup("#chatGroupPane");
			Platform.runLater(()->outer.setPrefHeight(0.9));
			Platform.runLater(()->outer.setPrefWidth(0.9));
			tabPane.getTabs().add(newGroup);

		}
	}
}
