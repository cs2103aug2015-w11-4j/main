package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserInterface extends Application {
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	private static final BooleanProperty ctrlPressed = new SimpleBooleanProperty(false);
	private static final BooleanProperty zPressed = new SimpleBooleanProperty(false);
	private static final BooleanBinding ctrlAndZPressed = ctrlPressed.and(zPressed);
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		Scene defaultScene = new Scene(root, 550, 480);
		TextField field = new TextField();
		Label feedback = new Label();
		Text text = new Text();
		VBox vbox = new VBox();
		HBox hbox = new HBox();
		Button category1 = new Button("All"); 
        Button category2 = new Button("Work");
        Button category3 = new Button("Personal");
        TableView<Integer> table = new TableView<>();
        
        setCtrlZListener(root);
        
		setText(text);
		setFeedback(feedback);
		
		// Set up vbox
		setVbox(vbox);
        vbox.getChildren().addAll(text, field, feedback);      
        BorderPane.setMargin(vbox, new Insets(10, 15, 0, 15));
        root.setTop(vbox);
		
        // Set up hbox
        hbox.getChildren().addAll(category1, category2, category3);
        BorderPane.setMargin(hbox, new Insets(0, 25, 20, 25));
        root.setBottom(hbox);
        hbox.setSpacing(10);
        
        // Set up table
        setTable(table);
        BorderPane.setMargin(table, new Insets(2,25,20,25));
        root.setCenter(table);
        
        // Set up the scene for different category -> Work, Personal etc
        BorderPane work = new BorderPane();
        Scene workScene = new Scene(work, 550, 500);
        BorderPane personal = new BorderPane();
        Scene personalTaskScene = new Scene(personal, 550, 500);
        
        // Event handler to switch between scenes -> check out the tasks 
        // under respective categories
        HBox categories = new HBox();
        Button tab1 = new Button("All"); 
        Button tab2 = new Button("Work");
        Button tab3 = new Button("Personal");
        categories.getChildren().addAll(tab1, tab2, tab3);
        
        work.setBottom(categories);
        
        // Event handler for input processing
        setKeyPressed(field, feedback, table);
        
        // Event handler for scene switching
        category2.setOnAction(e -> primaryStage.setScene(workScene)); 
        tab1.setOnAction(e -> primaryStage.setScene(defaultScene));
        
        primaryStage.setTitle("Welcome to F2DO, your personalised task manager (:");
        primaryStage.setScene(defaultScene);
        primaryStage.show();
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
	 * Set the design of the text.
	 * @param text
	 */
	private void setText(Text text) {
		text.setText("Viewing All Tasks");
        text.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 18));
        text.setFill(Color.DARKTURQUOISE);
	}
	
	/**
	 * Set the design of feedback.
	 * @param feedback
	 */
	private void setFeedback(Label feedback) {
		feedback.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 12));
        feedback.setTextFill(Color.GREY);
	}
	
	/**
	 * Set the design of vbox.
	 * @param vbox
	 */
	private void setVbox (VBox vbox) {
		vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10,10,5,10));
        vbox.setSpacing(8);
	}
	
	/**
	 * Set the design of table.
	 * @param table
	 */
	private void setTable(TableView<Integer> table) {
		updateTable(table);
        
        TableColumn<Integer, Number> id = new TableColumn<>("ID");
        id.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(rowIndex).getTaskName());
        });

        TableColumn<Integer, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(cellData -> {
        	int rowIndex = cellData.getValue();
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
        	int rowIndex = cellData.getValue();
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
	 * Set the event handler when the key is pressed.
	 * @param field
	 * @param feedback
	 * @param table
	 */
	private void setKeyPressed(TextField field, Label feedback, TableView<Integer> table) {
		field.setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.ENTER) {

				String userInput = field.getText();
				field.clear();
				
				String feedbackMsg = LogicController.process(userInput, _displayList);
				feedback.setWrapText(true);
				feedback.setTextAlignment(TextAlignment.CENTER);
				feedback.setText(feedbackMsg);
				updateTable(table);
			}
		});
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
}