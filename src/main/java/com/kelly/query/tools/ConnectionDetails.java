package com.kelly.query.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionDetails {
	
	private static final Logger log = LogManager.getLogger(ConnectionDetails.class);
	
	private String url;
	private String port;
	private String name;
	private String username;
	private String password;
	private boolean autoConnect;
	
	/**
	 * Connection Details for mysql database
	 * @param name
	 * @param username
	 * @param password
	 * @param autoConnect
	 */
	public ConnectionDetails(String name, String username, String password, boolean autoConnect) {
		
		init(name, username, password, autoConnect);
		
		//Set default settings
		this.url = "localhost";
		this.port = "3306";
	}

	/**
	 * Connection Details for mysql database
	 * @param url
	 * @param port
	 * @param name
	 * @param username
	 * @param password
	 * @param autoConnect
	 */
	public ConnectionDetails(String url, String port, String name, String username, String password, boolean autoConnect) {
		
		init(name, username, password, autoConnect);
		
		this.url = url;
		this.port = port;
	}
	
	/**
	 * Initialize the common details
	 * @param name
	 * @param username
	 * @param password
	 * @param autoConnect
	 */
	private void init(String name, String username, String password, boolean autoConnect) {
		
		log.debug("Connection details for database: " + name, "username: "+ username, "autoConnect: "+ autoConnect);
				
		this.name = name;
		this.username = username;
		this.password = password;
		this.autoConnect = autoConnect;
	}

	public String getUrl() {
		return url;
	}

	public String getPort() {
		return port;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isAutoConnect() {
		return autoConnect;
	}
}