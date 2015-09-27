/**
 * 
 */
package main;

import java.util.ArrayList;
import objects.Task;
import view.ViewFrame;

/**
 * @author W11 - 4J
 *
 */
public class F2DOMain {

	/**
	 * @param args
	 */
	private ArrayList<Task> taskList;
	private ViewFrame vf;
	
	
	
	private boolean initialize() {
		// load GUI
		ViewFrame.initView();
		vf = new ViewFrame();
		System.out.println("View Loaded");
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
