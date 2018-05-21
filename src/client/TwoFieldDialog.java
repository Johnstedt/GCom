package client;

import group_management.CommunicationType;
import group_management.MessageOrderingType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.*;

public class TwoFieldDialog {
	String val1;
	String val2;

	public boolean show(String title, String header, String val1, String val2) {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(header);

		// Set the icon (must be included in the project).
		//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

		// Create the field1 and field2 labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField field1 = new TextField();
		field1.setPromptText(val1);
		TextField field2 = new TextField();
		field2.setPromptText(val2);

		grid.add(new Label(val1), 0, 0);
		grid.add(field1, 1, 0);
		grid.add(new Label(val2), 0, 1);
		grid.add(field2, 1, 1);

		// Enable/Disable login button depending on whether a field1 was entered.
		Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
		okButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
		field1.textProperty().addListener((observable, oldValue, newValue) -> {
			okButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

// Request focus on the field1 field by default.
		Platform.runLater(() -> field1.requestFocus());

// Convert the result to a field1-field2-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == okButtonType) {
				return new Pair<>(field1.getText(), field2.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(input -> {
			System.out.println("field1=" + input.getKey() + ", field2=" + input.getValue());
			this.val1 = input.getKey();
			this.val2 = input.getValue();
		});
		return result.isPresent();
	}

	public HashMap<String, String> newGroup() {
		Set<String> messageOrdering = new HashSet<>();
		for (MessageOrderingType mot : MessageOrderingType.values()) {
			messageOrdering.add(mot.toString());
		}

		Set<String> communicationType = new HashSet<>();
		for (CommunicationType ct: CommunicationType.values()) {
			communicationType.add(ct.toString());
		}

		TextField field1 = new TextField();
		field1.setPromptText(val1);


		Dialog<HashMap<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Create new group");
		dialog.setHeaderText("Lets make an awesome group");

// Set the button types.
		ButtonType mergeButtonType = new ButtonType("Create group", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(mergeButtonType, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ComboBox<String> messageOrderingBox = new ComboBox<>();
		messageOrdering.forEach((mo) -> {
			messageOrderingBox.getItems().add(mo);
		});
		messageOrderingBox.setValue(messageOrdering.iterator().next());

		ComboBox<String> communicationTypeBox = new ComboBox<>();
		communicationType.stream().forEach((ct) -> {
			communicationTypeBox.getItems().add(ct);
		});
		communicationTypeBox.setValue(communicationType.iterator().next());

		int i1 = 0;
		grid.add(new Label("Name:"), 0, i1);
		grid.add(field1, 1, i1++);
		grid.add(new Label("Communication Type:"), 0, i1);
		grid.add(messageOrderingBox, 1, i1++);
		grid.add(new Label("Ordering Type:"), 0, i1);
		grid.add(communicationTypeBox, 1, i1++);

		dialog.getDialogPane().setContent(grid);
		Platform.runLater(() -> field1.requestFocus());

// Convert the result to the desired data structure
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == mergeButtonType) {
				HashMap<String, String> result = new HashMap<>();
				result.put("Name", field1.getText());
				result.put("Message Ordering", messageOrderingBox.getValue());
				result.put("Ordering Type", communicationTypeBox.getValue());
				return result;
			}
			return null;
		});

		Optional<HashMap<String, String>> result = dialog.showAndWait();

		return result.orElse(null);

	}
}
