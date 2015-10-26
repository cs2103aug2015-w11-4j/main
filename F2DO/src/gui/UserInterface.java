package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import object.Task;
import logic.LogicController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserInterface extends Application {
	private static final String MSG_WELCOME = "Welcome to F2DO, your personalised task manager(:\n"
			+ "Type " + "\"Help\"" + " for a list of commands to get started.";
	
	private static BorderPane _root = new BorderPane();
	private static Scene _defaultScene = new Scene(_root, 650, 480);
	private static VBox _vbox = new VBox();
	private static VBox _tables = new VBox();
	
	private static Button _taskButton = new Button();
	private static Button _floatingButton = new Button();
	private static TextField _field = new TextField();
	private static UITextArea _feedBack = new UITextArea(MSG_WELCOME);
	
	private static int _rowIndex;
	//private static int commandIndex;
	
	private static int _ctrlZCount = 0;
	private static int _ctrlYCount = 0;
	
	private static final BooleanProperty _ctrlPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty _zPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty _yPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty _ePressed = new SimpleBooleanProperty(false);
	private static final BooleanBinding _ctrlAndZPressed = _ctrlPressed.and(_zPressed);
	private static final BooleanBinding _ctrlAndYPressed = _ctrlPressed.and(_yPressed);
	private static final BooleanBinding _ctrlAndEPressed = _ctrlPressed.and(_ePressed);
	
	private static TableView<Integer> _taskTable = new TableView<>();
	private static TableView<Integer> _floatingTable = new TableView<>();
	//private static ArrayList<String> commandHistory = new ArrayList<String>();
	
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		_taskButton.setText("Tasks & Events");
		_floatingButton.setText("Floating Tasks");
		
		_field.setPromptText("Enter your command..");
		
		_vbox.setAlignment(Pos.CENTER);
		_vbox.setSpacing(2);
		_vbox.getChildren().addAll(_feedBack, _field);     
        BorderPane.setMargin(_vbox, new Insets(10, 20, 20, 20));
        _root.setBottom(_vbox);
        
        setTasksWithDates(_taskTable);
        setFloatingTasks(_floatingTable);
        BorderPane.setMargin(_tables, new Insets(10,20,0,20));
        BorderPane.setAlignment(_tables, Pos.CENTER);
        _tables.getChildren().addAll(_floatingButton, _floatingTable, _taskButton, _taskTable);
        _tables.setSpacing(5);
        _root.setCenter(_tables);
        
        setKeyCombinationListener();
        
        // Event handler for input processing
        setKeyPressed(_field, _feedBack, _taskTable, primaryStage);
        
        String css = UserInterface.class.getResource("style.css").toExternalForm();
        _defaultScene.getStylesheets().add(css);
        primaryStage.setScene(_defaultScene);
        primaryStage.show();
	}
	
	/**
	 * Create key combination handler.
	 * CTRL+Z: undo listener.
	 * CTRL+Y: redo listener.
	 * CTRL+E: exit listener.
	 */
	private void setKeyCombinationListener() {
		// Undo listener
		_ctrlAndZPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				 _ctrlZCount += 1;
				 
				 if ((_ctrlZCount % 2) == 0) {
					 _ctrlZCount = 0;
					 String feedbackMsg = LogicController.undo();
					 _feedBack.setText(feedbackMsg);
					 updateTable(_taskTable);
				 }
			}
        });
		
		// Redo listener
		_ctrlAndYPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				 _ctrlYCount += 1;
				 
				 if ((_ctrlYCount % 2) == 0) {
					 _ctrlYCount = 0;
					 String feedbackMsg = LogicController.redo();
					 _feedBack.setText(feedbackMsg);
					 updateTable(_taskTable);
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
            } else if (event.getCode() == KeyCode.Z) {
            	_zPressed.set(true);
            } else if (event.getCode() == KeyCode.Y) {
            	_yPressed.set(true);
            } else if (event.getCode() == KeyCode.E) {
            	_ePressed.set(true);
            }
        });
        
        _root.setOnKeyReleased((KeyEvent event) -> {
        	if (event.getCode() == KeyCode.CONTROL) {
        		_ctrlPressed.set(false);
            } else if (event.getCode() == KeyCode.Z) {
            	_zPressed.set(false);
            } else if (event.getCode() == KeyCode.Y) {
            	_yPressed.set(false);
            } else if (event.getCode() == KeyCode.E) {
            	_ePressed.set(false);
            }
        });
	}
	
	/**
	 * Set the design of table.
	 * @param table
	 */
	private void setTasksWithDates(TableView<Integer>table) {
		updateTable(table);
        
        TableColumn<Integer, Number> id = new TableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });

        TableColumn<Integer, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
        	DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
        	DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _displayList.get(_rowIndex).getStartDate();
			
			if (date != null) {
				String date_string = date.toString();
				//- to be edited : would not work if user inputs 12:00am/pm as the time
				if (date_string.contains("12:00")) {
					property.setValue(dateWithoutTime.format(date));
				} else {
					property.setValue(dateWithTime.format(date));
				}		
			} else {
				property.setValue("?");
			} 
	
			return property;
        });
        
        TableColumn<Integer, String> endDate = new TableColumn<>("End Date");
        endDate.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
			DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _displayList.get(_rowIndex).getEndDate();
			
			if (date != null) {
				String date_string = date.toString();
				//- to be edited : would not work if user inputs 12:00am/pm as the time
				if (date_string.contains("12:00")) {
					property.setValue(dateWithoutTime.format(date));
				} else {
					property.setValue(dateWithTime.format(date));
				}		
			} else {
				property.setValue("?");
			} 
			
			return property;
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER;");
        startDate.setStyle( "-fx-alignment: CENTER;");
        endDate.setStyle( "-fx-alignment: CENTER;");
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        table.getColumns().add(id);
        table.getColumns().add(taskName);
        table.getColumns().add(startDate);
        table.getColumns().add(endDate);
	}
	
	/**
	 * Set the design of table.
	 * @param table
	 */
	private void setFloatingTasks(TableView<Integer>table) {
		updateTable(table);
        
        TableColumn<Integer, Number> id = new TableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER;");
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        table.getColumns().add(id);
        table.getColumns().add(taskName);
	}
	
	/**
	 * Set the event handler when the key is pressed.
	 * @param field
	 * @param feedback
	 * @param table
	 */
	private void setKeyPressed(TextField field, TextArea feedback, TableView<Integer> table, Stage primaryStage) {
		field.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				
				String userInput = field.getText();
			
				//commandHistory.add(userInput);
				//commandIndex = commandHistory.size() - 1;
				
				field.clear();
				
				String feedbackMsg = LogicController.process(userInput, _displayList);
				
				// help section still has some bugs at the moment -- not linked with logic component yet
				// if user types "Help", the scene changes - the two tables will be removed and the cheat sheet
				// will be displayed in the existing feedback box or alternatively, inside a list.

				if (feedbackMsg == "help") {
					feedback.setPrefRowCount(50); 
					try {
						setCheatSheetContent();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					feedback.setText(feedbackMsg);
					updateTable(table);
				}			
				feedback.setText(feedbackMsg);
				updateTable(table);
			}
			/*else if (event.getCode() == KeyCode.UP) {
				
				//if (!commandHistory.isEmpty()) {
				//	field.setText(commandHistory.get(commandIndex));
				//	int length = commandHistory.get(commandIndex).length();
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
			}
			else if (event.getCode() == KeyCode.DOWN) {
				
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
			}*/
			/* ---Unsuccessful at the moment ---*/
			else if (event.getCode() == KeyCode.TAB) {
				exit(primaryStage);
			}
		});
	}
				
	private void setCheatSheetContent() throws IOException  {
		String text;
		StringBuilder stringBuilder = new StringBuilder();
		
		FileReader in = new FileReader("cheatsheet.txt");
	    BufferedReader br = new BufferedReader(in);

	    while ((text = br.readLine()) != null) {
	    	stringBuilder.append(text).append("\n");
	    }
	    br.close();
	}
	
	/**
	 * Update the table.
	 * @param table
	 */
	private void updateTable(TableView<Integer> table) {
		_displayList = LogicController.getDisplayList();
		table.getItems().clear();
		
		for (int i = 0; i < _displayList.size(); i++) {
            table.getItems().add(i);
		}
	}
	
	private void exit(Stage primaryStage) {
		primaryStage.close();
	}
}