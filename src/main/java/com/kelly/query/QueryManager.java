package com.kelly.query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kelly.mysql.connection.Connection;
import com.kelly.query.tools.ConnectionDetails;
import com.kelly.query.tools.ServerDetails;

public abstract class QueryManager extends Connection {
	
	private static final Logger log = LogManager.getLogger(QueryManager.class);
	
	protected PreparedStatement ps;
	protected ResultSet rs;
	
	private ServerDetails server;

	public QueryManager(ConnectionDetails connection, ServerDetails server) {
		super(connection);
		this.server = server;
	}
	
	public String getSqlName(String sqlName) {
		return this.server.getSqlPath(sqlName);
	}

	public boolean close(){
		return closeQuery() && closeConnection();
	}
	
	public boolean closeQuery(){
		return closeResultSet() && closePreparedStatement();
	}

	private boolean closePreparedStatement(){
		log.debug("Closing the prepared statement.");
		if (ps != null){
			try {
				ps.close();
				return true;
			} catch (SQLException e) {
				log.warn("Unable to close the prepared statement.", e);
			}
		}
		return false;
	}
	
	private boolean closeResultSet(){
		log.debug("Closing the result set.");
		if (rs != null){
			try {
				rs.close();
				return true;
			} catch (SQLException e) {
				log.warn("Unable to close the result statement.", e);
			}
		}
		return false;
	}
}
