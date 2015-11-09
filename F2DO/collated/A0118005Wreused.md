# A0118005Wreused
###### src\gui\UITableColumn.java
``` java
public class UITableColumn<S, T> extends TableColumn<S, T> {
	private final DoubleProperty _percentageWidth = new SimpleDoubleProperty(1);
	
	public UITableColumn(String text) {
		super(text);
		this.tableViewProperty().addListener(new ChangeListener<TableView<S>>() {

			@Override
			public void changed(ObservableValue<? extends TableView<S>> observable, TableView<S> oldValue,
					TableView<S> newValue) {
				if(UITableColumn.this.prefWidthProperty().isBound()) {
					UITableColumn.this.prefWidthProperty().unbind();
				}
				UITableColumn.this.prefWidthProperty().bind(newValue.widthProperty().multiply(_percentageWidth));
			}	
		});
	}
	
	/**
	 * Property of width by percentage.
	 * @return width by percentage in DoubleProperty format
	 */
	private DoubleProperty percentageWidthProperty() {
		return _percentageWidth;
	}
	
	/**
	 * Get width by percentage.
	 * @return width by percentage
	 */
	public double getPercentageWidth() {
		return this.percentageWidthProperty().get();
	}
	
	/**
	 * Set the width of column by percentage. Range from 0 to 1.
	 * @param value - percentage
	 * @throws IllegalArgumentException number entered is out of range
	 */
	public void setPercentageWidth(double value) throws IllegalArgumentException {
		if(value >= 0 && value <= 1) {
			this.percentageWidthProperty().set(value);
		} else {
			throw new IllegalArgumentException("Error: The percentage entered is invalid");
		}
	}
}
```
