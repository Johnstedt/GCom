package debugger;

import group_management.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import message.InternalMessage;
import message.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class DebuggerController extends Application{

	private Stage classStage = new Stage();
	private static DebuggerController dc;

	public static DebuggerController getDebugger(){
		if (dc == null) {
			dc = new DebuggerController();
		}
		return dc;
	}

	public DebuggerController() {

	}

	@Override
	public void start(Stage stage) {
			classStage = stage;
	}

	public void terminate() {

	}

	public void show() {
		classStage.show();
	}

	public void hide() {
		classStage.hide();
	}

	public void setQueues(InternalMessage msg, String groupName, BlockingQueue<Message> fromReceiverBeforeDebugger, BlockingQueue<Message> fromReceiverAfterDebugger, BlockingQueue<Message> toSenderBeforeDebugger, BlockingQueue<Message> toSenderAfterDebugger) {
		CommunicationQueues cq = new CommunicationQueues(fromReceiverBeforeDebugger, fromReceiverAfterDebugger, toSenderBeforeDebugger, toSenderAfterDebugger);
		boolean wasShowing = true;
		if (!classStage.isShowing()) {
			Platform.runLater(()->classStage.show());
			wasShowing = false;

		}
		Platform.runLater(()->createDebuggingWindowForGroup(cq, groupName, msg));

		if (!wasShowing) {
			Platform.runLater(()->classStage.hide());
		}
	}

	private void createDebuggingWindowForGroup(CommunicationQueues cq, String groupName, InternalMessage msg) {
		if (groupName.equals("init")) {
			return;
		}
		AnchorPane p = new AnchorPane();

		try {
			p.getChildren().add(FXMLLoader.load(new File("src/debugger/oneChat.fxml").toURL()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		AnchorPane aPane = (AnchorPane) classStage.getScene().lookup("#anchorPane");
		TabPane chatSplitPane = (TabPane) aPane.lookup("#vboxPane").lookup("#chatSplitPane");

		Tab t = new Tab();

		t.setContent(p);


		t.setText(groupName);
		chatSplitPane.getTabs().add(t);

		BorderPane bp = (BorderPane) p.lookup("#borderPane");

		FlowPane topFields = (FlowPane) bp.lookup("#topFields");

		Text groupNameTextField = (Text) topFields.lookup("#groupNameTxtField");
		groupNameTextField.setText(groupName);

		Text orderTextField = (Text) topFields.lookup("#orderTypeTxtField");
		orderTextField.setText(msg.ot.toString());

		Text comTextField = (Text) topFields.lookup("#comTypeTxtField");
		comTextField.setText(msg.ct.toString());


		SplitPane splitPane = (SplitPane) bp.lookup("#splitVertical");
		splitPane.applyCss();
		AnchorPane messageCount = (AnchorPane) splitPane.lookup("#messageCount");

		AnchorPane splitAnchorInDebug = (AnchorPane) splitPane.lookup("#said");
		SplitPane splitAnchorSplitPane = (SplitPane) splitAnchorInDebug.lookup("#splitAnchorSplitInDebug");

		AnchorPane sendAnchorPane = (AnchorPane) splitAnchorSplitPane.lookup("#sendAnchorPane");
		AnchorPane recAnchorPane = (AnchorPane) splitAnchorSplitPane.lookup("#recAnchorPane");

		FlowPane sendFlowPane = (FlowPane) sendAnchorPane.lookup("#sendFlowPane");
		FlowPane recFlowPane = (FlowPane) recAnchorPane.lookup("#recFlowPane");
		sendFlowPane.applyCss();
		recFlowPane.applyCss();
		for (Node n : recFlowPane.getChildren()) {
			System.err.println("recFlowPane -> "+n.getId());
		}

		ListView<MessageDebug> recMsgList = (ListView<MessageDebug>) recFlowPane.lookup("#recListAtDebug");
		ListView<MessageDebug> sendMsgList = (ListView<MessageDebug>) sendFlowPane.lookup("#sendListAtDebug");
		ListView<MessageCounter.Counts> msgCount = (ListView<MessageCounter.Counts>) messageCount.lookup("#messageListCount");
		cq.setMsgLists(recMsgList, sendMsgList, msgCount);

		Button recBtn = (Button) recFlowPane.lookup("#receiverBtn");
		Button recFlushBtn = (Button) recFlowPane.lookup("#recFlush");


		recBtn.setOnMouseClicked(mouseEvent -> {
			String currentText = recBtn.getText();
			switch (currentText){
				case "Auto":
					recBtn.setText("Man");
					cq.setHoldFromReceiver(true);
					recFlushBtn.setDisable(false);
					break;
				default:
					recBtn.setText("Auto");
					cq.setHoldFromReceiver(false);
					recFlushBtn.setDisable(true);
					break;
			}
		});

		recFlushBtn.setOnMouseClicked(mouseEvent -> {
			cq.flushToReceiver();
		});


		Button senderBtn = (Button) sendFlowPane.lookup("#senderBtn");
		Button senderFlushBtn = (Button) sendFlowPane.lookup("#sendFlush");
		senderBtn.setOnMouseClicked(mouseEvent -> {
			String currentText = senderBtn.getText();
			switch (currentText){
				case "Auto":
					senderBtn.setText("Man");
					cq.setHoldToSender(true);
					senderFlushBtn.setDisable(false);
					break;
				default:
					senderBtn.setText("Auto");
					cq.setHoldToSender(false);
					senderFlushBtn.setDisable(true);
					break;
			}
		});
		senderFlushBtn.setOnMouseClicked(mouseEvent -> {
			cq.flushToSender();
		});


		recMsgList.setCellFactory((ListView<MessageDebug> lv) -> {
			ListCell<MessageDebug> cell = new ListCell<>();
			ContextMenu contextMenu = new ContextMenu();
			MenuItem holdItem = new MenuItem();
			holdItem.textProperty().bind(Bindings.format("Hold/Un-hold"));
			holdItem.setOnAction(event -> {
				MessageDebug item = cell.getItem();
				item.isHold = !item.isHold;
				if (item.isHold)
					cell.setStyle("-fx-background: #FF9999;");
				else
					cell.setStyle("-fx-background: #FFFFFF;");
			});


			MenuItem sendItem = new MenuItem();
			sendItem.textProperty().bind(Bindings.format("Receive manually"));
			sendItem.setOnAction(event -> {
				MessageDebug item = cell.getItem();
				cq.receiveManuallyMessage(item);
			});

			MenuItem deleteItem = new MenuItem();
			deleteItem.textProperty().bind(Bindings.format("Delete"));
			deleteItem.setOnAction(event -> cq.removeFromReceiverDebugger(cell.getItem()));

			//Menu subMenu_s1 = new Menu("Remove from 'sending-to-list'");

			contextMenu.getItems().addAll(holdItem, sendItem, deleteItem);//, subMenu_s1);

			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
					cell.setText("");
					cell.setStyle("-fx-background: #FFFFFF;");
				} else {
					cell.setContextMenu(contextMenu);
					cell.setText(cell.getItem().toString());
					MessageDebug item = cell.getItem();
					if (item.isHold)
						cell.setStyle("-fx-background: #FF9999;");
					else
						cell.setStyle("-fx-background: #FFFFFF;");
				}
			});
			return cell;
		});


		sendMsgList.setCellFactory((ListView<MessageDebug> lv) -> {
			ListCell<MessageDebug> cell = new ListCell<>();
			ContextMenu contextMenu = new ContextMenu();
			MenuItem holdItem = new MenuItem();
			holdItem.textProperty().bind(Bindings.format("Hold/Unhold"));
			holdItem.setOnAction(event -> {
				MessageDebug item = cell.getItem();
				item.isHold = !item.isHold;
				if (item.isHold)
					cell.setStyle("-fx-background: #FF9999;");
				else
					cell.setStyle("-fx-background: #FFFFFF;");
			});

			MenuItem sendItem = new MenuItem();
			sendItem.textProperty().bind(Bindings.format("Send manually"));
			sendItem.setOnAction(event -> {
				MessageDebug item = cell.getItem();
				cq.sendManuallyMessage(item);
			});

			Menu subMenu_s1 = new Menu("Remove from 'sending-to-list'");



			MenuItem deleteItem = new MenuItem();
			deleteItem.textProperty().bind(Bindings.format("Delete"));
			deleteItem.setOnAction(event -> cq.removeFromSendDebugger(cell.getItem()));


			contextMenu.getItems().addAll(holdItem, sendItem, deleteItem, subMenu_s1);


			cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
				if (isNowEmpty) {
					cell.setContextMenu(null);
					cell.setText("");
					cell.setStyle("-fx-background: #FFFFFF;");
				} else {
					cell.setContextMenu(contextMenu);
					cell.setText(cell.getItem().toString());

					MessageDebug item = cell.getItem();
					for (User su : cell.getItem().msg.getSendTo()) {
						MenuItem subMenuItem1 = new MenuItem("Remove " + su.getNickname());
						subMenu_s1.setOnAction(actionEvent -> {
							//The following code sequence is stupid, but, 'su' will be
							// incorrect ptr since su is not final.
							String s = ((MenuItem) actionEvent.getTarget()).getText();
							s = s.replace("Remove ", "");
							for (Object u : new ArrayList(cell.getItem().msg.getSendTo())) {
								User u2 = (User) u;
								if (u2.getNickname().equals(s)) {
									cell.getItem().msg.getSendTo().remove(u2);
									subMenu_s1.getItems().remove(((MenuItem) actionEvent.getTarget()));
									cq.refreshLists();
								}
							}
						});
						subMenu_s1.getItems().add(subMenuItem1);
					}

					if (item.isHold)
						cell.setStyle("-fx-background: #FF9999;");
					else
						cell.setStyle("-fx-background: #FFFFFF;");
				}
			});
			return cell;
		});
	}

}
