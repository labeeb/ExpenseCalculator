package com.libu.expensecalulator.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

public class Expense {
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField
	String discription;
	
	@DatabaseField
	long addedDate;
	
	@DatabaseField
	long eventDate;
	
	
	@DatabaseField
	float amount;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh=true)
    User spentByUser;
	
	public Expense(){}

	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public User getSpentByUser() {
		return spentByUser;
	}

	public void setSpentByUser(User spentByUser) {
		this.spentByUser = spentByUser;
	};
	
	public int saveObject(Context context) throws SQLException{
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<Expense, Integer> dao = databaseHelper.getExpenseDao();
		return dao.create(this);
	}

	public long getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(long addedDate) {
		this.addedDate = addedDate;
	}

	public long getEventDate() {
		return eventDate;
	}

	public void setEventDate(long eventDate) {
		this.eventDate = eventDate;
	}

	public static List<Expense> getAllExpense(Context context) throws SQLException {
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<Expense, Integer> dao = databaseHelper.getExpenseDao();
		return dao.queryForAll();
		
	}
	
	@Override
	public String toString() {
		Date dateObject = new Date(addedDate);
		
		return spentByUser.getName() +" spent "+ amount+" on "+dateObject.toString();
	}
	
}
