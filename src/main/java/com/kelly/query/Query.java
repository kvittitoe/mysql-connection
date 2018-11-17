package com.kelly.query;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kelly.query.interfaces.Deleteable;
import com.kelly.query.interfaces.Insertable;
import com.kelly.query.interfaces.Queryable;
import com.kelly.query.interfaces.Selectable;
import com.kelly.query.tools.ConnectionDetails;
import com.kelly.query.tools.ServerDetails;

public abstract class Query extends QueryManager {
	
	private static final Logger log = LogManager.getLogger(Query.class);

	public Query(ConnectionDetails connection, ServerDetails server) {
		super(connection, server);
		
		
	}

	/**
	 * Generic Select statement takes in class that impletments selectable
	 * Returns list of type input classes
	 * @param selectable
	 * @return
	 */
	public List<Queryable> select(Class<?> selectable) {
		
		List<Queryable> selected = new ArrayList<>();
		Constructor<?> constructor = null;
		
		Selectable select = null;
		
		try {
			constructor = selectable.getConstructor();
			select = (Selectable) constructor.newInstance();
		} catch (NoSuchMethodException | SecurityException e1) {
			log.fatal("Initialization constuctor " + selectable.getSimpleName(), e1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
			log.fatal("Instanciation constructor " + selectable.getSimpleName(), e2);
		}
		
		String sql = getSqlName(select.getSelectSql());
		List<Object> parameters = select.getSelectParameters();
		
		try {
			
			ps = connection.prepareStatement(sql);
			ps = getParameters(parameters, ps);
			
			rs = ps.executeQuery();
			
			while(rs.next()){
				constructor = selectable.getConstructor();
				select = (Selectable) constructor.newInstance();
				select.setProperties(rs);
				selected.add(select);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e1) {
			log.fatal("Initialization constuctor " + selectable.getSimpleName(), e1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
			log.fatal("Instanciation constructor " + selectable.getSimpleName(), e2);
		} finally {
			closeQuery();
		}
		
		return selected;
	}
	
	public int insert(Insertable insert, boolean getId) {
		int rowsInserted = 0;
		int insertId = 0;
		
		String sql = getSqlName(insert.getInsertSql());
		List<Object> parameters = insert.getInsertParameters();
		
		try {
			
			ps = connection.prepareStatement(sql);
			ps = getParameters(parameters, ps);
			rowsInserted = ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			if(rs.next()) {
				insertId = rs.getInt(1);
			}
			
			log.debug("Rows inserted: " + rowsInserted);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuery();
		}
		
		if(getId) {
			return insertId;
		}else {
			return rowsInserted;
		}
	}
	
	public int delete(Deleteable delete) {

		int retcode = 0;
		
		String sql = getSqlName(delete.getDeleteSql());
		List<Object> parameters = delete.getDeleteParameters();
		
		try {
			ps = connection.prepareStatement(sql);
			ps = getParameters(parameters, ps);
			retcode = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeQuery();
		}
		
		return retcode;
	}
	
	public void truncate(String tableName) {
		log.debug("Truncating table: " + tableName);
		
		String sql = "truncate table " + tableName;
		
		try {
			ps = connection.prepareStatement(sql);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private PreparedStatement getParameters(List<Object> parameters, PreparedStatement ps) throws SQLException {
		
		for(int i=0; i<parameters.size(); i++) {
			Object parameter = parameters.get(i);
			String className = parameter.getClass().getName();
				
			switch(className) {
			case "java.lang.String": 
				ps.setString(i+1, (String) parameter);
				log.debug("Parameter [" + i+1 + "] of type String: " + parameter);
				break;
			case "java.lang.Integer":
				ps.setInt(i+1, (Integer) parameter);
				log.debug("Parameter [" + i+1 + "] of type Integer: " + parameter);
				break;
			case "java.lang.Long":
				ps.setLong(i+1, (Long) parameter);
				log.debug("Parameter [" + i+1 + "] of type Long: " + parameter);
				break;				
			case "java.math.BigDecimal":
				ps.setBigDecimal(i+1, (BigDecimal) parameter);
				log.debug("Parameter [" + i+1 + "] of type BigDecimal: " + parameter);
				break;
			case "java.sql.Date":
				ps.setDate(i+1, (Date) parameter);
				log.debug("Parameter [" + i+1 + "] of type Date: " + parameter);
				break;
			default: log.fatal("Unimplemented class type: " + className);
			}
		}
		
		return ps;
	}
}