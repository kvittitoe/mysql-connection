package com.kelly.query.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface Insertable extends Queryable {
	public String getInsertSql();
	public long getInsertedId() throws SQLException;
	public List<Object> getInsertParameters();
}
