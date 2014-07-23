package com.crawler.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.crawler.Container;

public class Connector {
	
	private static String userName = Container.getContainer().getProperty("sql.server.db.username");
	private static String password = Container.getContainer().getProperty("sql.server.db.psssword");
	private static String url = Container.getContainer().getProperty("sql.server.db.url");
	private static String driver = Container.getContainer().getProperty("sql.server.db.driver"); 
	private Connection conn;
	
	static {
		try {
//			userName = Container.getContainer().getProperty("sql.server.db.username");
//			password = Container.getContainer().getProperty("sql.server.db.psssword");
//			url = Container.getContainer().getProperty("sql.server.db.url");
//			driver = Container.getContainer().getProperty("sql.server.db.driver");
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
	}
	
	private Connection getconnection() throws SQLException{
		return DriverManager.getConnection(url, userName, password);
	}
	
		
	public void open() throws SQLException{
		this.conn = getconnection();
	}
	
	public void close() throws SQLException{
		if (conn!=null) {
			conn.close();
			conn = null;
		}
	}
	
	private Statement getStatement() throws SQLException{
		return conn.createStatement(); 
	}
	
	
	public ResultSet query(String sql) { 
		ResultSet rs = null;
        try {   
        	rs = getStatement().executeQuery(sql);  
        } catch (SQLException e) {   
            e.printStackTrace();   
        }   
        return rs;   
    }   
	
	public void executeInsert(String sql) throws SQLException  
    {  
		getStatement().execute(sql);
    }  
	
	public void close(ResultSet rs) throws SQLException  
    {  
        if (null != rs)  
        {  
            rs.close();  
            rs = null;  
        }  
    }  
  
    public void close(Statement stmt) throws SQLException  
    {  
        if (null != stmt)  
        {  
            close(stmt.getResultSet());  
            stmt.close();  
            stmt = null;  
        }  
    }  

}
