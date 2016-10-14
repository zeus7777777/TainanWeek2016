package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mysql
{
	static String username = "", userpass = "", DBName = "tnweek2016";
	static Mysql mysql = null;
	static Connection con = null;
	
	static
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBName +"?autoReconnect=true", username, userpass);
			insert("SET global interactive_timeout = 31536000");
			insert("SET global wait_timeout = 31536000");
		} 
		catch (Exception e){e.printStackTrace();}
	}
	
	public static void insert(String sql) throws SQLException 
	{
		PreparedStatement ps = con.prepareStatement(sql);
		ps.executeUpdate();
		ps.close();
	}
	
	public static void update(String sql) throws SQLException
	{
		insert(sql);
	}
	
	public static ResultSet select(String sql) throws SQLException
	{
		PreparedStatement ps = con.prepareStatement(sql);
		return ps.executeQuery();
	}
	
	public static void delete(String sql) throws SQLException
	{
		insert(sql);
	}
}
