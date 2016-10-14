package handler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import product.Product;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;

import json.JSONObject;
import server.LoginServer;

public class ProductHandler
{
	// each method should check user's access with name and password
	
	public static String createProduct(String body) throws IOException
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
		{
			return "invalid user";
		}
		String base64 = json.getString("image"), filePath;
		base64 = base64.substring(base64.indexOf("base64,")+7);
		Random r = new Random();
		filePath = "image/"+r.nextInt(10000000)+""+base64.length()+".jpg";
		FileOutputStream fos = new FileOutputStream("public/"+filePath);
		fos.write(Base64.decodeBase64(base64));
		fos.close();
		System.out.println(filePath);
		return Product.create(json.getInt("gid"), json.getString("name"), json.getString("description"), 
			filePath, json.getInt("price"), json.getInt("price2"));
	}
	
	public static String readProduct()
	{
		return Product.read();
	}
	
	public static String updateProduct(String body) throws IOException
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
			filePath = "image/"+r.nextInt(100000000)+""+base64.length()+".jpg";
			FileOutputStream fos = new FileOutputStream("public/"+filePath);
			fos.write(Base64.decodeBase64(base64));
			fos.close();
			System.out.println(filePath);
		}
		return Product.update(json.getInt("id"), json.getInt("gid"), json.getString("name"), 
			json.getString("description"), filePath, json.getInt("price"), json.getInt("price2"));
	}
	
	public static String deleteProduct(String body)
	{
		JSONObject json = new JSONObject(body);
		if((LoginServer.checkUser(json.getString("username"), json.getString("password"))&1)!=1)
		{
			return "invalid user";
		}
		return Product.delete(json.getInt("id"));
	}
}
