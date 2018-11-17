package com.kelly.mysql.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kelly.query.tools.ConnectionDetails;

public abstract class Connection {
	private static final Logger log = LogManager.getLogger(Connection.class);
	
	protected java.sql.Connection connection;
	
	private State state;
	
	private ConnectionDetails details;
	
	public enum State {
		INIT,
		CONNECTED,
		CLOSED,
		SQL_EXCEPTION,
		CONNECTION_ERROR
	}

	public Connection(ConnectionDetails details) {
		
		state = State.INIT;
		
		this.details = details;
		
		if(details.isAutoConnect()) {
			log.debug("Auto connecting to the database.");
			initConnection();
		}
	}
	
	public void initConnection(){
		log.debug("Initializing connection.");
		
		if (state == State.INIT) {
			this.connection = connect();
			
			if(this.connection == null){
				log.fatal("Could not connect the the database.");
				this.setState(State.CONNECTION_ERROR);
			}else{
				log.debug("Connected to database: " + this.details.getName());
				this.setState(State.CONNECTED);
			}
		}else {
			log.warn("Unable to connect to the database due to unexpected database state: " + this.state);
		}
	}
	
	/**
	 * Generate mysql connection string
	 * Initialize the connection
	 * @return
	 */
	private java.sql.Connection connect(){
		java.sql.Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log.fatal("Unable to find the appropriate mysql driver.", e);
		}
		
		String connStr = getConnectionString();
		
		try {
			connection = DriverManager
							.getConnection(connStr, 
								this.details.getUsername(), 
								this.details.getPassword());
			
			connection.setAutoCommit(false);
			log.debug("Connected to the database successfully.");
		} catch (SQLException e) {
			log.fatal("Unable to connect to the mysql database.", e);
		}
		
		return connection;
	}
	
	/**
	 * Write the mysql connections string from Connection Details
	 * sample: jdbc:mysql://url:port/dbname&utoReconnect=true&useSSL=false
	 * @return
	 */
	private String getConnectionString() {
		
		StringBuilder connStr = new StringBuilder("jdbc:mysql://");
		connStr.append(this.details.getUrl()).append(":");
		connStr.append(this.details.getPort()).append("/");
		connStr.append(this.details.getName());
		connStr.append("?").append("autoReconnect=true");
		connStr.append("&").append("useSSL=false");
		
		return connStr.toString();
	}
	
	public boolean closeConnection(){
		if(this.connection == null) {
			log.warn("Connection is null.  Cannot close.");
			return false;
		}
		if(this.state == State.CLOSED || this.state == State.INIT) {
			log.warn("Connection is not in expected state: " + this.state);
			return false;
		}
		
		try {
			this.connection.close();
			this.setState(State.CLOSED);
			return true;
		} catch (SQLException e) {
			log.warn("Failed to close the connection.", e);
		}
		
		return false;
	}
	
	/**
	 * Commit data into the database.  
	 * Auto commit is false.
	 */
	public void commit(){
		log.info("Data committed");
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Rollback any changes to the database.
	 * Auto commit is false.
	 */
	public void rollback(){
		log.warn("Data rolledback");
		try {
			connection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setState(State state) {
		this.state = state;
	}
}
