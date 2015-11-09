package gui;

import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import com.google.common.util.concurrent.SettableFuture;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

//@@author A0112882H-reused
public class UserInterfaceTest {
	private static final SettableFuture<Stage> stageFuture = SettableFuture.create();
	private static GuiTest controller;
	
	 @BeforeClass
	    public static void setUpClass() {
	            FXTestUtils.launchApp(UserInterface.class);

	            controller = new GuiTest() {
	                @Override
	                protected Parent getRootNode() {
	                    return stage.getScene().getRoot();
	                }
	            };
	            System.out.println("GUI TEST START");

	        }
	
	/*
	@Override
	protected Parent getRootNode() {
		return stage.getScene().getRoot();
	}

	@Override
	public void setupStage() throws Throwable {
		FXTestUtils.launchApp(UserInterface.class);
		stage = targetWindow(stageFuture.get(25, TimeUnit.SECONDS));
		FXTestUtils.bringToFront(stage);
	}
	*/
	@Test
	public void test() throws Exception {
		TextField textField = (TextField)GuiTest.find("_field");
		System.out.println("TESTING GUI");
		controller.click(textField).type("add task").push(KeyCode.ENTER);
		
	}
	@Test
	public void test2() throws Exception {
		TextField textField = (TextField)GuiTest.find("_field");
		System.out.println("TESTING GUI");
		controller.click(textField).type("add task").push(KeyCode.ENTER);
		
	}
	@Test
	public void test3() throws Exception {
		TextField textField = (TextField)GuiTest.find("_field");
		System.out.println("TESTING GUI");
		controller.click(textField).type("add task").push(KeyCode.ENTER);
		
	}
	@Test
	public void test4() throws Exception {
		TextField textField = (TextField)GuiTest.find("_field");
		System.out.println("TESTING GUI");
		controller.click(textField).type("add task").push(KeyCode.ENTER);
		
	}
	


/*	
	@Test
	public void testTextArea() {
		TableView<S> table = (TableView)find("floatingTable");
		click("#textarea").type("testing 1").push(Enter);
		Assertions.verifyThat(table, TableViews.containsCell("testing 1"));
	}*/

}
