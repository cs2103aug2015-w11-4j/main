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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
	
	private static BorderPane root;
	private static Scene defaultScene;
	private static VBox vbox;
	private static VBox tables;
	
	private static Button taskButton;
	private static Button floatingButton;
	private static TextField field;
	private static TextArea feedBack;
	
	private static int rowIndex;
	private static int commandIndex;
	private static String cheatSheet;
	
	private static final BooleanProperty ctrlPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty zPressed = new SimpleBooleanProperty(false);
	private static final BooleanBinding ctrlAndZPressed = ctrlPressed.and(zPressed);
	
	private static TableView<Integer> tasks;
	private static TableView<Integer> floating;
	private static ArrayList<String> commandHistory = new ArrayList<String>();
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new BorderPane();
		defaultScene = new Scene(root, 650, 480);
		
		taskButton = new Button();
		floatingButton = new Button();
		field = new TextField();
		feedBack = new TextArea();
		
		taskButton.setText("Tasks & Events");
		floatingButton.setText("Floating Tasks");
		
		field.setPromptText("Enter your command..");
		setFeedback(feedBack);
		
		vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(2);
        vbox.getChildren().addAll(feedBack, field);     
        BorderPane.setMargin(vbox, new Insets(10, 20, 20, 20));
        root.setBottom(vbox);
        
        tasks = new TableView<>();
        floating = new TableView<>();
        
        tables = new VBox();
        setTasksWithDates(tasks);
        setFloatingTasks(floating);
        BorderPane.setMargin(tables, new Insets(10,20,0,20));
        BorderPane.setAlignment(tables, Pos.CENTER);
        tables.getChildren().addAll(floatingButton, floating, taskButton, tasks);
        tables.setSpacing(5);
        root.setCenter(tables);
        
        setCtrlZListener(root);
        
        // Event handler for input processing
        setKeyPressed(commandHistory, field, feedBack, tasks, primaryStage);
        
        String css = UserInterface.class.getResource("style.css").toExternalForm();
        defaultScene.getStylesheets().add(css);
        primaryStage.setScene(defaultScene);
        primaryStage.show();
	}
	
	/**
	 * Set the design of feedback.
	 * @param feedback
	 */
	private void setFeedback(TextArea feedBack) {
		feedBack.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 12));
		feedBack.setPrefRowCount(3);
		feedBack.setText("Welcome to F2DO, your personalised task manager(:\n"
				+ "Type " + "\"Help\"" + " for a list of commands to get started.");
		feedBack.setMouseTransparent(true);
	}
	
	/**
	 * Create CTRL+Z undo handler.
	 * @param root
	 */
	private static void setCtrlZListener(BorderPane root) {
        ctrlAndZPressed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				 System.out.println("Ctrl and Z pressed together");
			}
        });
        
        root.setOnKeyPressed((KeyEvent event) -> {
        	if (event.getCode() == KeyCode.CONTROL) {
        		ctrlPressed.set(true);
            } else if (event.getCode() == KeyCode.Z) {
            	zPressed.set(true);
            }
        });
        
        root.setOnKeyReleased((KeyEvent event) -> {
        	if (event.getCode() == KeyCode.CONTROL) {
        		ctrlPressed.set(false);
            } else if (event.getCode() == KeyCode.Z) {
            	zPressed.set(false);
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
            rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
            rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(rowIndex).getTaskName());
        });

        TableColumn<Integer, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(cellData -> {
        	rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
        	DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
        	DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _displayList.get(rowIndex).getStartDate();
			
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
        	rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
			DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _displayList.get(rowIndex).getEndDate();
			
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
            rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
            rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(rowIndex).getTaskName());
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
	private void setKeyPressed(ArrayList<String> commandHistory, TextField field, TextArea feedback, TableView<Integer> table, Stage primaryStage) {
		field.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				
				String userInput = field.getText();
			
				commandHistory.add(userInput);
				commandIndex = commandHistory.size() - 1;
				
				field.clear();
				
				String feedbackMsg = LogicController.process(userInput, _displayList);
				
				// help section still has some bugs at the moment -- not linked with logic yet
				if (feedbackMsg == "help") {
					feedback.setPrefRowCount(20);
					try {
						setCheatSheetContent(cheatSheet);
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
			else if (event.getCode() == KeyCode.UP) {
				
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
			}
			/* ---Unsuccessful at the moment ---*/
			else if (event.getCode() == KeyCode.TAB) {
				exit(primaryStage);
			}
		});
	}
				
	private void setCheatSheetContent(String cheatSheet) throws IOException  {
		String text;
		StringBuilder stringBuilder = new StringBuilder();
		
		FileReader in = new FileReader(cheatSheet);
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

/*
HBox hbox = new HBox();
Button category1 = new Button("All"); 
Button category2 = new Button("Work");
Button category3 = new Button("Personal");
*/

/*
hbox.getChildren().addAll(category1, category2, category3);
BorderPane.setMargin(hbox, new Insets(0, 25, 20, 25));
root.setBottom(hbox);
hbox.setSpacing(10);
*/

//Event handler to switch between scenes -> check out the tasks 
// under respective categories
/*
HBox categories = new HBox();
Button tab1 = new Button("All"); 
Button tab2 = new Button("Work");
Button tab3 = new Button("Personal");
categories.getChildren().addAll(tab1, tab2, tab3);

work.setBottom(categories);
*/

//Event handler for scene switching
/*
category2.setOnAction(e -> primaryStage.setScene(workScene)); 
tab1.setOnAction(e -> primaryStage.setScene(defaultScene));
*/
