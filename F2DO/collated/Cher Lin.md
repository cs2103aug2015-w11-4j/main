# Cher Lin
###### src\gui\UIButton.java
``` java
package gui;

import javafx.scene.control.Button;

public class UIButton extends Button {
	public UIButton(String text) {
		this.setText(text);
	}
}
```
###### src\gui\UITable.java
``` java
package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;
import object.Task;

public class UITable extends TableView<Integer> {
	private ArrayList<Task> _displayList = new ArrayList<Task>();
	private int _rowIndex = 0;
	private int _displayIndex = 0;
	private boolean _isFloating;
	
	/**
	 * Constructor of UITable.
	 * @param isFloating - true if this table is for displaying floating tasks; false otherwise
	 */
	public UITable(boolean isFloating) {
		_isFloating = isFloating;
		
		if (isFloating) {
			setFloatingTable();
		} else {
			setNonFloatingTable();
		}
	}
	
	/**
	 * Update the table.
	 * @param nonFloatingList - non-floating task list
	 * @param floatingList - floating task list
	 */
	public void updateTable(ArrayList<Task> nonFloatingList, ArrayList<Task> floatingList) {
		this.getItems().clear();
		
		_displayList.clear();
		_displayList.addAll(nonFloatingList);
		_displayList.addAll(floatingList);
		
		if (!_isFloating) {
			for (_displayIndex = 0; _displayIndex < nonFloatingList.size(); _displayIndex++) {
				this.getItems().add(_displayIndex);
			}
		} else {
			int floatingMaxSize = nonFloatingList.size() + floatingList.size();
			
			for (_displayIndex = nonFloatingList.size(); 
					_displayIndex < floatingMaxSize; 
					_displayIndex++) {
				this.getItems().add(_displayIndex);
			}
		}
	}
	
	/**
	 * Set non-floating table.
	 */
	private void setNonFloatingTable() {
		UITableColumn<Integer, Number> id = new UITableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        UITableColumn<Integer, String> taskName = new UITableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });

        UITableColumn<Integer, String> startDate = new UITableColumn<>("Start Date");
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
			} 
	
			return property;
        });
        
        UITableColumn<Integer, String> endDate = new UITableColumn<>("End Date");
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
			} 
			
			return property;
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER-LEFT;");
        startDate.setStyle( "-fx-alignment: CENTER;");
        endDate.setStyle( "-fx-alignment: CENTER;");
        
        // Percentage width must sum up to 1
        try {
        	id.setPercentageWidth(0.1);
        	taskName.setPercentageWidth(0.5);
        	startDate.setPercentageWidth(0.2);
        	endDate.setPercentageWidth(0.2);
        } catch (Exception e) {
        	this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        
        this.getColumns().add(id);
        this.getColumns().add(taskName);
        this.getColumns().add(startDate);
        this.getColumns().add(endDate);
	}
	
	/**
	 * Set floating table.
	 */
	private void setFloatingTable() {
		UITableColumn<Integer, Number> id = new UITableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        UITableColumn<Integer, String> taskName = new UITableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER-LEFT;");
        
        // Percentage width must sum up to 1
        try {
        	id.setPercentageWidth(0.1);
        	taskName.setPercentageWidth(0.9);
        } catch (Exception e) {
        	this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        
        this.getColumns().add(id);
        this.getColumns().add(taskName);
	}
}
```
###### src\gui\UITableColumn.java
``` java
package gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class UITableColumn<S, T> extends TableColumn<S, T> {
	private final DoubleProperty _percentageWidth = new SimpleDoubleProperty(1);
	
	public UITableColumn(String text) {
		super(text);
		this.tableViewProperty().addListener(new ChangeListener<TableView<S>>() {

			@Override
			public void changed(ObservableValue<? extends TableView<S>> observable, TableView<S> oldValue,
					TableView<S> newValue) {
				if(UITableColumn.this.prefWidthProperty().isBound()) {
					UITableColumn.this.prefWidthProperty().unbind();
				}
				UITableColumn.this.prefWidthProperty().bind(newValue.widthProperty().multiply(_percentageWidth));
			}	
		});
	}
	
	/**
	 * Property of width by percentage.
	 * @return width by percentage in DoubleProperty format
	 */
	private DoubleProperty percentageWidthProperty() {
		return _percentageWidth;
	}
	
	/**
	 * Get width by percentage.
	 * @return width by percentage
	 */
	public double getPercentageWidth() {
		return this.percentageWidthProperty().get();
	}
	
	/**
	 * Set the width of column by percentage. Range from 0 to 1.
	 * @param value - percentage
	 * @throws IllegalArgumentException number entered is out of range
	 */
	public void setPercentageWidth(double value) throws IllegalArgumentException {
		if(value >= 0 && value <= 1) {
			this.percentageWidthProperty().set(value);
		} else {
			throw new IllegalArgumentException("Error: The percentage entered is invalid");
		}
	}
}
```
###### src\gui\UITextField.java
``` java
package gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import object.Task;
import type.CommandType;
import type.TaskType;

public class UITextField extends TextField {
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	private ContextMenu popupMenu = new ContextMenu();
	
	public UITextField() {
		super();
		setAutoFill();
	}
	
	/**
	 * Update the display list in TextField.
	 * @param displayList - display list
	 */
	public void updateDisplayList(ArrayList<Task> displayList) {
		_displayList = displayList;
	}
	
	/**
	 * Show pop-up menu.
	 */
	public void showPopup() {
		if (!popupMenu.isShowing()) {
			popupMenu.show(this, Side.BOTTOM, 0, 0);
		}
	}
	
	/**
	 * Set up auto fill in of the text field.
	 */
	private void setAutoFill() {
		this.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String text = UITextField.this.getText();
				String[] textTokens = text.split(" ");
				
				int spaceCount = 0;
				for (int i = 0; i < text.length() && spaceCount < 2; i++) {
					if (text.charAt(i) == ' ') {
						spaceCount += 1;
					}
				}
				
				if (textTokens.length == 2 && spaceCount == 2) {
					String firstToken = textTokens[0];
					CommandType cmd = CommandType.toCmd(firstToken);
					int index = getInteger(textTokens[1]) - 1;
					boolean isWithinRange = ((index >= 0) && (index < _displayList.size()));
					
					if (cmd == CommandType.EDIT && isWithinRange) {
						Task task = _displayList.get(index);
						populatePopup(index, task);
						
						if (!popupMenu.isShowing()) {
							popupMenu.show(UITextField.this, Side.BOTTOM, 0, 0);
						}	
					} 
				} else if (textTokens.length <= 2){
					// Hide pop up
					popupMenu.hide();
					popupMenu.getItems().clear();
				}
				
			}
			
		});
	}
	
	/**
	 * Get the integer from an input string.
	 * If the input cannot be parsed, return -1.
	 * @param input - input string
	 * @return parsed integer
	 */
	private int getInteger(String input) {
		try {
			int integer = Integer.parseInt(input);
			return integer;
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Populate the pop-up box.
	 * @param index - index of the task
	 * @param task - task to be displayed
	 */
	private void populatePopup(int index, Task task) {
		ArrayList<String> displayList = getDisplayItems(index, task);
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		
		for (int i = 0; i < displayList.size(); i++) {
			String str = displayList.get(i);
			Label label = new Label(str);
			CustomMenuItem item = new CustomMenuItem(label, true);
			
			if (i == 0) {
				this.setText(str);
			}

			item.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					setText(str);
					popupMenu.hide();
				}

			});

			menuItems.add(item);
		}
		
		popupMenu.getItems().clear();
		popupMenu.getItems().addAll(menuItems);
	}
	
	/**
	 * Get the command input to be displayed in the pop-up menu.
	 * @param index - index of the task
	 * @param task - task to be displayed
	 * @return display items
	 */
	private ArrayList<String> getDisplayItems(int index, Task task) {
		ArrayList<String> items = new ArrayList<String>();
		TaskType taskType = task.getTaskType();
		
		Integer displayIndex = index + 1;
		String floatingStr = "edit " + displayIndex.toString() + " " + task.getTaskName() + " ";
		String eventStr = floatingStr;
		String alternateEventStr = floatingStr;
		String deadlineStr = floatingStr;
		
		Calendar tmrCalendar = Calendar.getInstance();
		Calendar afterTmrCalendar = Calendar.getInstance();
		tmrCalendar.add(Calendar.DAY_OF_MONTH, 1);	
		afterTmrCalendar.add(Calendar.DAY_OF_MONTH, 2);
		
		Date tomorrow = tmrCalendar.getTime();
		Date afterTomorrow = afterTmrCalendar.getTime();
		Date startDate = task.getStartDate();
		Date endDate = task.getEndDate();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		// Set event string
		if (startDate != null && endDate != null) {
			eventStr += "from " + dateFormat.format(startDate) + " ";
			eventStr += "to " + dateFormat.format(endDate);
			
			alternateEventStr += "on " + dateFormat.format(startDate);
		} else if (startDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);	
			
			eventStr += "on " + dateFormat.format(startDate);
			
			alternateEventStr += "from " + dateFormat.format(startDate) + " ";
			alternateEventStr += "to " + dateFormat.format(calendar.getTime());
		} else if (endDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			
			eventStr += "from " + dateFormat.format(endDate) + " ";
			eventStr += "to " + dateFormat.format(calendar.getTime());
			
			alternateEventStr += "on " + dateFormat.format(endDate);
			
		} else {
			eventStr += "from " + dateFormat.format(tomorrow) + " ";
			eventStr += "to " + dateFormat.format(afterTomorrow);
			
			alternateEventStr += "on " + dateFormat.format(tomorrow);
		}
		
		// Set deadline string
		if (endDate != null) {
			deadlineStr += "by " + dateFormat.format(endDate);
		} else if (startDate != null) {
			deadlineStr += "by " + dateFormat.format(startDate);
		} else {
			deadlineStr += "by " + dateFormat.format(tomorrow);
		}
		
		// Assign display order
		int eventIndex = 0;
		int floatingIndex = 1;
		int alternateEventIndex = 2;
		int deadlineIndex = 3;
		int firstIndex = -1;
		
		String[] eventList = {eventStr, floatingStr, alternateEventStr, deadlineStr};
		
		switch (taskType) {
			case EVENT:
				if (endDate == null) {
					items.add(eventList[alternateEventIndex]);
					firstIndex = alternateEventIndex;
				} else {
					items.add(eventList[eventIndex]);
					firstIndex = eventIndex;
				}
				break;
			case DEADLINE:
				items.add(eventList[deadlineIndex]);
				firstIndex = deadlineIndex;
				break;
			case FLOATING:
				items.add(eventList[floatingIndex]);
				firstIndex = floatingIndex;
				break;
			default:
				// Do nothing
		}
		
		for (int i = 0; i < eventList.length; i++) {
			if (i != firstIndex) {
				items.add(eventList[i]);
			}
		}
		
		return items;
	}
}
```
###### src\gui\UserInterface.java
``` java
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
import object.Task;
import logic.FeedbackHelper;
import logic.LogicController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class UserInterface extends Application {
	
	private static BorderPane _root = new BorderPane();
	private static Scene _defaultScene = new Scene(_root, 650, 500);
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

	private static ArrayList<String> commandHistory = new ArrayList<String>();
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {	
		
		setCommandPrompt(); 
		updateDisplayList();
        setUpTables();
        
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
	private void setCommandPrompt() {
		_field.setPromptText("Enter your command..");
		setFeedback(_feedBack);
		
		_vbox.setAlignment(Pos.CENTER);
		_vbox.setSpacing(5);
		_vbox.getChildren().addAll(_field, _feedBack);     
        BorderPane.setMargin(_vbox, new Insets(15, 20, 0, 20));
        
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
	 * Set the design of feedback.
	 * @param feedback
	 */
	private void setFeedback(Label feedBack) {
		feedBack.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 13));
		//feedBack.setPrefRowCount(3);
		feedBack.setText("Welcome to F2DO, your personalised task manager(:\n"
				+ "Type " + "\"Help\"" + " for a list of commands to get started.");
		feedBack.setMouseTransparent(true);
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
	private void setKeyPressed(TextField field, Label feedback, TableView<Integer> table, Stage primaryStage) {
		field.setOnKeyPressed((KeyEvent event) -> {
			
			if (event.getCode() == KeyCode.ENTER) {
				
				String userInput = field.getText();
			
				commandHistory.add(userInput);
				commandIndex = commandHistory.size() - 1;
				
				field.clear();
				
				String feedbackMsg = LogicController.process(userInput, _displayList);

				if (feedbackMsg == 	FeedbackHelper.MSG_HELP) {
					try {
						initialiseScene();
						setCommandPrompt();
						setCheatSheetContent();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					feedback.setText(feedbackMsg);
					updateDisplayList();
				}			
				
				if (feedbackMsg == FeedbackHelper.MSG_HOME) {
					initialiseScene();
					setCommandPrompt();
					setUpTables();
				}
			}
			else if (event.getCode() == KeyCode.F1) {
				
				if (!commandHistory.isEmpty()) {
					field.setText(commandHistory.get(commandIndex));
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
			} else if (event.getCode() == KeyCode.DOWN) {
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
			
			else if (event.getCode() == KeyCode.TAB) {
				try {
					initialiseScene();
					setCommandPrompt();
					setUpTables();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			else if (event.getCode() == KeyCode.ESCAPE) {
				exit();
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
	    _cheatSheet.appendText(content.toString());
	    
	    _root.setCenter(_cheatSheet);
	    
	    br.close();
	}
	
	private void exit() {
		Platform.exit();
	}
}
```
