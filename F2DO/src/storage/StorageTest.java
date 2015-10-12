package storage;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import objects.Task;

public class StorageTest {

	@Test
	public void testAddTask() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Task testTask1 = new Task();
		Storage.taskList.add(testTask1);
		
		String dateInput1 = "24/12/2015";
		Storage.taskList.get(0).setTaskID(1);
		try {
			Storage.taskList.get(0).setStartDate(sdf.parse(dateInput1));
			Storage.taskList.get(0).setEndDate(sdf.parse(dateInput1));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Storage.taskList.get(0).setTaskName("z testing Task 1");
		
		Task testTask2 = new Task();
		Storage.taskList.add(testTask2);

		String dateInput2 = "23/12/2015";
		Storage.taskList.get(1).setTaskID(2);
		try {
			Storage.taskList.get(1).setStartDate(sdf.parse(dateInput2));
			Storage.taskList.get(1).setEndDate(sdf.parse(dateInput2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Storage.taskList.get(1).setStartDate(null);
		Storage.taskList.get(1).setEndDate(null);		
		Storage.taskList.get(1).setTaskName("y testing Task 2");
		
		Task testTask3 = new Task();
		Storage.taskList.add(testTask3);
		
		Storage.taskList.get(2).setTaskID(3);
		Storage.taskList.get(2).setStartDate(null);
		Storage.taskList.get(2).setEndDate(null);		
		Storage.taskList.get(2).setTaskName("x testing Task 3");
		
		Task testTask4 = new Task();
		Storage.taskList.add(testTask4);

		String dateInput3 = "22/12/2015";
		Storage.taskList.get(3).setTaskID(4);
		try {
			Storage.taskList.get(3).setStartDate(sdf.parse(dateInput3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Storage.taskList.get(3).setEndDate(null);		
		Storage.taskList.get(3).setTaskName("w testing Task 4");

		Task testTask5 = new Task();
		Storage.taskList.add(testTask5);

		String dateInput4 = "21/12/2015";
		Storage.taskList.get(4).setTaskID(5);
		Storage.taskList.get(4).setStartDate(null);
		try {
			Storage.taskList.get(4).setEndDate(sdf.parse(dateInput4));
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		Storage.taskList.get(4).setTaskName("v testing Task 5");

		assertTrue(Storage.addTask(testTask1));
		assertTrue(Storage.addTask(testTask2));
		assertTrue(Storage.addTask(testTask3));
		assertTrue(Storage.addTask(testTask4));
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
		assertTrue(Storage.saveToFile());
	}

	@Test
	public void testSortTaskList() {
		assertTrue(Storage.sortTaskList());
		Storage.displayTaskList();
	}
	

}
