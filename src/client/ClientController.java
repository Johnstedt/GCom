package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import utils.TimeFormat;

import java.io.File;
import java.io.IOException;

public class ClientController {
	public MenuItem fileMenuCloseItem;
	public CheckMenuItem fileMenuDebugItem;
	public TextField chatInputField;
	public ListView users;
	public TextFlow chatOutputField;
	public ScrollPane chatInputScrollPane;
	public Button sendButton;

	private DebuggerController debugger = null;
	int textSize = 13;


	public void initialize() {
		Platform.runLater(()-> chatInputField.requestFocus());
		Platform.runLater(()->chatInputScrollPane.vvalueProperty().bind(chatOutputField.heightProperty()));

		setTextInChat("","System","Welcome "+Test.username);


	}
	public ClientController() {

	}

	public void onSendButton() {
		setTextInChat(TimeFormat.getTimestamp(), Test.username, chatInputField.getText());
		chatInputField.setText("");
	}


	public void onEnter(javafx.event.ActionEvent actionEvent) {
		setTextInChat(TimeFormat.getTimestamp(), Test.username, chatInputField.getText());
		chatInputField.setText("");
	}


	private void setTextInChat(String timestamp, String user, String msg) {
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

		chatOutputField.getChildren().addAll(text1, text2, text3, new Text("\n"));
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
}
