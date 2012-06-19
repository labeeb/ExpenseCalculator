package com.libu.expensecalulator.services;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.libu.expensecalulator.db.DatabaseHelper;
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
			
			expense.setAddedDate(new Date().getTime());
			expense.setType(Constants.ET_EXPENSE);
			
			expense.saveObject(context);
			
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
	public String caluculateRent() throws Exception {
		Calendar currentMonth = Utils.getCurrentMonth();
		List<Expense> allExpenseInThisMonth = Expense.getExpensesForThisMonth(context,currentMonth.getTime());
		List<User> allUsers = getAllUsers();
		int noOfUser= allUsers.size();
		
		float totalExpenseInHome = 0; 
		for(Expense expense:allExpenseInThisMonth){
			totalExpenseInHome += expense.getAmount();
			for(User user:allUsers){
				if(user.getId() == expense.getSpentByUser().getId()){
					user.setTotalSpent(user.getTotalSpent()+expense.getAmount());
				}
			}
		}
		float forExpense=0;
		float totalRent = 0;
		float netRent = 0;
		StringBuilder recipients= new StringBuilder();
		StringBuilder reportHtml = new StringBuilder("<Body>");
		
		Log.d(TAG, currentMonth+" For month:"+Utils.getMonthName(currentMonth.get(Calendar.MONTH)));
		
		//reportHtml.append("<H3>").append("Report on ").append(Utils.getMonthName(currentMonth.getMonth())).append("</H3>");
		
		reportHtml.append("<table border=\"1\">");
		reportHtml.append("<tr>");
		reportHtml.append("<th>").append("Name").append("</th>");
		reportHtml.append("<th>").append("Rent").append("</th>");
		reportHtml.append("<th>").append("Spend").append("</th>");//TODO spell 
		reportHtml.append("<th>").append("Extra").append("</th>");
		reportHtml.append("<th>").append("Net Rent").append("</th>");
		reportHtml.append("</tr>");
		float userExtraSpent= 0;
		for(User user:allUsers){
			totalRent += user.getRent(); 
			userExtraSpent = user.getTotalSpent() - (user.getTotalSpent()/noOfUser);
			forExpense = user.getRent() + totalExpenseInHome/noOfUser-user.getTotalSpent() ;
			netRent += forExpense;
			reportHtml.append("<tr>");
			reportHtml.append("<td>").append(user.getName()).append("</td>");
			reportHtml.append("<td>").append(user.getRent()).append("</td>");
			reportHtml.append("<td>").append(user.getTotalSpent()).append("</td>");
			reportHtml.append("<td>").append(userExtraSpent).append("</td>");
			reportHtml.append("<td>").append(forExpense).append("</td>");
			reportHtml.append("</tr>");
			recipients.append(user.getEmailAddress()).append(",");
		}
		reportHtml.append("<tr>");
		reportHtml.append("<td>").append("Total").append("</td>");
		reportHtml.append("<td>").append(totalRent).append("</td>");
		reportHtml.append("<td>").append(totalExpenseInHome).append("</td>");
		reportHtml.append("<td>").append("</td>");
		reportHtml.append("<td>").append(netRent).append("</td>");
		reportHtml.append("</tr>");
		
		reportHtml.append("</table><br/><>");		
		
	 	List<Expense> allExpense = Expense.getExpensesForThisMonth(context, currentMonth.getTime());
	 	if(null != allExpense && allExpense.size() > 0){
	 		reportHtml.append("<table border=\"1\">");
			//reportHtml.append("<tr>");
			//reportHtml.append("<th>").append("Name").append("</th>");
			//reportHtml.append("</tr>");
			for(Expense expense:allExpense){
				reportHtml.append("<tr>");
				reportHtml.append("<td>").append(expense.getSpentByUser()).append("</td>");
				reportHtml.append("<td>").append(expense.getAmount()).append("</td>");
				reportHtml.append("<td>").append(Utils.getDateInFormat(expense.getEventDate())).append("</td>");
				reportHtml.append("<td>").append(expense.getDiscription()).append("</td>");
				reportHtml.append("</tr>");
			}
			reportHtml.append("</table>");
	 	}
		reportHtml.append("</Body>");
		
		EmailManager emailManager = new EmailManager();
		emailManager.sendHtmlMail("Report on "+Utils.getMonthName(currentMonth.get(Calendar.MONTH)), reportHtml.toString(), recipients.toString());
		
		return reportHtml.toString();
	}
	
	
	
	@Override
	public List<User> getAllUsers() throws SQLException{
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<User, Integer> dao = databaseHelper.getUserDao();
		return dao.queryForAll();
		
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
		boolean isExpenseAdded = false;
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
								isExpenseAdded = true;
							} catch (SubjectFormatException e) {
								Log.e(TAG, "SubjectFormatException = "+e.getLocalizedMessage());
								emailAddress = emailAddress+",p.labeeb@gmail.com";
								replay= "Please reformat the subject and sent again";
								e.printStackTrace();
							} catch (DataFormatException e) {
								replay = "Given date incorrect!:"+e.getMessage();
								emailAddress = emailAddress+",p.labeeb@gmail.com";
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
				if(isExpenseAdded){
					caluculateRent();
					Log.v(TAG, "isExpenseAdded  so sent message ");
				}else{
					Log.v(TAG, "isExpenseAdded  so sent message ");
				}
				
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

	@Override
	public void setWelcomeMail(String recipients) throws Exception {
		
		StringBuilder welcomeHtml = new StringBuilder("<Body>");
		welcomeHtml.append("<H2>Welcome to ……….</H2>");
		welcomeHtml.append("<H2>To add your expense </H2>");
		welcomeHtml.append("<span>Format your subject line like </span>");
		welcomeHtml.append("<b>Expense:&lt;amount&gt;Date:Dat&lt;dd/mm/yyyyy&gt;</b><br/>");
		welcomeHtml.append("<span >And sent it to <b>olivebarent12b@gmail.com</b></span>");
		welcomeHtml.append("<H4>Eg:</H4><span>Expense:80Date:15/06/2012</span>");
		welcomeHtml.append("<Body>");
		
		EmailManager emailManager = new EmailManager();
		emailManager.sendHtmlMail("Welcome", welcomeHtml.toString(), recipients);
	}

}
