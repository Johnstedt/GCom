package client;

import javafx.application.Application;
import javafx.stage.Stage;

public class DebuggerController extends Application{
	static Stage classStage = new Stage();

	public DebuggerController() {

	}




	@Override
	public void start(Stage stage) throws Exception {
			classStage = stage;
	}

	public void terminate() {

	}
}
