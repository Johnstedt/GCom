package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
	int textSize = 13;

	@FXML
	public TextField txt;
	public ListView users;
	public TextFlow chatInputField;
	public ScrollPane chatInputScrollPane;
	public Button sendButton;


	public void initialize() {
		Platform.runLater(()->txt.requestFocus());
		Platform.runLater(()->chatInputScrollPane.vvalueProperty().bind(chatInputField.heightProperty()));

		setTextInChat("","System","Welcome "+Test.username);


	}
	public Controller() {

	}

	public void onSendButton() {
		setTextInChat(getTimestamp(), Test.username, txt.getText());
		txt.setText("");
	}


	public void onEnter(javafx.event.ActionEvent actionEvent) {
		setTextInChat(getTimestamp(), Test.username, txt.getText());
		txt.setText("");
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

		chatInputField.getChildren().addAll(text1, text2, text3, new Text("\n"));
	}

	public void clickOnUser(MouseEvent click) {
		if (click.getClickCount() == 2) {
			txt.setText("@"+users.getSelectionModel().getSelectedItem()+" " + txt.getText());
			txt.requestFocus();
			txt.end();
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
			setTextInChat(getTimestamp(), user, msg);
		}
	}

	public static String getTimestamp() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());

	}


}
