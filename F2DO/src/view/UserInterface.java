package view;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogicController;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;

import objects.Task;
import objects.TaskDeadLine;
import objects.TaskEvent;
import objects.TaskFloating;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import javax.security.auth.callback.Callback;

public class UserInterface extends Application {
	
	private TextField field; 
	private static ArrayList<Task> _taskList;
	private int[] taskNum = new int[100000];

	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		_taskList = LogicController.getTaskList();
		
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
        
		int index = 1;
		
		for (int i = 0; i < 100000; i++) {
			taskNum[i] = 0;
		}
		
		for (int i = 0; i < 10000; i++) {
			taskNum[i] = index;
			index++;
		}
		
		TableView<Integer> table = new TableView<>();
		
        for (int i = 0; i < _taskList.size(); i++) {
            table.getItems().add(i);
        }
        
        TableColumn<Integer, Number> id = new TableColumn<>("Task ID");
        id.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(taskNum[rowIndex]);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
            int rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_taskList.get(rowIndex).getTaskName());
        });

        TableColumn<Integer, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(cellData -> {
        	int rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			DateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm aaa");
			Date date = _taskList.get(rowIndex).getStartDate();

			if (date != null) {
				property.setValue(dateFormat.format(date));
			} 
			return property;
        });
        
        TableColumn<Integer, String> endDate = new TableColumn<>("End Date");
        endDate.setCellValueFactory(cellData -> {
        	int rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			DateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm aaa");
			Date date = _taskList.get(rowIndex).getStartDate();

			if (date != null) {
				property.setValue(dateFormat.format(date));
			} 
			return property;
        });
        
        table.getColumns().add(id);
        table.getColumns().add(taskName);
        table.getColumns().add(startDate);
        table.getColumns().add(endDate);
       
        /*
        column_ID.prefWidthProperty().bind(table.widthProperty().divide(10)); 
        column_Task.prefWidthProperty().bind(table.widthProperty().divide(2)); 
        column_endDate.prefWidthProperty().bind(table.widthProperty().divide(3));
        column_endDate.prefWidthProperty().bind(table.widthProperty().divide(3));
        */
		
        //table.getColumns().addAll(column_ID, column_Task, column_startDate, column_endDate);
        //table.setItems(data);
        

        BorderPane.setMargin(table, new Insets(0,12,12,12));
       
        root.setCenter(table);
        
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
        		
        		updateTable(table);
        	}
        });
        
        /* ------ Event handler for scene switching ----- */
        category2.setOnAction(e -> primaryStage.setScene(workScene)); 
        tab1.setOnAction(e -> primaryStage.setScene(defaultScene));
        
        primaryStage.setTitle("F2DO");
        primaryStage.setScene(defaultScene);
        primaryStage.show();
	}
	
	private void updateTable(TableView<Integer> table) {
		table.getItems().clear();
		
		for (int i = 0; i < _taskList.size(); i++) {
            table.getItems().add(i);
		}
		
		
		//Result result = Parser.parse(input, _taskList);
		
		/* ------ update the dates provided based on each event type ----- */
		/*
		switch(result.getType()) { 
			case DEADLINE: {
				
			}
			case EVENT: {
				//provide either the starting date, or both start and end date.
			}
		    case FLOATING: {
		    	
		    	//no start or end date
		    }
		    case INVALID: {
		    	
		    }
		    default: {
		    	
		    }
		}
		*/
		
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
	}
}
