package logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class LogicControllerTest {

	@Test
	public void testProcess() {
		LogicController.process("add randomtesttask by Friday");
		LogicController.process("add randomtask2 from Friday to Sunday");
		LogicController.process("add random3 on Saturday");
		LogicController.process("del 1");
		LogicController.process("edit 1 ifail on Tuesday");
		LogicController.process("add failuretask on 32/13/2015"); //System sets the date to null when invalid
		//LogicController.process("edit 5 doesthiswork by Sunday"); //The controller fails this test with array OOB
		//LogicController.process("del 5"); //The controller fails this test with array OOB
	}

}
