//@@author Cher Lin
package gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;
import object.Task;

public class UITable extends TableView<Integer> {
	private ArrayList<Task> _displayList = new ArrayList<Task>();
	private int _rowIndex = 0;
	private int _displayIndex = 0;
	private boolean _isFloating;
	private DateFormat dateFormat = new SimpleDateFormat("dd MMM HH:mm");
	
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
			int floatingMaxSize = nonFloatingList.size() + floatingList.size();
			
			for (_displayIndex = nonFloatingList.size(); 
					_displayIndex < floatingMaxSize; 
					_displayIndex++) {
				this.getItems().add(_displayIndex);
			}
		}
	}
	
	/**
	 * Set non-floating table.
	 */
	private void setNonFloatingTable() {
		UITableColumn<Integer, Number> id = new UITableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        UITableColumn<Integer, String> taskName = new UITableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });

        UITableColumn<Integer, String> startDate = new UITableColumn<>("Start Date");
        startDate.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			Date date = _displayList.get(_rowIndex).getStartDate();
			
			if (date != null) {
				property.setValue(dateFormat.format(date));
			} 
	
			return property;
        });
        
        UITableColumn<Integer, String> endDate = new UITableColumn<>("End Date");
        endDate.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
        	SimpleStringProperty property = new SimpleStringProperty();
			Date date = _displayList.get(_rowIndex).getEndDate();
			
			if (date != null) {
				property.setValue(dateFormat.format(date));
			} else {
				property.setValue("-");
			}
			
			return property;
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER-LEFT;");
        startDate.setStyle( "-fx-alignment: CENTER;");
        endDate.setStyle( "-fx-alignment: CENTER;");
        
        // Percentage width must sum up to 1
        try {
        	id.setPercentageWidth(0.1);
        	taskName.setPercentageWidth(0.5);
        	startDate.setPercentageWidth(0.2);
        	endDate.setPercentageWidth(0.2);
        } catch (Exception e) {
        	this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        
        this.getColumns().add(id);
        this.getColumns().add(taskName);
        this.getColumns().add(startDate);
        this.getColumns().add(endDate);
	}
	
	/**
	 * Set floating table.
	 */
	private void setFloatingTable() {
		UITableColumn<Integer, Number> id = new UITableColumn<>("Task#");
        id.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(_rowIndex + 1);
        });

        UITableColumn<Integer, String> taskName = new UITableColumn<>("Task Description");
        taskName.setCellValueFactory(cellData -> {
        	_rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(_displayList.get(_rowIndex).getTaskName());
        });
        
        id.setStyle( "-fx-alignment: CENTER;");
        taskName.setStyle( "-fx-alignment: CENTER-LEFT;");
        
        // Percentage width must sum up to 1
        try {
        	id.setPercentageWidth(0.1);
        	taskName.setPercentageWidth(0.9);
        } catch (Exception e) {
        	this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        
        this.getColumns().add(id);
        this.getColumns().add(taskName);
	}
}
