package application;
	
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
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;



public class UserInterface extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,400,400);
		//root.setAlignment(Pos.TOP_CENTER);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		//scene.getStylesheets().add("application.css");
		
		Text text = new Text();
        text.setText("F2DO, your personalised task manager");
        text.setFont(Font.font ("Verdana", FontWeight.SEMI_BOLD, 16));
        text.setFill(Color.DARKTURQUOISE);
        
        TextField field = new TextField();
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        vbox.getChildren().addAll(text, field);
        
        root.setTop(vbox);
            
        HBox hbox = new HBox();
        Button category1 = new Button("All");
        Button category2 = new Button("Work");
        Button category3 = new Button("Personal");
        hbox.getChildren().addAll(category1, category2, category3);
       
        root.setBottom(hbox);
        
        /*
        Label ID = new Label("ID");
        Label Task = new Label("Task");
        Label DueDate = new Label("Due Date");
        HBox tasks = new HBox();
        tasks.setAlignment(Pos.TOP_CENTER);
        tasks.setPadding(new Insets(10));
        tasks.setSpacing(100);
        tasks.getChildren().addAll(ID, Task, DueDate);
        
        root.setCenter(tasks);
        */
        
        TableView table = new TableView();
        table.setPrefSize(250, 250);
        
        TableColumn ID = new TableColumn("ID");
        TableColumn Task = new TableColumn("Task");
        TableColumn DueDate = new TableColumn("Due Date");
      
        table.getColumns().addAll(ID, Task, DueDate);
        
        root.setCenter(table);
        
        field.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                String userInput = field.getText();
                field.clear();
                
                System.out.println(userInput);
                
                
            }
        });
        
        primaryStage.setTitle("F2DO");
		primaryStage.setScene(scene);
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

    




    


