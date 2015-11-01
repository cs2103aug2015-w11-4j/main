package object;

public class Category {

	private String _category = null;

	public Category(String content) {
		this._category = content;
	}

	public String getCategory() {
		return _category;
	}

	public void setCategory(String category) {
		this._category = category;
	}	
}
