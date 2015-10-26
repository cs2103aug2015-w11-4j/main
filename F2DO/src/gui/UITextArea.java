package gui;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UITextArea extends TextArea {
	public UITextArea(String message) {
		super();
		this.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 12));
		this.setPrefRowCount(3);
		this.setText(message);
		this.setMouseTransparent(true);
	}
}
