package com.libu.expensecalulator.gmail;
 
import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import android.util.Log;

import com.libu.expensecalulator.utils.Constants;

 
public class EmailManager {
	private String stmpHost = "smtp.gmail.com";
	private String mailServer = "imap.gmail.com";
	private EmailAccount account;
	private Session smtpSession; 
	private Session imapSession; 
	
	private Folder inbox;
	private Folder readFolder;
	private String readFolderName= "readFolder";
	private Store store;
	public EmailManager(String username, String password, String urlServer, String stmpHost, String mailServer) {
		account = new EmailAccount(username, password, urlServer);
		this.stmpHost = stmpHost;
		this.mailServer = mailServer;
		initProtocol();
	}
	
	public EmailManager() {
		account = new EmailAccount();
		this.stmpHost =  "smtp.gmail.com";
		this.mailServer = "imap.gmail.com";
		initProtocol();
	}
	private void initProtocol() {
		Authenticator authenticator = new EmailAuthenticator(account);
		
		Properties props1 = new Properties();  
		props1.setProperty("mail.transport.protocol", "smtps");  
		props1.setProperty("mail.host", stmpHost);  
		props1.put("mail.smtp.auth", "true");  
		props1.put("mail.smtp.port", "465");  
		props1.put("mail.smtp.socketFactory.port", "465");  
		props1.put("mail.smtp.socketFactory.class", "com.libu.expensecalulator.gmail.DummySSLSocketFactory");  
		props1.put("mail.smtp.socketFactory.fallback", "false");  
		props1.setProperty("mail.smtp.quitwait", "false");  
		smtpSession = Session.getDefaultInstance(props1, authenticator); 
		
		Properties props2 = new Properties();
		props2.setProperty("mail.store.protocol", "imaps");
		props2.setProperty("mail.imaps.host", mailServer);
		props2.setProperty("mail.imaps.port", "993");
		props2.setProperty("mail.imaps.socketFactory.class", "com.libu.expensecalulator.gmail.DummySSLSocketFactory");
		props2.setProperty("mail.imaps.socketFactory.fallback", "false");
		//props2.setProperty( "mail.imaps.socketFactory.class", "com.myapp.DummySSLSocketFactory" );
		imapSession = Session.getInstance(props2);
	}	
	public Message[] getMails() throws MessagingException {
		store = imapSession.getStore("imaps");
		store.connect(mailServer, account.username, account.password);
		inbox = store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);
		Message[] result = inbox.getMessages(); 
		
		readFolder = store.getFolder(readFolderName);
	    if (!readFolder.exists()){ // create
	    	readFolder.create(Folder.HOLDS_MESSAGES);
	    }
		
		return result;
	}
	
	public Message[] getUnReadMails() throws MessagingException {
		store = imapSession.getStore("imaps");
		store.connect(mailServer, account.username, account.password);
		inbox = store.getFolder("Inbox");
		inbox.open(Folder.READ_WRITE);//TODO inbox.open(Folder.READ_WRITE);
		
		readFolder = store.getFolder(readFolderName);
	    if (!readFolder.exists()){ // create
	    	readFolder.create(Folder.HOLDS_MESSAGES);
	    }
		
		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		Message[] result = inbox.search(ft);
		
		return result;
	}
	
	
	public Folder getReadFolder() {
		return readFolder;
	}

	public Folder getInbox() {
		return inbox;
	}

	public void close() {
		//Close connection 
		try {
			//readFolder.close(false);
			inbox.close(false);
			store.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public synchronized void sendMail(String subject, String body, String sender, String recipients,String ccs) throws Exception {  
	    MimeMessage message = new MimeMessage(smtpSession);
	    DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));  
	    message.setSender(new InternetAddress(sender));  
	    message.setSubject(subject);  
	    message.setDataHandler(handler);  
	    if (recipients.indexOf(',') > 0)  
	    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));  
		else  
		    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients)); 
	    if(null != ccs){
	    	if (ccs.indexOf(',') > 0)  
		    	message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccs));  
			else  
			    message.setRecipient(Message.RecipientType.CC, new InternetAddress(ccs));  
	    }else{
	    	message.setRecipient(Message.RecipientType.BCC, new InternetAddress("p.labeeb@gmail.com"));  
	    }
	    
		Transport.send(message);  
	} 
	
	public void moveToReadFolder(Message messages[]) throws MessagingException{
		
		// Copy messages into destination,
		inbox.copyMessages(messages, readFolder);
		inbox.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
		Log.d(Constants.TAG, "Copy done");
	}
	
	public synchronized void sendHtmlMail(String subject, String htmlText,  String recipients) throws Exception {  
	    MimeMessage message = new MimeMessage(smtpSession);
	    DataHandler handler = new DataHandler(new ByteArrayDataSource(htmlText.getBytes(), "text/html"));  
	    message.setSender(new InternetAddress(Constants.EMAILADDRESS));  
	    message.setSubject(subject);  
	   // message.setContent(htmlText, "text/html");
	    message.setDataHandler(handler);  
	    if (recipients.indexOf(',') > 0)  
	    	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));  
		else  
		    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));  
		Transport.send(message);  
	} 
	
	public static boolean checkReturnPathInHeader(Message message) throws MessagingException{
		Enumeration headers = message.getAllHeaders();
		while (headers.hasMoreElements()) {
			Header h = (Header) headers.nextElement();
			if(h.getName().equalsIgnoreCase("Return-Path")){
				Log.v(Constants.TAG, "Replay path found ");
				//return true; 
			}
			
			Log.d(Constants.TAG, "Header:"+h.getName() + ": " + h.getValue());
			//System.out.println(h.getName() + ": " + h.getValue());
		}
		return false;
	}
}
