package com.libu.expensecalulator.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;

import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.exceptions.SubjectFormatException;

public interface MainService {
	public void processInbox();
	
	public String processEmail(String emailAddress,String subject,String body) throws SQLException, SubjectFormatException, DataFormatException;
	
	public void addExpense(User user,long amount,Date date,String details);
	public String caluculateRent()throws Exception ;
	public void generateReport();
	public void addNotes();
	public void getUserDetails();
	public void getMonthlyReport();
	
	public User getUser(String emailAddress)throws SQLException;
	
	public List<User> getAllUsers()throws SQLException;
	
	public void setWelcomeMail(String recipients)throws Exception;

}
