package server;

import handler.PublicFileHandler;
import handler.RequestHandler;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class MainServer
{
	public static void main(String[] args)
	{
		try
		{
			HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
			server.createContext("/request", new RequestHandler());
			server.createContext("/", new PublicFileHandler());
			server.setExecutor(new ThreadPerRequestExecutor());
			server.start();
		}
		catch(Exception ee){ee.printStackTrace();}
	}
}