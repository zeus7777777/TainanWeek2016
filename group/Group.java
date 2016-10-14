package group;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import mysql.Mysql;

public class Group
{
	public static String create(String name, String desc, String img)
	{
		try
		{
			ResultSet rs = Mysql.select("select * from pgroup where name='" + name + "'");
			if(rs.next())
			{
				return "repeated group name";
			}
			Mysql.insert("insert into pgroup(name, description, image) values('"+name+"', '"+desc+"', '"+img+"')");
			return "";
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return "internal error";
		}
	}
	
	public static String read()
	{
		try
		{
			Gson gson = new Gson();
			ArrayList<GroupJson> arr = new ArrayList<GroupJson>();
			ResultSet rs = Mysql.select("select * from pgroup order by id");
			while(rs.next())
			{
				arr.add(new GroupJson(
						rs.getInt("id"), 
						rs.getString("name"), 
						rs.getString("description"), 
						rs.getString("image"))
				);
			}
			rs.close();
			return gson.toJson(arr);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	public static String update(int id, String name, String desc, String img)
	{
		try
		{
			Mysql.update("update pgroup set name='"+name+"',description='"+desc+"' where id='"+id+"'");
			if(img.length()>0)
				Mysql.update("update pgroup set image='"+img+"' where id='"+id+"'");
			return "";
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return "internal error";
		}
	}
	
	public static String delete(int id)
	{
		try
		{
			Mysql.delete("delete from pgroup where id='"+ id +"'");
			Mysql.delete("delete from product where gid='"+ id +"'");
			return "";
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return "internal error";
		}
	}
}
