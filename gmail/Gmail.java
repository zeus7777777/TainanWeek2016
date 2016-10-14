package gmail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.client.json.jackson2.JacksonFactory;;


public class Gmail
{
	static String APP_NAME = "zeustest";
	static File DATA_STORE_DIR = new File("stored_credentials/gmail_create_official.json");
	static FileDataStoreFactory DATA_STORE_FACTORY;
	static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	static HttpTransport HTTP_TRANSPORT;
	static List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_SEND);
	
	static com.google.api.services.gmail.Gmail service;
	
	static 
	{
		try
		{
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
			service = getGmailService();
		} 
		catch (Exception ee){ee.printStackTrace();}
	}
	
	public static Credential authorize() throws IOException
	{
		InputStream in = new FileInputStream("/home/zeus/workspace/TainanWeek/credentials/client_secret_official.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			HTTP_TRANSPORT,	JSON_FACTORY, clientSecrets, SCOPES)
			.setDataStoreFactory(DATA_STORE_FACTORY)
			.setAccessType("offline")
			.build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to "+DATA_STORE_DIR.getAbsolutePath());
        return credential;
	}
	
	public static com.google.api.services.gmail.Gmail getGmailService() throws IOException
	{
		Credential credential = authorize();
		return new com.google.api.services.gmail.Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName(APP_NAME)
			.build();
	}
	
	public static MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException
	{
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage email = new MimeMessage(session);
		InternetAddress fa = new InternetAddress(from), ta = new InternetAddress(to);
		email.setFrom(fa);
		email.addRecipient(javax.mail.Message.RecipientType.TO, ta);
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}
	
	public static Message createMessageWithEmail(MimeMessage email) throws IOException, MessagingException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		email.writeTo(bytes);
		Message message = new Message();
		message.setRaw(Base64.encodeBase64URLSafeString(bytes.toByteArray()));
		return message;
	}
	
	public static void sendMessage(com.google.api.services.gmail.Gmail service, String userID, MimeMessage email) throws IOException, MessagingException
	{
		Message message = createMessageWithEmail(email);
		service.users().messages().send(userID, message).execute();
		
		System.out.println("Message id: " + message.getId());
	    System.out.println(message.toPrettyString());
	}
	
	public static void sendEmail(String to, String sub, String body) throws IOException, MessagingException
	{
		sendMessage(service, "me", createEmail(to, "zeus840703@gmail.com", sub, body));
	}
}
