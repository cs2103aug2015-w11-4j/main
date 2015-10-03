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
import parser.Parser;
import parser.Result;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class UserInterface extends Application {
	
	TextField field; 
	
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
        text.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 15));
        text.setFill(Color.DARKTURQUOISE);
        
        field = new TextField();
        Text feedback = new Text();
        feedback.setText("Feedback: ");
        feedback.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 10));
        feedback.setFill(Color.GREY);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(6);
        vbox.getChildren().addAll(text, field, feedback);
        
        root.setTop(vbox);
            
        HBox hbox = new HBox();
        Button category1 = new Button("All"); 
        Button category2 = new Button("Work");
        Button category3 = new Button("Personal");
        hbox.getChildren().addAll(category1, category2, category3);
        
        root.setBottom(hbox);
              
        TableView table = new TableView();
        table.setPrefWidth(10);
        table.setPrefHeight(10);
        TableColumn ID = new TableColumn("ID");
        TableColumn Task = new TableColumn("Task");
        TableColumn DueDate = new TableColumn("Due Date");

        table.getColumns().addAll(ID, Task, DueDate);
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
        		
        		Result result = Parser.Parse(userInput);
        		String taskName = result.getTitle();
        		
        		System.out.println("input: " + userInput);
        		System.out.println("cmd: " + result.getCmd());
        		System.out.println("title: " + result.getTitle());
        		System.out.println("type: " + result.getType());
        		System.out.println("startDate: " + result.getStartDate());
        		System.out.println("endDate:" + result.getEndDate());
        		
        		switch(result.getCmd()) {
        		    case ADD: 
        		    	feedback.setText("Feedback: " + taskName + "has been successfully added!");
        		    	break;
        		    case EDIT:
        		    	feedback.setText("Feedback: Task Description has been modified!");
        		    case DELETE: 
        		    	feedback.setText("Feedback: " + taskName + "has been deleted!");
        		}
        	}
        });
        
        /* ------ Event handler for scene switching ----- */
        category2.setOnAction(e -> primaryStage.setScene(workScene)); 
        tab1.setOnAction(e -> primaryStage.setScene(defaultScene));
        
        primaryStage.setTitle("F2DO");
        primaryStage.setScene(defaultScene);
        primaryStage.show();
	}
}

/*    
 class WindowButtons extends HBox {

    public WindowButtons() {       
        Button closeBtn = new Button("X");       

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();            
            }
        });

        this.getChildren().add(closeBtn);        
    }
}
*/


