package com.libu.expensecalulator.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.zip.DataFormatException;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;

import android.content.Context;
import android.util.Log;

import com.libu.expensecalulator.db.Expense;
import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.exceptions.SubjectFormatException;
import com.libu.expensecalulator.gmail.EmailManager;
import com.libu.expensecalulator.utils.Constants;
import com.libu.expensecalulator.utils.Utils;

public class MainServiceImpl implements MainService {
	private static String TAG = "ExpenseServiceImpl";
	private Context context;
	
	

	public MainServiceImpl(Context context) {
		this.context = context;
	}

	@Override
	public String processEmail(String emailAddress,String subject, String body) throws SQLException, SubjectFormatException, DataFormatException {
		emailAddress= Utils.getAbsoluteAddress(emailAddress);
		Log.d(TAG, "emailAddress :="+emailAddress +" and subject :="+subject );
		Log.d(TAG, "body = "+body);
		
		if(subject.startsWith(Constants.EXPENSE)){
			//For expense 
			
				User user = User.getUser(context, emailAddress);
				Expense expense = Utils.getExpenseObjectFromSubject(subject);
				expense.setDiscription(body);
				expense.setSpentByUser(user);
				expense.saveObject(context);
				String result = user.getName()+" added "+expense.getEventDate()+" on "+expense.getEventDate();
				Log.d(TAG, "result :"+result);
				return result;
			
			
		}else{
			throw new SubjectFormatException();
		}
		
		
		/*User user = getUser(emailAddress);
		long amount = Utils.getAmount(subject);
		Date date = Utils.getDate(subject);
		String details = Utils.getDetails(body);
		
		EmailManager emailManager = new EmailManager();
		try {
			emailManager.sendMail(subject, body,Constants.EMAILADDRESS, emailAddress);
			//Log.d(TAG, "Emails:"+emailManager.getMails());
			Log.d(TAG, "sending done ");
		} catch (Exception e) {
			Log.e(TAG, "error in sending ");
			e.printStackTrace();
		}*/
		//addExpense(user, amount, date, details);
	}

	@Override
	public void addExpense(User user,long amount,Date date,String details) {
		// TODO Auto-generated method stub

	}

	@Override
	public void caluculateRent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void generateReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNotes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getUserDetails() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getMonthlyReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUser(String emailAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processInbox() {
		//EmailService emailService = new EmailServiceImple();
		EmailManager emailManager = new EmailManager();
		Message messages[] = null;
		try {
			messages = emailManager.getMails();//getUnReadMails();
			if (null != messages) {
				Log.d(TAG, "Messages length :" + messages.length);
				String body = null;
				//String emailAddress = null;
				//String subject = null;
				for (Message message : messages) {
					String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.CC));
					Log.v(TAG,"ToTOTOT: " + to);
					/*if(EmailManager.checkReturnPathInHeader(message)){
						//Replay to email 
						Log.d(TAG, "Replay to email  :");
						break;
					}else{
						Log.d(TAG, "Not replay to email  :");
						body = "";
						Object content = message.getContent();
						if (content instanceof String) {
							body = (String) content;
						} else if (content instanceof Multipart) {
							// Make sure to cast to it's Multipart derivative
							body = Utils.parseMultipart((Multipart) content);
						}
					}*/
						////////////////////////////
					String from = InternetAddress.toString(message.getFrom());
					if (from != null) {
						Log.v(TAG,"From: " + from);
					}

					String replyTo = InternetAddress.toString(message.getReplyTo());
					if (replyTo != null) {
						Log.v(TAG,"Reply-to: " + replyTo);
					}
					String to2 = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
					if (to != null) {
						Log.v(TAG,"To: " + to2);
					}

					String subject = message.getSubject();
					if (subject != null) {
						Log.v(TAG,"Subject: " + subject);
					}
					Date sent = message.getSentDate();
					if (sent != null) {
						Log.v(TAG,"Sent: " + sent);
					}

					Log.v(TAG,"Message : ");

					Multipart multipart = (Multipart) message.getContent();

					for (int x = 0; x < multipart.getCount(); x++) {
						BodyPart bodyPart = multipart.getBodyPart(x);

						String disposition = bodyPart.getDisposition();

						if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
							Log.v(TAG,"Mail have some attachment : ");

							DataHandler handler = bodyPart.getDataHandler();
							Log.v(TAG,"file name : " + handler.getName());
						} else {
							Log.v(TAG,""+bodyPart.getContent());
						}
					}
					Log.d(TAG,"End!");
					//////////////////
					/*Address address[] = message.getFrom();
					 message.getReplyTo();
					emailAddress = address[0].toString();
					
					//TODO need to verify is it an address in our db 
					Log.d(TAG, "emailAddress :" + emailAddress);
					
					subject = message.getSubject();
					
					Log.d(TAG, "subject :" + subject);
					String replay = null;
					try{
						replay = processEmail(emailAddress, subject, body);
						
					} catch (SubjectFormatException e) {
						Log.e(TAG, "SubjectFormatException = "+e.getLocalizedMessage());
						replay= "Please reformat the subject and sent again";
						e.printStackTrace();
					} catch (DataFormatException e) {
						replay = "Given date not incorrect!:"+e.getMessage();
						Log.e(TAG, "DataFormatException = "+e.getMessage());
						e.printStackTrace();
					} */
					/*Log.d(TAG, "replay : " + replay);
					if(null != replay){
						emailManager.sendMail(subject, body, Constants.EMAILADDRESS, emailAddress);
					}*/
					// message.getAllHeaders().
					
					
					//Flags flags = message.getFlags();
					Flags flags = message.getFlags();
					Flags.Flag[] sf = flags.getSystemFlags();
					
					//message.setFlag(Flags.Flag.FLAGGED, true);
					
					
					if (!message.isSet(Flags.Flag.SEEN) && !message.isSet(Flags.Flag.ANSWERED)) {
						message.setFlag(Flags.Flag.FLAGGED, true);
						//Here you will read all the email content which are unread
					}
					
					
				}
				
				Folder dfolder =emailManager.getReadFolder();
				Folder inbox = emailManager.getInbox();
				
				// Copy messages into destination,
				inbox.copyMessages(messages, dfolder);
				inbox.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
				Log.d(TAG, "Copy done");
			} else {
				Log.e(TAG, "Messages is null ");
			}
			emailManager.close();
		} catch (MessagingException e) {
			Log.e(TAG, "MessagingException in processInbox = "+e.getMessage());
			e.printStackTrace();
		} /*catch (IOException e) {
			Log.e(TAG, "IOException in processInbox = "+e.getMessage());
			e.printStackTrace();
		} */catch (Exception e) {
			Log.e(TAG, "Excepiton in processInbox = "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	

}
