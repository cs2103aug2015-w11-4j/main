package date;

import java.util.Date;

public class ParsedDate {
	private Date _date;
	private boolean _isValid;
	private boolean _isAbsolute;
	
	public ParsedDate() {
		_date = null;
		_isValid = false;
		_isAbsolute = false;
	}
	
	public ParsedDate(Date date, boolean isValid, boolean isAbsolute) {
		_date = date;
		_isValid = isValid;
		_isAbsolute = isAbsolute;
	}
	
	public Date getDate() {
		return _date;
	}
	
	public boolean isValid() {
		if (_date == null) {
			return true;
		}
		return _isValid;
	}
	
	public boolean isAbsolute() {
		return _isAbsolute;
	}
	
}
