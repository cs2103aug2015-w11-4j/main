package view;
	
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogicController;
import main.F2DOMain;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import objects.Task;
import objects.TaskDeadLine;
import objects.TaskEvent;
import objects.TaskFloating;

import java.util.ArrayList;
import java.util.Date;

public class UserInterface extends Application {
	
	private TextField field; 
	private static ArrayList<Task> _taskList;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane root = new BorderPane();
		Scene defaultScene = new Scene(root, 420, 420);
		
		//root.setAlignment(Pos.TOP_CENTER);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		//scene.getStylesheets().add("application.css");
		
		Text text = new Text();
        text.setText("F2DO, your personalised task manager");
        text.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 16));
        text.setFill(Color.DARKTURQUOISE);
        
        field = new TextField();
        
        Text feedback = new Text();
        feedback.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 9));
        feedback.setFill(Color.GREY);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(12,10,5,10));
        vbox.setSpacing(7);
        vbox.getChildren().addAll(text, field, feedback);
        
        root.setTop(vbox);
            
        HBox hbox = new HBox();
        Button category1 = new Button("All"); 
        Button category2 = new Button("Work");
        Button category3 = new Button("Personal");
        hbox.getChildren().addAll(category1, category2, category3);
        
        root.setBottom(hbox);
              
        TableView<Task> table = new TableView<Task>();
        table.setPrefWidth(10);
        table.setPrefHeight(10);
        
		_taskList = LogicController.getTaskList();
		ObservableList<Task> data = FXCollections.observableArrayList(_taskList);
        TableColumn<Task, Integer> column_ID = new TableColumn<>("ID");
        TableColumn<Task, String> column_Task = new TableColumn<>("Task Name");
        TableColumn<Task, Date> column_endDate = new TableColumn<>("Deadline");
        
        column_ID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        column_Task.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        column_endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        
        column_ID.prefWidthProperty().bind(table.widthProperty().divide(9)); 
        column_Task.prefWidthProperty().bind(table.widthProperty().divide(2)); 
        column_endDate.prefWidthProperty().bind(table.widthProperty().divide(2.5));
        
        table.getColumns().addAll(column_ID, column_Task, column_endDate);
        table.setItems(data);

        BorderPane.setMargin(table, new Insets(0,12,12,12));
       
        root.setCenter(table);
        //updateTable(table);
        
        /* ----- Setting up the scene for different category ->  Work, Personal etc----- */
        BorderPane work = new BorderPane();
        Scene workScene = new Scene(work, 400, 400);
        BorderPane personal = new BorderPane();
        Scene personalTaskScene = new Scene(personal, 400, 400);
        
        /* ----- Event handler to switch between scenes -> check out the tasks under respective categories ----- */
        HBox categories = new HBox();
        Button tab1 = new Button("All"); 
        Button tab2 = new Button("Work");
        Button tab3 = new Button("Personal");
        categories.getChildren().addAll(tab1, tab2, tab3);
        
        work.setBottom(categories);
        
        /* ----- Event handler for input processing ----- */ 
        field.setOnKeyPressed((KeyEvent event) -> {
        	
        	if (event.getCode() == KeyCode.ENTER) {
  
        		String userInput = field.getText();
        		field.clear();
        		
        		String feedbackMsg = LogicController.process(userInput, _taskList);
        		feedback.setText(feedbackMsg);
        		
        		updateTable(table, data);
        		
        	}
        });
        
        /* ------ Event handler for scene switching ----- */
        category2.setOnAction(e -> primaryStage.setScene(workScene)); 
        tab1.setOnAction(e -> primaryStage.setScene(defaultScene));
        
        primaryStage.setTitle("F2DO");
        primaryStage.setScene(defaultScene);
        primaryStage.show();
	}
	
	private void updateTable(TableView<Task> table, ObservableList<Task> data) {
		
		data = FXCollections.observableArrayList(_taskList);
		table.setItems(data);
		
		// For testing purpose. You can refer to this on how task details 
		// can be called. However, Please delete this part after updateTable 
		// function is implemented.
		for (int i = 0; i < _taskList.size(); i++) {
			
			Task task = _taskList.get(i);
			if (task instanceof TaskFloating){
				System.out.println("TASK FLOATING DETECTED");
			}else if (task instanceof TaskDeadLine){
				System.out.println("TASK DEADLINE DETECTED");
			}else if (task instanceof TaskEvent){
				System.out.println("TASK EVENT DETECTED");
			}
			
			System.out.println("ID: " + task.getTaskID());
			System.out.println("Title: " + task.getTaskName());
			System.out.println("Is completed: " + task.getCompleted());
			System.out.println("Start Time: " + task.getStartDate());
			System.out.println("End Time: " + task.getEndDate());
			System.out.println("Is floating task: " + task.getFloating());
		}
		
		// Display the task list in the table
	}
}
