package com.kelly.query;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public abstract class Row {

	private long id;
	private Date effdt;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setId(String id) {
		this.id = Long.parseLong(id);
	}
	public Date getEffdt() {
		return effdt;
	}
	public void setEffdt(Date effdt) {
		this.effdt = effdt;
	}
	public void setEffdt(String effdt) {
		this.effdt = strToSqlDate(effdt);
	}
	
	public Date strToSqlDate(String dateStr){
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date;
		java.sql.Date sqlStartDate = null;
		try {
			date = sdf1.parse(dateStr);
			sqlStartDate = new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return sqlStartDate;
	}
}
