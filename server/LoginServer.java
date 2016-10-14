package server;

import java.sql.ResultSet;
import java.sql.SQLException;

import mysql.Mysql;

public class LoginServer
{
	public static int checkUser(String name, String pwd)
	{
		String sql = "select * from account where name='" + name + "'";
		try
		{
			ResultSet rs = Mysql.select(sql);
			if(!rs.next())
			{
				return 0;
			}
			if(rs.getString("password").equals(pwd))
			{
				return rs.getInt("access");
			}
			return 0;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
}
