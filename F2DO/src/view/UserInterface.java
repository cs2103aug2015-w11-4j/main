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
	
	private static ArrayList<Task> _taskList;
	private int[] _taskNum = new int[100000];

	/*-----history of commands/input keyed in by user-----*/
	//private ArrayList<String> log; 
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		_taskList = LogicController.getTaskList();
		//log = new ArrayList<String>();
		
		/*-------testing------*/
		if (_taskList.isEmpty()){
			System.out.println("List is empty!");
		}
		
		BorderPane root = new BorderPane();
		Scene defaultScene = new Scene(root, 550, 480);
		
		Text text = new Text();
		setText(text);
		
        TextField field = new TextField();
        
        Text feedback = new Text();
        setFeedback(feedback);
        
        /*
        Text lastAction = new Text();
        setLastAction(lastAction);
        */
        
        VBox vbox = new VBox();
        setVbox(vbox);
        vbox.getChildren().addAll(text, field, feedback/*, lastAction*/);
        
        BorderPane.setMargin(vbox, new Insets(10, 15, 0, 15));
        root.setTop(vbox);
            
        HBox hbox = new HBox();
        Button category1 = new Button("All"); 
        Button category2 = new Button("Work");
        Button category3 = new Button("Personal");
        hbox.getChildren().addAll(category1, category2, category3);
        
        BorderPane.setMargin(hbox, new Insets(0, 25, 20, 25));
        
        root.setBottom(hbox);
        hbox.setSpacing(10);
		int index = 1;
		
		for (int i = 0; i < _taskNum.length; i++) {
			_taskNum[i] = 0;
		}
		
		for (int i = 0; i < _taskNum.length; i++) {
			_taskNum[i] = index;
			index++;
		}
		
		TableView<Integer> table = new TableView<>();
		setTable(table);
       
        
        BorderPane.setMargin(table, new Insets(2,25,20,25));
       
        root.setCenter(table);
        
        /* ----- Setting up the scene for different category ->  Work, Personal etc----- */
        BorderPane work = new BorderPane();
        Scene workScene = new Scene(work, 550, 500);
        BorderPane personal = new BorderPane();
        Scene personalTaskScene = new Scene(personal, 550, 500);
        
        /* ----- Event handler to switch between scenes -> check out the tasks under respective categories ----- */
        HBox categories = new HBox();
        Button tab1 = new Button("All"); 
        Button tab2 = new Button("Work");
        Button tab3 = new Button("Personal");
        categories.getChildren().addAll(tab1, tab2, tab3);
        
        work.setBottom(categories);
        
        /* ----- Event handler for input processing ----- */ 
        setKeyPressed(field, feedback,/*lastAction,*/ table);
        
        /* ------ Event handler for scene switching ----- */
        category2.setOnAction(e -> primaryStage.setScene(workScene)); 
        tab1.setOnAction(e -> primaryStage.setScene(defaultScene));
        
        primaryStage.setTitle("Welcome to F2DO, your personalised task manager (:");
        primaryStage.setScene(defaultScene);
        primaryStage.show();
	}
	
	private void setText(Text text) {
		text.setText("Viewing All Tasks");
        text.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 18));
        text.setFill(Color.DARKTURQUOISE);
	}
	
	private void setFeedback(Text feedback) {
		feedback.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 12));
        feedback.setFill(Color.GREY);
	}
	/*
	private void setLastAction(Text lastAction) {
		lastAction.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 8));
        lastAction.setFill(Color.GREY);
	}
	*/
	
	private void setVbox (VBox vbox) {
		vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10,10,5,10));
        vbox.setSpacing(8);
	}
	
	private void setTable(TableView<Integer> table) {
		for (int i = 0; i < _taskList.size(); i++) {
            table.getItems().add(i);
        }
        
        TableColumn<Integer, Number> id = new TableColumn<>("ID");
        id.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_taskNum[rowIndex]);
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
        	DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
        	DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _taskList.get(rowIndex).getStartDate();
			
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
			Date date = _taskList.get(rowIndex).getEndDate();
			
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
       
        /*
        id.prefWidthProperty().bind(table.widthProperty().divide(10)); 
        taskName.prefWidthProperty().bind(table.widthProperty().divide(2)); 
        startDate.prefWidthProperty().bind(table.widthProperty().divide(5)); 
        endDate.prefWidthProperty().bind(table.widthProperty().divide(5)); 
        */
        
        /*
        table.setColumnResizePolicy(new Callback<TableView.ResizeFeatures<Integer>, Boolean>() {
          @Override
          public Boolean call(ResizeFeatures p) {
             return true;
          }
        });
        */
        
        table.getColumns().add(id);
        table.getColumns().add(taskName);
        table.getColumns().add(startDate);
        table.getColumns().add(endDate);
	}
	
	private void setKeyPressed(TextField field, Text feedback, /*Text lastAction, */ TableView<Integer> table) {
		field.setOnKeyPressed((KeyEvent event) -> {
			
			//int size; 

			if (event.getCode() == KeyCode.ENTER) {

				String userInput = field.getText();
				field.clear();
				
				String feedbackMsg = LogicController.process(userInput, _taskList);
				feedback.setText(feedbackMsg);
				
				/*
				log.add(userInput); 
				size = log.size();
				if (size > 1) {
					lastAction.setText("Last Action: " + log.get(size-1));
				}
				*/

				updateTable(table);
			}
		});
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
