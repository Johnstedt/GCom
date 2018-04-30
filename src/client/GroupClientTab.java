package client;

import group_management.Group;
import group_management.User;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import message_ordering.Message;
import utils.TimeFormat;

import java.util.Observable;
import java.util.Observer;

public class GroupClientTab implements Observer{
	private User self;
	private Button sendButton;
	private TextFlow chatOutputField;
	private ListView userList;
	private Group group;
	private Tab tab;
	private TextField chatInputField;
	private int textSize = 13;

	public GroupClientTab(Group g, User self, Tab tab) {
		this.group = g;
		this.self = self;
		this.tab = tab;
		//chatInputField = (TextField) tab.getContent().lookup("chatInputField");
		//chatInputField.setOnAction(this::onEnter);
		Node borderPane = tab.getContent().lookup("#chatGroupPane");

		ScrollPane chatInputScrollPane = (ScrollPane) borderPane.lookup("#chatInputScrollPane");
		chatOutputField = (TextFlow) chatInputScrollPane.getContent();

		ScrollPane userListScrollPane = (ScrollPane) borderPane.lookup("#userListScrollPane");
		userList = (ListView) userListScrollPane.getContent();
		userList.setOnMouseClicked(this::clickOnUser);
		//userList.setItems(group.getUsers());
		Node lowerPane = borderPane.lookup("#lowerFlowPane");

		sendButton = (Button) lowerPane.lookup("#sendButton");
		sendButton.setOnAction(this::onSendButton);
		chatInputField = (TextField) lowerPane.lookup("#chatInputField");
		chatInputField.setOnAction(this::onEnter);
		group.addObserver(this::update);
		setTextInChat(TimeFormat.getTimestamp(), "System", "Created Group "+g.getGroupName());
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

		chatOutputField.getChildren().addAll(text1, text2, text3, new Text(System.lineSeparator()));
	}


	private void onSendMessage(String msg) {
		setTextInChat(TimeFormat.getTimestamp(), Test.username, msg);
		group.send(msg, self);
	}

	private void onSendButton(ActionEvent actionEvent) {
		onSendMessage(chatInputField.getText());
		chatInputField.setText("");
	}

	public void onEnter(javafx.event.ActionEvent actionEvent) {
		onSendMessage(chatInputField.getText());
		chatInputField.setText("");
	}


	public void clickOnUser(MouseEvent click) {
		if (click.getClickCount() == 2) {
			chatInputField.setText("@"+userList.getSelectionModel().getSelectedItem()+" " + chatInputField.getText());
			chatInputField.requestFocus();
			chatInputField.end();
		}
	}

	@Override
	public void update(Observable observable, Object o) {
		if (o instanceof Message) {
			Message msg = (Message) o;
			setTextInChat(TimeFormat.getTimestamp(), "???", msg.getMsg());
			if (!tab.isSelected()) {
				tab.setText("!"+group.getGroupName());
			}
		} else {
			System.err.println("Dont know!" + o.getClass());
		}
	}
}
