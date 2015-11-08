package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import object.Task;
import type.CommandType;
import type.KeywordType;
import logic.FeedbackHelper;
import logic.LogicController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

//@@author A0112882H
public class UserInterface extends Application {
	private static final int ROW_HEIGHT = 30;
	
	private static BorderPane _root = new BorderPane();
	private static Scene _defaultScene = new Scene(_root, 750, 580);
	private static VBox _vbox = new VBox();
	private static VBox _tables = new VBox();
	
	private static UIButton _taskButton = new UIButton("Tasks & Events");
	private static UIButton _floatingButton = new UIButton("Floating Tasks");
	private static UITextField _field = new UITextField();
	
	private static TextArea _cheatSheet = new TextArea();
	private static Label _feedBack = new Label();
	private static int commandIndex;
	
	private static UITable _taskTable = new UITable(false);
	private static UITable _floatingTable = new UITable(true);
	
	private final KeyCombination _undoKey = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
	private final KeyCombination _redoKey = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
	private final KeyCombination _homeKey = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN);
	private final KeyCombination _showUndoneKey = new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN);
	private final KeyCombination _showDoneKey = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
	private final KeyCombination _showAllKey = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

	private static ArrayList<String> commandHistory = new ArrayList<String>();
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setScene();
		setUpCommandPrompt(); 
        setUpTables();
        
        setKeywordsHighlighting();
        
        setHotKey();
        setCommandKeyPressed();
        
        primaryStage.setScene(_defaultScene);
        primaryStage.setTitle("F2DO");
        primaryStage.show();
	}

	public BorderPane getRootNode() {
		return _root;
	}
	
	private void setScene() {
		String css = UserInterface.class.getResource("style.css").toExternalForm();
        _defaultScene.getStylesheets().add(css);
        
        _defaultScene.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int displaySize = (int) Math.floor(_taskTable.getHeight()/ROW_HEIGHT) - 1 ;
				LogicController.setNonFloatingDisplaySize(displaySize);
				updateDisplayList();
			}
        	
        });
	}
	
	/** 
	 * Set up command prompt and feedback
	 */
	private void setUpCommandPrompt() {
		setTextArea();
		setFeedback();
		
		_field.setId("textarea");
		_feedBack.setId("feedback");
		
		_vbox.setAlignment(Pos.CENTER);
		_vbox.setSpacing(5);
		_vbox.getChildren().addAll(_field, _feedBack);     
        BorderPane.setMargin(_vbox, new Insets(20, 20, 0, 20));
        
        _root.setTop(_vbox);
	}
	
	/** 
	 * Set up labels and tables 
	 */
	private void setUpTables() {
		updateDisplayList();
		
		BorderPane.setMargin(_tables, new Insets(8, 20, 30, 20));
        BorderPane.setAlignment(_tables, Pos.CENTER);
        
        _floatingTable.setId("floatingTable");
        _taskTable.setId("taskTable");
        
        _taskButton.setMaxWidth(Double.MAX_VALUE);
        _floatingButton.setMaxWidth(Double.MAX_VALUE);
        _taskButton.setStyle("-fx-font-size: 13.5; -fx-font-weight: bold");
        _floatingButton.setStyle("-fx-font-size: 13.5; -fx-font-weight: bold");
        
        _tables.setAlignment(Pos.CENTER);
        _tables.getChildren().addAll(_taskButton, _taskTable, _floatingButton, _floatingTable);
        _tables.setSpacing(7);
        
        _root.setCenter(_tables);
	}
	
	/**
	 * Update tables.
	 */
	private static void updateDisplayList() {
		ArrayList<Task> nonFloatingList = LogicController.getNonFloatingList();
		ArrayList<Task> floatingList = LogicController.getFloatingList();
		
		_displayList.clear();
		_displayList.addAll(nonFloatingList);
		_displayList.addAll(floatingList);
		
		_taskTable.updateTable(nonFloatingList, floatingList);
		_floatingTable.updateTable(nonFloatingList, floatingList);
		_field.updateDisplayList(_displayList);
	}
	
	/**
	 * Set the design of textArea 
	 */
	private void setTextArea() {
		_field.setPrefHeight(25);
		_field.setMaxHeight(25);
		_field.setPadding(new Insets(2,2,2,2));
		_field.setWrapText(true);
		_field.setStyle("-fx-border-color: lightblue; -fx-font-size: 14");
	}
	
	/**
	 * Set the design of feedback.
	 * @param feedback
	 */
	private void setFeedback() {
		_feedBack.setText("Welcome to F2DO, your personalised task manager(:\n"
				+ "Type " + "\"Help\"" + " for a list of commands to get started.");
		_feedBack.setMouseTransparent(true);
	}
	
	/**
	 * Set highlighting of the keyword.
	 */
	private void setKeywordsHighlighting() {

		_field.textProperty().addListener((observable, oldValue, newValue) -> {
			//check if the first word is a keyword - happens in most cases 
			//for commands e.g. like add, search, edit, delete
			String firstWord = getFirstWord(newValue);

			if (isValidCmd(firstWord)) {
				_field.setStyle(0, firstWord.length(), "-fx-font-weight: bold; -fx-fill: red");
				if (newValue.length() > firstWord.length()) {
					_field.setStyle(firstWord.length() + 1, newValue.length(), "-fx-font-weight: normal; -fx-fill: black");
				}

				String[] result = newValue.substring(firstWord.length()).split("\\s");
				int currentIndex = firstWord.length();
				for (int i = 0; i < result.length; i++) {
					String word = result[i];
					if (isValidKeyword(word)) {
						_field.setStyle(currentIndex, currentIndex + word.length(),
								"-fx-font-weight: bold; -fx-fill: blue");
					}
					currentIndex += word.length() + 1;
				}

			} else {
				_field.setStyle(0, newValue.length(), "-fx-font-weight: normal; -fx-fill: black");
			}
		});
	}

	/**
	 * Get the first word of the command.
	 * @param newCommand - input command
	 * @return first word
	 */
	private String getFirstWord(String newCommand) {
		
		String[] textTokens = newCommand.split(" ");
		
		if (textTokens.length > 0) {
			return textTokens[0];
		}
		
		return null;
	}
	
	/**
	 * Check if the entered word is a valid command.
	 * @param word - input word
	 * @return true if the word is a valid command; false otherwise
	 */
	private boolean isValidCmd(String word) {
		
		if (CommandType.toCmd(word) != CommandType.INVALID) {
			return true;
		}		
		return false;	
	}
	
	/**
	 * Check if the entered word is a valid keyword.
	 * @param word - input word
	 * @return true if the word is a valid keyword; false otherwise
	 */
	private boolean isValidKeyword(String word) {
		
		if (KeywordType.toType(word) != KeywordType.INVALID) {
			return true;
		}
		return false;	
	}
	
	/**
	 * Set the hot keys.
	 * Ctrl + Z: undo operation.
	 * Ctrl + Y: redo operation.
	 * Ctrl + H: home page.
	 * Ctrl + U: show undone tasks.
	 * Ctrl + D: show done tasks.
	 * Ctrl + S: show all.
	 * F1: help page.
	 * ESC: exit application.
	 */
	private void setHotKey() {
		String showUndone = "show undone";
		String showDone = "show done";
		String showAll = "show all";

		_root.setOnKeyPressed((KeyEvent event) -> {
			
			if (_undoKey.match(event)) {
				String feedbackMsg = LogicController.undo();
				 _feedBack.setText(feedbackMsg);
				 updateDisplayList();
			} else if (_redoKey.match(event)) {
				String feedbackMsg = LogicController.redo();
				 _feedBack.setText(feedbackMsg);
				 updateDisplayList();
			} else if (_homeKey.match(event)) {
				initialiseScene();
				setUpCommandPrompt();
				setUpTables();
			} else if (_showUndoneKey.match(event)) {
				String feedbackMsg = LogicController.process(showUndone, _displayList);
				_feedBack.setText(feedbackMsg);
				 updateDisplayList();
			} else if (_showDoneKey.match(event)) {
				String feedbackMsg = LogicController.process(showDone, _displayList);
				_feedBack.setText(feedbackMsg);
				 updateDisplayList();
			} else if (_showAllKey.match(event)) {
				String feedbackMsg = LogicController.process(showAll, _displayList);
				_feedBack.setText(feedbackMsg);
				 updateDisplayList();
			} else if (event.getCode().equals(KeyCode.F1)) {
				try {
					initialiseScene();
					setUpCommandPrompt();
					setCheatSheetContent();
				} catch (Exception e) {}
			} else if (event.getCode().equals(KeyCode.ESCAPE)) {
				exit();
			}
			
		});
	}
	
	/**
	 * Set the event handler when the key is pressed.
	 */
	private void setCommandKeyPressed() {
		
		_field.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				String userInput = _field.getText();
				commandHistory.add(userInput);
				commandIndex = commandHistory.size() - 1;
					
				_field.clear();
				event.consume();
	
				String feedbackMsg = LogicController.process(userInput, _displayList);

				if (feedbackMsg == 	FeedbackHelper.MSG_HELP) {
					try {
						initialiseScene();
						setUpCommandPrompt();
						setCheatSheetContent();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (feedbackMsg == FeedbackHelper.MSG_HOME) {
					initialiseScene();
					setUpCommandPrompt();
					setUpTables();
				} else {
					_feedBack.setText(feedbackMsg);
					updateDisplayList();
				}
			} else if (event.getCode().equals(KeyCode.UP)) {
			
				if (!commandHistory.isEmpty()) {
					_field.replaceText(commandHistory.get(commandIndex));
					int length = commandHistory.get(commandIndex).length();
					commandIndex--;
				
					Platform.runLater( new Runnable() {
						@Override
						public void run() {
							_field.positionCaret(length);
						}
					});
				
					if (commandIndex < 0) {
						commandIndex = 0;
					}
				}
			} else if (event.getCode().equals(KeyCode.DOWN)) {
				_field.showPopup();
			}
		});
		
	}
	
	private void initialiseScene() {
		_vbox.getChildren().clear();
		_tables.getChildren().clear();
		_root.getChildren().clear();
	}
	
	private void setCheatSheetContent() throws IOException  {
		String text;
		StringBuilder content = new StringBuilder();

		_cheatSheet.setEditable(false);
		
		BorderPane.setMargin(_cheatSheet, new Insets(8, 20, 25, 20));
		
		URL url = UserInterface.class.getClassLoader().getResource("./gui/cheatsheet.txt");
		System.out.println(url.getPath());
		
		FileReader in = new FileReader(url.getPath());
	    BufferedReader br = new BufferedReader(in);

	    while ((text = br.readLine()) != null) {
	    	content.append(text).append("\n");
	    }
	    
	    _cheatSheet.clear();
	    _cheatSheet.appendText(content.toString());
	    
	    _root.setCenter(_cheatSheet);
	    
	    br.close();
	}
	
	private void exit() {
		Platform.exit();
	}
}
