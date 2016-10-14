package product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;

import mysql.Mysql;

public class Product
{
	public static String create(int gid, String name, String description, String image, int price, int price2)
	{
		try
		{
			ResultSet rs = Mysql.select("select * from product where name='" + name + "'");
			if(rs.next())
			{
				return "repeated product name";
			}
			Mysql.insert("insert into product(gid,name,description,image,price,price2)"+
				"values('"+gid+"', '"+name+"', '"+description+"', '"+image+"', '"+price+"', '"+price2+"')");
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
			ArrayList<ProductJson> arr = new ArrayList<ProductJson>();
			ResultSet rs = Mysql.select("select * from product");
			while(rs.next())
			{
				arr.add(new ProductJson(
						rs.getInt("id"), 
						rs.getInt("gid"), 
						rs.getString("name"), 
						rs.getString("description"),
						rs.getString("image"),
						rs.getInt("price"),
						rs.getInt("price2"))
				);
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
	
	public static String update(int id, int gid, String name, String description, String image, int price, int price2)
	{
		try
		{
			Mysql.update("update product set gid='"+gid+"', name='"+name+"', description='"+description+"'"+
				", price='"+price+"' , price2='"+price2+"' where id='"+id+"'");
			if(image.length()>0)
				Mysql.update("update product set image='"+image+"' where id='"+id+"'");
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
			Mysql.delete("delete from product where id='"+ id +"'");
			return "";
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			return "internal error";
		}
	}
}
