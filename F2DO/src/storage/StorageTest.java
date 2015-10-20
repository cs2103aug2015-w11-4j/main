package storage;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.Test;

import objects.Task;

public class StorageTest {

	@Test
	public void testAddTask() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Task testTask1 = new Task();
		
		String dateInput1 = "24/12/2015";
		testTask1.setTaskID(1);
		try {
			testTask1.setStartDate(sdf.parse(dateInput1));
			testTask1.setEndDate(sdf.parse(dateInput1));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		testTask1.setTaskName("z testing Task 1");
		
		assertTrue(Storage.addTask(testTask1));
		
		Task testTask2 = new Task();

		String dateInput2 = "23/12/2015";
		testTask2.setTaskID(2);
		try {
			testTask2.setStartDate(sdf.parse(dateInput2));
			testTask2.setEndDate(sdf.parse(dateInput2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		testTask2.setStartDate(null);
		testTask2.setEndDate(null);		
		testTask2.setTaskName("y testing Task 2");
		
		assertTrue(Storage.addTask(testTask2));

		Task testTask3 = new Task();
		
		testTask3.setTaskID(3);
		testTask3.setStartDate(null);
		testTask3.setEndDate(null);		
		testTask3.setTaskName("x testing Task 3");
		
		assertTrue(Storage.addTask(testTask3));
		
		Task testTask4 = new Task();

		String dateInput3 = "22/12/2015";
		testTask4.setTaskID(4);
		try {
			testTask4.setStartDate(sdf.parse(dateInput3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		testTask4.setEndDate(null);		
		testTask4.setTaskName("w testing Task 4");

		assertTrue(Storage.addTask(testTask4));

		Task testTask5 = new Task();

		String dateInput4 = "21/12/2015";
		testTask5.setTaskID(5);
		testTask5.setStartDate(null);
		try {
			testTask5.setEndDate(sdf.parse(dateInput4));
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		testTask5.setTaskName("v testing Task 5");

		assertTrue(Storage.addTask(testTask5));

		Storage.displayTaskList();
		System.out.println("\n\n");
	}
	
	@Test
	public void testUpdateTask() {
		String updateDateInput1 = "01/01/2015";
		String updateDateInput2 = "02/02/2015";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			assertTrue(Storage.updateTask(4, "first", sdf.parse(updateDateInput1) , sdf.parse(updateDateInput2)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveToFile() {
		assertTrue(Storage.saveToFile(Storage.getTaskList()));
	}

	@Test
	public void testSortTaskList() {
		assertTrue(Storage.sortTaskList());
		Storage.displayTaskList();
		
		//Clearing the file
/*		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Storage.getFilePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.close();
	*/
	}
	
}
