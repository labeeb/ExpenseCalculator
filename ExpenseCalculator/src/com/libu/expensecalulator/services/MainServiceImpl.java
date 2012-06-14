package com.libu.expensecalulator.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.zip.DataFormatException;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

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
			User user = getUser(emailAddress);
			Expense expense = Utils.getExpenseObjectFromSubject(subject);
			expense.setDiscription(body);
			expense.setSpentByUser(user);
			expense.saveObject(context);
			expense.setAddedDate(new Date().getTime());
			expense.setType(Constants.ET_EXPENSE);
			Date date = new Date(expense.getEventDate());
			String result = user.getName()+" added "+expense.getAmount()+" on "+date.toLocaleString();
			Log.d(TAG, "result :"+result);
			return result;
		}else{
			throw new SubjectFormatException();
		}
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
	public User getUser(String emailAddress) throws SQLException {
		return User.getUser(context, emailAddress);
	}

	@Override
	public void processInbox() {
		//EmailService emailService = new EmailServiceImple();
		EmailManager emailManager = new EmailManager();
		Message messages[] = null;
		try {
			messages = emailManager.getUnReadMails();
			if (null != messages) {
				Log.d(TAG, "Messages length :" + messages.length);
				String body = null;
				String emailAddress = null;
				String subject = null;
				for (Message message : messages) {
					Address address[] = message.getFrom();
					message.getReplyTo();
					emailAddress = Utils.getAbsoluteAddress(address[0].toString());
					Log.d(TAG, "emailAddress :" + emailAddress);
					if(EmailManager.checkReturnPathInHeader(message)){
						//Replay to email 
						Log.d(TAG, "Replay to email So will break :");
						break;
					}else{
						User sentUser = getUser(emailAddress);
						if(null == sentUser){
							Log.d(TAG, "Senter not in db, So will break :");
							break;
						}else{
							subject = message.getSubject();
							Log.d(TAG, "subject :" + subject);
							
							body = "";
							Object content = message.getContent();
							if (content instanceof String) {
								body = (String) content;
							} else if (content instanceof Multipart) {
								// Make sure to cast to it's Multipart derivative
								body = Utils.parseMultipart((Multipart) content);
							}
						
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
							} 
							
							if (!message.isSet(Flags.Flag.SEEN) && !message.isSet(Flags.Flag.ANSWERED)) {
								message.setFlag(Flags.Flag.FLAGGED, true);
								//Here you will read the email
							}
							
							Log.d(TAG, "replay : " + replay);
							if(null != replay){
								emailManager.sendMail("RE:"+subject, replay, Constants.EMAILADDRESS, emailAddress);
							}
						}
					}
				}
				emailManager.moveToReadFolder(messages);
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
