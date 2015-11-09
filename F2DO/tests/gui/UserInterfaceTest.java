package gui;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import javafx.scene.Parent;
import javafx.scene.input.KeyCode;

//@@author A0112882H-reused
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
	public void test1ShowUndoneEmpty() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("show undone").push(KeyCode.ENTER);
		Assertions.assertNodeExists("");
	}
	
	@Test
	public void test2AddFloatingTask() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("add Meeting with boss").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}

	@Test
	public void test3Search() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("search Meeting with boss").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}
	
	@Test
	public void test4ShowUndone() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("show undone").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}
	
	@Test
	public void test5MarkDone() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("done 1").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}
	
	@Test
	public void test6ShowDone() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("show done").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}

	@Test
	public void test7ShowAll() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("show all").push(KeyCode.ENTER);
		Assertions.assertNodeExists("Meeting with boss");
	}

	@Test
	public void test8Help() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("help").push(KeyCode.ENTER);
//		Assertions.assertNodeExists(hasText("Cheat Sheet"));
	}

	@Test
	public void test9Home() throws Exception {
		UITextField textField = (UITextField)GuiTest.find("#textarea");
		controller.click(textField).type("home").push(KeyCode.ENTER);
		Assertions.assertNodeExists("");
	}
	
	/*	
	@Test
	public void testTextArea() {
		TableView<S> table = (TableView)find("floatingTable");
		click("#textarea").type("testing 1").push(Enter);
		Assertions.verifyThat(table, TableViews.containsCell("testing 1"));
	}*/

}
