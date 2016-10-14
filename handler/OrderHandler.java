package handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import gmail.Gmail;
import json.JSONObject;
import order.Order;
import server.LoginServer;

public class OrderHandler
{
	public static String createOrder(String body, String rmtAddr)
	{
		JSONObject json = new JSONObject(body);
		rmtAddr = rmtAddr.substring(1, rmtAddr.indexOf(":"));
		Random r = new Random();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), code = "";
		int i;
		for(i=0;i<64;i++)
			code += (char)(r.nextInt(26)+65);
		String rv = Order.create(json.getString("dpmt"), json.getString("name"), json.getString("stuid"),json.getString("email"),
			json.getString("content"), json.getString("note"), time, rmtAddr, code, "unchecked", json.getString("fb"));
		// post-process
		try
		{
			Gmail.sendEmail(json.getString("email"), "府城周訂單確認", 
				json.getString("name")+" 同學你好 請至下方連結完成訂單確認手續\n"+ "http://140.119.152.222:9090/confirm.html?code="+code);
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
			rv = "gmail error";
		}
		return rv;
	}
	
	public static String readOrder(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
			return "invalid user";
		return Order.read();
	}
	
	public static String updateOrder(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
			return "invalid user";
		 return Order.update(json.getInt("id"), json.getString("name"), json.getString("stuid"), 
				 json.getString("dpmt"), json.getString("content"), json.getString("note"), json.getString("state"), 
				 json.getString("email"), json.getString("fb"));
	}
	
	public static String deleteOrder(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
			return "invalid user";
		return Order.delete(json.getInt("id"));
	}
	
	public static String confirmOrder(String body)
	{
		JSONObject json = new JSONObject(body);
		return Order.confirm(json.getString("code"));
	}
	
	public static String notifiedOrder(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
			return "invalid user";
		return Order.notify(json.getInt("id"));
	}
	
	public static String doneOrder(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
			return "invalid user";
		return Order.done(json.getInt("id"));
	}
}
