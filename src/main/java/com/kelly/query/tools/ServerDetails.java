package com.kelly.query.tools;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerDetails {
	private static final Logger log = LogManager.getLogger(ServerDetails.class);
	
	private String catalinaPath;
	private String context;

	public ServerDetails(String catalinaPath, String context) {
		
		log.debug("Initializing Server Details",
				"Catalina Path: " + catalinaPath,
				"Context: " + context);
		
		this.catalinaPath = catalinaPath;
		this.context = context;
	}

	public String getCatalinaPath() {
		return catalinaPath;
	}

	public String getContext() {
		return context;
	}
	
	/**
	 * Find path on the server to the sql folder.
	 * @param sqlName
	 * @return
	 */
	public String getSqlPath(String sqlName) {
		log.debug("Getting sql named: "+sqlName);
		
		StringBuffer sb = new StringBuffer(catalinaPath);
		sb.append(File.separator).append("webapps");
		sb.append(context);
		sb.append(File.separator).append("WEB-INF");
		sb.append(File.separator).append("classes");
		sb.append(File.separator).append("sql");
		sb.append(File.separator).append(sqlName);
		
		return sb.toString();
	}
}