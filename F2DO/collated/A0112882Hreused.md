# A0112882Hreused
###### tests\gui\UserInterfaceTest.java
``` java
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

```
