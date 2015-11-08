package date;

import java.util.Date;

public class DatePair {
	private Date _startDate = null;
	private Date _endDate = null;
	private String _dateString = null;
	private String _errorMsg = null;
	private boolean _isError = false;
	
	public DatePair(Date startDate, Date endDate) {
		_startDate = startDate;
		_endDate = endDate;
	}
	
	public Date getStartDate() {
		return _startDate;
	}
	
	public Date getEndDate() {
		return _endDate;
	}
	
	public void setDateString(String dateString) {
		_dateString = dateString;
	}
	
	public String getDateString() {
		return _dateString;
	}
	
	public void setErrorMsg(String msg) {
		_errorMsg = msg;
	}
	
	public boolean isError() {
		return _isError;
	}
	
	public String getErrorMsg() {
		return _errorMsg;
	}
}
