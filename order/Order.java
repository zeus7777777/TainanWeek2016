package order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import mysql.Mysql;

public class Order 
{
	public static String create(String dpmt, String name, String stuid, String email, String content, String note,
		String time, String ip, String code, String status, String fb)
	{
		try 
		{
			Mysql.insert(String.format(
				"insert into porder(name, dpmt, stuid, content, note, time, ip, state, code, email, fb)" +
				"values('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
				name, dpmt, stuid, content, note, time, ip, status, code, email, fb
			));
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
		Gson gson = new Gson();
		ArrayList<OrderJson> arr = new ArrayList<>();
		try 
		{
			ResultSet rs = Mysql.select("select * from porder");
			while(rs.next())
			{
				arr.add(new OrderJson(
						rs.getInt("id"), 
						rs.getString("name"), 
						rs.getString("dpmt"), 
						rs.getString("stuid"), 
						rs.getString("content"), 
						rs.getString("note"), 
						rs.getString("fb"), 
						rs.getString("state"),
						rs.getString("email")));
			}
			rs.close();
			return gson.toJson(arr);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return "internal error";
		}
	}
	
	public static String update(int id, String name, String stuid, String dpmt, String content, String note, 
			String state, String email, String fb)
	{
		try 
		{
			Mysql.update("update porder set name='"+name+"', stuid='"+stuid+"', dpmt='"+dpmt+"', content='"+
				content+"', note='"+note+"', state='"+state+"', email='"+email+"', fb='"+fb+"' where id="+id);
			return "";
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return "internal error";
		}
	}
	
	public static String delete(int id)
	{
		try
		{
			Mysql.delete("delete from porder where id='"+ id +"'");
			return "";
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return "internal error";
		}
	}
	
	public static String confirm(String code)
	{
		try
		{
			ResultSet rs = Mysql.select("select * from porder where code='"+code+"'");
			if(rs.next())
			{
				if(rs.getString("state").equals("unchecked"))
				{
					Mysql.update("update porder set state='checked' where code='"+code+"'");
				}
				return new Gson().toJson(new OrderJson(
						rs.getInt("id"), 
						rs.getString("name"), 
						rs.getString("dpmt"), 
						rs.getString("stuid"), 
						rs.getString("content"), 
						rs.getString("note"), 
						rs.getString("fb"), 
						rs.getString("state"),
						rs.getString("email")));
			}
			else
				return "invalid code";
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
			return "internal error";
		}
	}
	
	public static String notify(int id)
	{
		try
		{
			ResultSet rs = Mysql.select("select * from porder where id='"+id+"'");
			if(rs.next())
			{
				if(!rs.getString("state").equals("notified"))
				{
					Mysql.update("update porder set state='notified' where id='"+id+"'");
				}
				return "";
			}
			else
				return "internal error";
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
			return "internal error";
		}
	}
	
	public static String done(int id)
	{
		try
		{
			ResultSet rs = Mysql.select("select * from porder where id='"+id+"'");
			if(rs.next())
			{
				if(!rs.getString("state").equals("done"))
				{
					Mysql.update("update porder set state='done' where id='"+id+"'");
				}
				return "";
			}
			else
				return "internal error";
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
			return "internal error";
		}
	}
}
