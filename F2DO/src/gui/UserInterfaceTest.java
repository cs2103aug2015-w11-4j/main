package gui;

import org.junit.Test;
import org.loadui.testfx.Assertions;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.controls.TableViews;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class UserInterfaceTest extends GuiTest {

	@Override
	protected Parent getRootNode() {
		UserInterface UI = new UserInterface();
		return UI.getRootNode();
	}
	
	@Test
	public void testTextArea() {
		TableView<S> table = (TableView)find("floatingTable");
		click("#textarea").type("testing 1").push(Enter);
		Assertions.verifyThat(table, TableViews.containsCell("testing 1"));
	}
}
