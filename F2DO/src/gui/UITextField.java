package gui;

import org.fxmisc.richtext.InlineCssTextArea;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import object.Task;
import type.CommandType;
import type.TaskType;

//@@author A0118005W
public class UITextField extends InlineCssTextArea {
	private static ArrayList<Task> _displayList = new ArrayList<Task>();
	private ContextMenu popupMenu = new ContextMenu();
	
	public UITextField() {
		super();
		setAutoFill();
	}
	
	/**
	 * Update the display list in TextField.
	 * @param displayList - display list
	 */
	public void updateDisplayList(ArrayList<Task> displayList) {
		_displayList = displayList;
	}
	
	/**
	 * Show pop-up menu.
	 */
	public void showPopup() {
		if (!popupMenu.isShowing()) {
			popupMenu.show(this, Side.BOTTOM, 0, 0);
		}
	}
	
	/**
	 * Set up auto fill in of the text field.
	 */
	private void setAutoFill() {
		this.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String text = UITextField.this.getText();
				String[] textTokens = text.split(" ");
				
				popupMenu.hide();
				
				int spaceCount = 0;
				for (int i = 0; i < text.length() && spaceCount < 2; i++) {
					if (text.charAt(i) == ' ') {
						spaceCount += 1;
					}
				}
				
				if (textTokens.length == 2 && spaceCount == 2) {
					String firstToken = textTokens[0];
					CommandType cmd = CommandType.toCmd(firstToken);
					int index = getInteger(textTokens[1]) - 1;
					boolean isWithinRange = ((index >= 0) && (index < _displayList.size()));
					
					if (cmd == CommandType.EDIT && isWithinRange) {
						Task task = _displayList.get(index);
						populatePopup(index, task);
						
						if (!popupMenu.isShowing()) {
							popupMenu.show(UITextField.this, Side.BOTTOM, 0, 0);
						}	
					} 
				} else if (textTokens.length <= 2){
					// Hide pop up
					popupMenu.hide();
					popupMenu.getItems().clear();
				}
				
			}
			
		});
	}
	
	/**
	 * Get the integer from an input string.
	 * If the input cannot be parsed, return -1.
	 * @param input - input string
	 * @return parsed integer
	 */
	private int getInteger(String input) {
		try {
			int integer = Integer.parseInt(input);
			return integer;
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Populate the pop-up box.
	 * @param index - index of the task
	 * @param task - task to be displayed
	 */
	private void populatePopup(int index, Task task) {
		ArrayList<String> displayList = getDisplayItems(index, task);
		ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
		
		for (int i = 0; i < displayList.size(); i++) {
			String str = displayList.get(i);
			Label label = new Label(str);
			CustomMenuItem item = new CustomMenuItem(label, true);

			item.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					replaceText(str);
					positionCaret(str.length());
				}

			});

			menuItems.add(item);
		}
		
		popupMenu.getItems().clear();
		popupMenu.getItems().addAll(menuItems);
	}
	
	/**
	 * Get the command input to be displayed in the pop-up menu.
	 * @param index - index of the task
	 * @param task - task to be displayed
	 * @return display items
	 */
	private ArrayList<String> getDisplayItems(int index, Task task) {
		ArrayList<String> items = new ArrayList<String>();
		TaskType taskType = task.getTaskType();
		
		Integer displayIndex = index + 1;
		String floatingStr = "edit " + displayIndex.toString() + " " + task.getTaskName() + " ";
		String eventStr = floatingStr;
		String alternateEventStr = floatingStr;
		String deadlineStr = floatingStr;
		
		Calendar tmrCalendar = Calendar.getInstance();
		Calendar afterTmrCalendar = Calendar.getInstance();
		tmrCalendar.add(Calendar.DAY_OF_MONTH, 1);	
		afterTmrCalendar.add(Calendar.DAY_OF_MONTH, 2);
		
		Date tomorrow = tmrCalendar.getTime();
		Date afterTomorrow = afterTmrCalendar.getTime();
		Date startDate = task.getStartDate();
		Date endDate = task.getEndDate();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		
		// Set event string
		if (startDate != null && endDate != null) {
			eventStr += "from " + dateFormat.format(startDate) + " ";
			eventStr += "to " + dateFormat.format(endDate);
			
			alternateEventStr += "on " + dateFormat.format(startDate);
		} else if (startDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);	
			
			eventStr += "on " + dateFormat.format(startDate);
			
			alternateEventStr += "from " + dateFormat.format(startDate) + " ";
			alternateEventStr += "to " + dateFormat.format(calendar.getTime());
		} else if (endDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			
			eventStr += "from " + dateFormat.format(endDate) + " ";
			eventStr += "to " + dateFormat.format(calendar.getTime());
			
			alternateEventStr += "on " + dateFormat.format(endDate);
			
		} else {
			eventStr += "from " + dateFormat.format(tomorrow) + " ";
			eventStr += "to " + dateFormat.format(afterTomorrow);
			
			alternateEventStr += "on " + dateFormat.format(tomorrow);
		}
		
		// Set deadline string
		if (endDate != null) {
			deadlineStr += "by " + dateFormat.format(endDate);
		} else if (startDate != null) {
			deadlineStr += "by " + dateFormat.format(startDate);
		} else {
			deadlineStr += "by " + dateFormat.format(tomorrow);
		}
		
		// Assign display order
		int eventIndex = 0;
		int floatingIndex = 1;
		int alternateEventIndex = 2;
		int deadlineIndex = 3;
		int firstIndex = -1;
		
		String[] eventList = {eventStr, floatingStr, alternateEventStr, deadlineStr};
		
		switch (taskType) {
			case EVENT:
				if (endDate == null) {
					items.add(eventList[alternateEventIndex]);
					firstIndex = alternateEventIndex;
				} else {
					items.add(eventList[eventIndex]);
					firstIndex = eventIndex;
				}
				break;
			case DEADLINE:
				items.add(eventList[deadlineIndex]);
				firstIndex = deadlineIndex;
				break;
			case FLOATING:
				items.add(eventList[floatingIndex]);
				firstIndex = floatingIndex;
				break;
			default:
				// Do nothing
		}
		
		for (int i = 0; i < eventList.length; i++) {
			if (i != firstIndex) {
				items.add(eventList[i]);
			}
		}
		
		return items;
	}
}
