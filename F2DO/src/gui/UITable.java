package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import object.Task;

public class UITable extends TableView<Integer> {
	private ArrayList<Task> _displayList = new ArrayList<Task>();
	private int _rowIndex = 0;
	private int _displayIndex = 0;
	private boolean _isFloating;
	
	/**
	 * Constructor of UITable.
	 * @param isFloating - true if this table is for displaying floating tasks; false otherwise
	 */
	public UITable(boolean isFloating) {
		_isFloating = isFloating;
		
		if (isFloating) {
			setFloatingTable();
		} else {
			setNonFloatingTable();
		}
	}
	
	/**
	 * Update the table.
	 * @param nonFloatingList - non-floating task list
	 * @param floatingList - floating task list
	 */
	public void updateTable(ArrayList<Task> nonFloatingList, ArrayList<Task> floatingList) {
		this.getItems().clear();
		
		_displayList.clear();
		_displayList.addAll(nonFloatingList);
		_displayList.addAll(floatingList);
		
		if (!_isFloating) {
			for (_displayIndex = 0; _displayIndex < nonFloatingList.size(); _displayIndex++) {
				this.getItems().add(_displayIndex);
			}
		} else {
			int nonFloatingMaxSize = nonFloatingList.size() + floatingList.size();
			
			for (_displayIndex = nonFloatingList.size(); 
					_displayIndex < nonFloatingMaxSize; 
					_displayIndex++) {
				this.getItems().add(_displayIndex);
			}
		}
	}
	
	/**
	 * Set non-floating table.
	 */
	private void setNonFloatingTable() {
		TableColumn<Integer, Number> id = new TableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });

        TableColumn<Integer, String> startDate = new TableColumn<>("Start Date");
        startDate.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
        	DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
        	DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _displayList.get(_rowIndex).getStartDate();
			
			if (date != null) {
				String date_string = date.toString();
				//- to be edited : would not work if user inputs 12:00am/pm as the time
				if (date_string.contains("12:00")) {
					property.setValue(dateWithoutTime.format(date));
				} else {
					property.setValue(dateWithTime.format(date));
				}		
			} else {
				property.setValue("?");
			} 
	
			return property;
        });
        
        TableColumn<Integer, String> endDate = new TableColumn<>("End Date");
        endDate.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			DateFormat dateWithTime = new SimpleDateFormat("dd MMM HH:mm");
			DateFormat dateWithoutTime = new SimpleDateFormat("dd MMM");
			Date date = _displayList.get(_rowIndex).getEndDate();
			
			if (date != null) {
				String date_string = date.toString();
				//- to be edited : would not work if user inputs 12:00am/pm as the time
				if (date_string.contains("12:00")) {
					property.setValue(dateWithoutTime.format(date));
				} else {
					property.setValue(dateWithTime.format(date));
				}		
			} else {
				property.setValue("?");
			} 
			
			return property;
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER;");
        startDate.setStyle( "-fx-alignment: CENTER;");
        endDate.setStyle( "-fx-alignment: CENTER;");
        
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        this.getColumns().add(id);
        this.getColumns().add(taskName);
        this.getColumns().add(startDate);
        this.getColumns().add(endDate);
	}
	
	/**
	 * Set floating table.
	 */
	private void setFloatingTable() {
		TableColumn<Integer, Number> id = new TableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        TableColumn<Integer, String> taskName = new TableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER;");
        
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        this.getColumns().add(id);
        this.getColumns().add(taskName);
	}
}
