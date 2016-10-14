package handler;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Date;

import com.sun.net.httpserver.*;

public class PublicFileHandler implements HttpHandler
{
	public void handle(HttpExchange arg0) throws IOException
	{
		String prefix = "public", path = URLDecoder.decode(arg0.getRequestURI().toString(), "UTF-8");
		path = path.substring(0, path.lastIndexOf("?")==-1?path.length():path.lastIndexOf("?"));
		if(path.endsWith("/"))
		{
			path += "index.html";
		}
		System.out.println(new Date().toString() +" "+ arg0.getRemoteAddress()+" "+ prefix+path);
		File f = new File(prefix+path);
		if(path.indexOf("../")==-1 && f.exists() && !f.isDirectory())
		{
			arg0.sendResponseHeaders(200, f.length());
			OutputStream os = arg0.getResponseBody();
			Files.copy(f.toPath(), os);
			os.close();
			return;
		}
		arg0.sendResponseHeaders(404, "".getBytes().length);
		OutputStream os = arg0.getResponseBody();
		os.write("".getBytes());
		os.close();
	}
}
