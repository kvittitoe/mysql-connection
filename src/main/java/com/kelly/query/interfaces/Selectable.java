package com.kelly.query.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Selectable extends Queryable {
	public void setProperties(ResultSet rs) throws SQLException;
	public String getSelectSql();
	public List<Object> getSelectParameters();
}
