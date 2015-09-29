/**
 * 
 */
package main;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import objects.Task;
import view.UserInterface;

/**
 * @author W11 - 4J
 *
 */
public class F2DOMain {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	private ArrayList<Task> taskList;
	
	private boolean initialize() {
		// load GUI
		new JFXPanel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				new UserInterface().start(new Stage());
			}
		});
		System.out.println("UI Loaded");
		
		// check / create any required storage files
		// Load any existing tasks from storage if available
		taskList = new ArrayList<Task>();
		
		return true;
	}
	
	
	private void run() throws Exception {
		if (initialize()){
			// continue
		} else {
			// error occured
			// print error message/state
			System.exit(0);
		}
	}
	
	public static void main(String[] args) throws Exception {
		F2DOMain fdo = new F2DOMain();
		fdo.run();
	}

}
