package gui;

import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;

//@@author A0112882H-reused
public class UserInterfaceTest {
	private static GuiTest controller;
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
		FXTestUtils.launchApp(UserInterface.class);
		Thread.sleep(7000);
		
		controller = new GuiTest() {
			@Override
			protected Parent getRootNode() {
				return stage.getScene().getRoot();
			}
		};
		System.out.println("GUI TEST START");

	}

	@Test
	public void testAddFloatingTask() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		System.out.println("TESTING GUI");
		controller.click(textField).type("add Meeting with boss").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}

	/*	
	@Test
	public void testTextArea() {
		TableView<S> table = (TableView)find("floatingTable");
		click("#textarea").type("testing 1").push(Enter);
		Assertions.verifyThat(table, TableViews.containsCell("testing 1"));
	}*/

}
