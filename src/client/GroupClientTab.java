package client;

import group_management.Group;
import group_management.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import message.Message;
import utils.TimeFormat;

import java.util.Observable;
import java.util.Observer;

public class GroupClientTab implements Observer{
	private User self;
	private Button sendButton;
	private ListView<Node> chatOutputField;
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

		chatOutputField = (ListView<Node>) borderPane.lookup("#chatOutputField");

		userList = (ListView) borderPane.lookup("#userList");
		userList.setOnMouseClicked(this::clickOnUser);
		Node lowerPane = borderPane.lookup("#lowerFlowPane");

		sendButton = (Button) lowerPane.lookup("#sendButton");
		sendButton.setOnAction(this::onSendButton);
		chatInputField = (TextField) lowerPane.lookup("#chatInputField");
		chatInputField.setOnAction(this::onEnter);
		group.addObserver(this::update);
		setTextInChat(TimeFormat.getTimestamp(), "System", "Joined group "+g.getGroupName());
		userList.setItems(FXCollections.observableArrayList(group.getUsers()));
		chatOutputField.autosize();
		Platform.runLater(()->chatInputField.requestFocus());

	}

	private void userListChange(Object u) {

	}


	private synchronized void setTextInChat(String timestamp, String user, String msg) {
		msg = msg.trim();
		if (msg.length() == 0)
			return;
		Text text1 = new Text(timestamp+" ");
		text1.setFill(Color.GREEN);
		text1.setFont(Font.font("Helvetica", textSize));

		Text text2 = new Text(user);
		text2.setFill(Color.BLUE);
		if (user.equals("Client") || user.equals("System") || user.equals(Test.username))
			text2.setFill(Color.RED);
		text2.setFont(Font.font( "Helvetica", FontWeight.BOLD, textSize));
		Text text3 = new Text(": " + msg);
		text3.setFill(Color.BLACK);
		text3.setFont(Font.font("Helvetica", textSize));
		FlowPane field = new FlowPane();
		field.setPrefWrapLength(chatOutputField.getWidth());
		field.getChildren().addAll(text1, text2, text3);

		Platform.runLater(()->chatOutputField.getItems().addAll(field));
		Platform.runLater( () ->chatOutputField.scrollTo(chatOutputField.getItems().size()-1) );

	}


	private void onSendMessage(String msg) {
		//setTextInChat(TimeFormat.getTimestamp(), Test.username, msg);
		group.send(msg, self);
	}

	private void onSendButton(ActionEvent actionEvent) {
		onSendMessage(chatInputField.getText());
		chatInputField.setText("");
	}

	private void onEnter(javafx.event.ActionEvent actionEvent) {
		onSendMessage(chatInputField.getText());
		chatInputField.setText("");
	}


	private void clickOnUser(MouseEvent click) {
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
			setTextInChat(TimeFormat.getTimestamp(), msg.getFrom().getNickname(), msg.getMsg());
			if (!tab.isSelected()) {
				tab.setText("!"+group.getGroupName());
			}
		} else if (o instanceof User) {
			userList.setItems(FXCollections.observableArrayList(group.getUsers()));
		} else {
			System.err.println("Dont know!" + o.getClass());
		}
	}
}
