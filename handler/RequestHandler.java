package handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;

import server.LoginServer;

import json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler
{
	public void handle(HttpExchange arg0) throws IOException
	{
		String result = "internal error";
		try
		{
			String query = arg0.getRequestURI().getRawQuery();
			HashMap<String, String> q = parseQuery(query);
			if(q.get("t").equals("0")) // login
			{
				String body = getRequestBody(arg0);
				System.out.println(body);
				JSONObject json = new JSONObject(body);
				result = LoginServer.checkUser(json.getString("username"), json.getString("password"))>0?"":"invalid user";
			}
			else if(q.get("t").equals("1")) // group request
			{
				String body = getRequestBody(arg0); // body may include account information
				System.out.println(new Date().toString()+" "+arg0.getRemoteAddress()+" "+arg0.getRequestURI());
				System.out.println(body);
				if(q.get("a").equals("1")) // create
				{
					result = GroupHandler.createGroup(body);
				}
				else if(q.get("a").equals("2")) // read
				{
					result = GroupHandler.readGroup();
				}
				else if(q.get("a").equals("3")) // update
				{
					result = GroupHandler.updateGroup(body);
				}
				else if(q.get("a").equals("4")) //delete
				{
					result = GroupHandler.deleteGroup(body);
				}
			}
			else if(q.get("t").equals("2")) // product request
			{
				String body = getRequestBody(arg0); // body may include account information
				System.out.println(new Date().toString()+" "+arg0.getRemoteAddress()+" "+arg0.getRequestURI());
				System.out.println(body);
				if(q.get("a").equals("1")) // create
				{
					result = ProductHandler.createProduct(body);
				}
				else if(q.get("a").equals("2")) // read
				{
					result = ProductHandler.readProduct();
				}
				else if(q.get("a").equals("3")) // update
				{
					result = ProductHandler.updateProduct(body);
				}
				else if(q.get("a").equals("4")) //delete
				{
					result = ProductHandler.deleteProduct(body);
				}
			}
			else if(q.get("t").equals("3")) // order request
			{
				String body = getRequestBody(arg0);
				System.out.println(new Date().toString()+" "+arg0.getRemoteAddress()+" "+arg0.getRequestURI());
				System.out.println(body);
				if(q.get("a").equals("1")) // create
				{
					result = OrderHandler.createOrder(body, arg0.getRemoteAddress().toString());
				}
				else if(q.get("a").equals("2")) // read
				{
					result = OrderHandler.readOrder(body);
				}
				else if(q.get("a").equals("3")) // update
				{
					result = OrderHandler.updateOrder(body);
				}
				else if(q.get("a").equals("4")) // delete
				{
					result = OrderHandler.deleteOrder(body);
				}
				else if(q.get("a").equals("5")) // confirm
				{
					result = OrderHandler.confirmOrder(body);
				}
				else if(q.get("a").equals("6")) // notified
				{
					result = OrderHandler.notifiedOrder(body);
				}
				else if(q.get("a").equals("7")) // done
				{
					result = OrderHandler.doneOrder(body);
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		arg0.sendResponseHeaders(200, result.getBytes().length);
		OutputStream os = arg0.getResponseBody();
		os.write(result.getBytes());
		os.close();
	}
	
	public String getRequestBody(HttpExchange arg0) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(arg0.getRequestBody()));
		String body = "", line;
		while((line=br.readLine())!=null)
		{
			body += line;
		}
		return body;
	}
	
	public HashMap<String, String> parseQuery(String query)
	{
		int i;
		String[] tok = query.split("\\?|=|&");
		HashMap<String, String> hm = new HashMap<String, String>();
		for(i=0;i<tok.length/2;i++)
		{
			hm.put(tok[i*2], tok[i*2+1]);
		}
		return hm;
	}
}
