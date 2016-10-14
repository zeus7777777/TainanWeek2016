package handler;

import group.Group;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import server.LoginServer;
import json.JSONObject;

public class GroupHandler
{
	// each method should check user's access with name and password
	
	public static String createGroup(String body) throws IOException
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
		{
			return "invalid user";
		}
		String base64 = json.getString("image"), filePath;
		base64 = base64.substring(base64.indexOf("base64,")+7);
		Random r = new Random();
		filePath = "image/"+r.nextInt(1000000000)+""+base64.length()+".jpg";
		FileOutputStream fos = new FileOutputStream("public/"+filePath);
		fos.write(Base64.decodeBase64(base64));
		fos.close();
		System.out.println(filePath);
		return Group.create(json.getString("name"), json.getString("description"), filePath);
	}
	
	public static String readGroup()
	{
		return Group.read();
	}
	
	public static String updateGroup(String body) throws IOException
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
		{
			return "invalid user";
		}
		String filePath = "";
		if(json.getString("image").length()!=0)
		{
			String base64 = json.getString("image");
			base64 = base64.substring(base64.indexOf("base64,")+7);
			Random r = new Random();
			filePath = "image/"+r.nextInt(1000000000)+""+base64.length()+".jpg";
			FileOutputStream fos = new FileOutputStream("public/"+filePath);
			fos.write(Base64.decodeBase64(base64));
			fos.close();
			System.out.println(filePath);
		}
		return Group.update(json.getInt("id"), json.getString("name"), json.getString("description"), filePath);
	}
	
	public static String deleteGroup(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
		{
			return "invalid user";
		}
		return Group.delete(json.getInt("id"));
	}
}
