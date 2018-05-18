package debugger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import message.InternalMessage;
import message.Message;

import java.io.File;
import java.io.IOException;
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
		if (groupName.equals("init")) {
			return;
		}
		Pane p = new Pane();
		try {
			p.getChildren().add(FXMLLoader.load(new File("src/debugger/oneChat.fxml").toURL()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		AnchorPane aPane = (AnchorPane) classStage.getScene().lookup("#anchorPane");
		SplitPane chatSplitPane = (SplitPane) aPane.lookup("#vboxPane").lookup("#chatSplitPane");
		chatSplitPane.getItems().add(p);

		BorderPane bp = (BorderPane) p.lookup("#borderPane");
		FlowPane topFields = (FlowPane) bp.lookup("#topFields");

		Text groupNameTextField = (Text) topFields.lookup("#groupNameTxtField");
		groupNameTextField.setText(groupName);

		Text orderTextField = (Text) topFields.lookup("#orderTypeTxtField");
		orderTextField.setText(msg.ot.toString());

		Text comTextField = (Text) topFields.lookup("#comTypeTxtField");
		comTextField.setText(msg.ct.toString());


		GridPane gp = (GridPane) bp.lookup("#gridPane");
		ListView<Message> recMsgList = (ListView<Message>) gp.lookup("#receiverAtDebug");
		ListView<Message> sendMsgList = (ListView<Message>) gp.lookup("#senderAtDebug");
		cq.setMsgLists(recMsgList, sendMsgList);

		FlowPane recFlowPane = (FlowPane) gp.lookup("#recFlowPane");
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

		FlowPane sendFlowPane = (FlowPane) gp.lookup("#sendFlowPane");
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

	}

}
