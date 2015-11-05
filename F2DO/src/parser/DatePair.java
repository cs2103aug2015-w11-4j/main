package parser;

import java.util.Date;

public class DatePair {
	private Date _startDate = null;
	private Date _endDate = null;
	
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
}
