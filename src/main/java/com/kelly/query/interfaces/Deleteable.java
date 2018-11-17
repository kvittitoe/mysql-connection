package com.kelly.query.interfaces;

import java.util.List;

public interface Deleteable {
	public String getDeleteSql();
	public List<Object> getDeleteParameters();
}
