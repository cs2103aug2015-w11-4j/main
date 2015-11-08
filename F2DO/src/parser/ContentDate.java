package parser;

import java.util.Date;

//author A0118005W
public class ContentDate {
	private String _content = null;
	private Date _startDate = null;
	private Date _endDate = null;
	private boolean _isError = false;
	private String _errorMsg = null;
	
	public ContentDate(String content, Date startDate, Date endDate) {
		_content = content;
		_startDate = startDate;
		_endDate = endDate;
	}
	
	public String getContent() {
		return _content;
	}
	
	public Date getStartDate() {
		return _startDate;
	}
	
	public Date getEndDate() {
		return _endDate;
	}
	
	public void setErrorMsg(String msg) {
		_isError = true;
		_errorMsg = msg;
	}
	
	public boolean isError() {
		return _isError;
	}
	
	public String getErrorMsg() {
		return _errorMsg;
	}
}
