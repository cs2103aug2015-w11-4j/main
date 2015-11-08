//@@author Cher Lin
package gui;

import org.fxmisc.richtext.InlineCssTextArea;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
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
import java.util.logging.Logger;

public class UserInterface extends Application {
	
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
	
	private static int _ctrlUCount = 0;
	private static int _ctrlRCount = 0;
	
	private static final BooleanProperty _ctrlPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty _uPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty _rPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty _ePressed = new SimpleBooleanProperty(false);
	private static final BooleanBinding _ctrlAndUPressed = _ctrlPressed.and(_uPressed);
	private static final BooleanBinding _ctrlAndRPressed = _ctrlPressed.and(_rPressed);
	private static final BooleanBinding _ctrlAndEPressed = _ctrlPressed.and(_ePressed);
	private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private static ArrayList<String> commandHistory = new ArrayList<String>();
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		
		setUpCommandPrompt(); 
		updateDisplayList();
        setUpTables();
        
        setKeywordsHighlighting();
        
        setKeyCombinationListener();
        setKeyPressed(_field, _feedBack, _taskTable, primaryStage);
        
        String css = UserInterface.class.getResource("style.css").toExternalForm();
        _defaultScene.getStylesheets().add(css);
        
        primaryStage.setScene(_defaultScene);
        primaryStage.show();
	}
	
	/** 
	 * Set up command prompt and feedback
	 */
	private void setUpCommandPrompt() {
		setTextArea();
		setFeedback(_feedBack);
		
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
		
		BorderPane.setMargin(_tables, new Insets(8, 20, 25, 20));
        BorderPane.setAlignment(_tables, Pos.CENTER);
        
        _taskButton.setMaxWidth(Double.MAX_VALUE);
        _floatingButton.setMaxWidth(Double.MAX_VALUE);
        _taskButton.setStyle("-fx-font-size: 13.5; -fx-font-weight: bold");
        _floatingButton.setStyle("-fx-font-size: 13.5; -fx-font-weight: bold");
        
        _tables.setAlignment(Pos.CENTER);
        //_tables.setPadding(new Insets(7.5,7.5,7.5,7.5));
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
		
		/*
		ScrollBar scrollBarv = (ScrollBar)_field.lookup(".scroll-bar:vertical");
		scrollBarv.setDisable(true);
		*/
	}
	
	/**
	 * Set the design of feedback.
	 * @param feedback
	 */
	private void setFeedback(Label feedBack) {
		feedBack.setStyle("-fx-text-fill: black");
		feedBack.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 13));
		feedBack.setText("Welcome to F2DO, your personalised task manager(:\n"
				+ "Type " + "\"Help\"" + " for a list of commands to get started.");
		feedBack.setMouseTransparent(true);
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
					//System.out.println(result[i]);
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
	 * Create key combination handler.
	 * CTRL+U: undo listener.
	 * CTRL+R: redo listener.
	 * CTRL+E: exit listener.
	 */
	private void setKeyCombinationListener() {
		// Undo listener
		_ctrlAndUPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				_ctrlUCount += 1;
				 
				 if ((_ctrlUCount % 2) == 0) {
					 _ctrlUCount = 0;
					 String feedbackMsg = LogicController.undo();
					 _feedBack.setText(feedbackMsg);
					 updateDisplayList();
				 }
			}
        });
		
		// Redo listener
		_ctrlAndRPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				_ctrlRCount += 1;
				 
				 if ((_ctrlRCount % 2) == 0) {
					 _ctrlRCount = 0;
					 String feedbackMsg = LogicController.redo();
					 _feedBack.setText(feedbackMsg);
					 updateDisplayList();
				 }
			}
        });
		
		// Exit listener
		_ctrlAndEPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				System.exit(1);
			}
        });
        
        _root.setOnKeyPressed((KeyEvent event) -> {
        	if (event.getCode() == KeyCode.CONTROL) {
        		_ctrlPressed.set(true);
            } else if (event.getCode() == KeyCode.U) {
            	_uPressed.set(true);
            } else if (event.getCode() == KeyCode.R) {
            	_rPressed.set(true);
            } else if (event.getCode() == KeyCode.E) {
            	_ePressed.set(true);
            }
        });
        
        _root.setOnKeyReleased((KeyEvent event) -> {
        	if (event.getCode() == KeyCode.CONTROL) {
        		_ctrlPressed.set(false);
            } else if (event.getCode() == KeyCode.U) {
            	_uPressed.set(false);
            } else if (event.getCode() == KeyCode.R) {
            	_rPressed.set(false);
            } else if (event.getCode() == KeyCode.E) {
            	_ePressed.set(false);
            }
        });
	}
	
	/**
	 * Set the event handler when the key is pressed.
	 * @param field
	 * @param feedback
	 * @param table
	 */
	private void setKeyPressed(InlineCssTextArea field, Label feedback, TableView<Integer> table, Stage primaryStage) {
			
		field.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override 
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					String userInput = field.getText();
					commandHistory.add(userInput);
					commandIndex = commandHistory.size() - 1;
						
					field.clear();
					keyEvent.consume();
		
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
						feedback.setText(feedbackMsg);
						updateDisplayList();
					}
				}
				else if (keyEvent.getCode() == KeyCode.F1) {
				
					if (!commandHistory.isEmpty()) {
						_field.insertText(0, commandHistory.get(commandIndex));
						//_field.setText(commandHistory.get(commandIndex));
						int length = commandHistory.get(commandIndex).length();
						commandIndex--;
					
						Platform.runLater( new Runnable() {
							@Override
							public void run() {
								field.positionCaret(length);
							}
						});
					
						if (commandIndex < 0) {
							commandIndex = 0;
						}
					}
				} else if (keyEvent.getCode() == KeyCode.DOWN) {
					_field.showPopup();
				}
				/*
				else if (event.getCode() == KeyCode.TAB + SHIFT) {
				
					if (!commandHistory.isEmpty()) {
						field.setText(commandHistory.get(commandIndex + 1));
						int length = commandHistory.get(commandIndex + 1).length();
						commandIndex++;
					
						Platform.runLater( new Runnable() {
					    	@Override
					    	public void run() {
					        	field.positionCaret(length);
					    	}
						});
					
						if (commandIndex > commandHistory.size()) {
							commandIndex = 0;
						} 
					}
				}
				*/
				else if (keyEvent.getCode() == KeyCode.TAB) {
					try {
						initialiseScene();
						setUpCommandPrompt();
						setUpTables();
					} catch (Exception e) {
						logger.warning("Unable to properly process TAB keycode");
						e.printStackTrace();
					}
				}
			
				else if (keyEvent.getCode() == KeyCode.ESCAPE) {
					exit();
				}
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
