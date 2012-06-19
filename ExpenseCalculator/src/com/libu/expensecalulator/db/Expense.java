package com.libu.expensecalulator.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.libu.expensecalulator.utils.Utils;

public class Expense {
	@DatabaseField(generatedId = true)
	int id;
	public static final String EXPENSE_id = "id";
	
	
	@DatabaseField
	int type;
	public static final String EXPENSE_type= "type";
	
	@DatabaseField
	String discription;
	public static final String EXPENSE_ID = "id";
	
	@DatabaseField
	long addedDate;
	public static final String EXPENSE_addedDate = "addedDate";
	
	@DatabaseField
	long eventDate;
	public static final String EXPENSE_eventDate = "eventDate";
	
	
	@DatabaseField
	float amount;
	public static final String EXPENSE_amount = "amount";
	
	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh=true)
    User spentByUser;
	
	public static final String EXPENSE_spentByUser = "spentByUser";
	
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
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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
	
	public static List<Expense> getExpensesForThisMonth(Context context,Date currentMonth) throws SQLException{
	
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<Expense, Integer> dao = databaseHelper.getExpenseDao();
		QueryBuilder<Expense, Integer> userQueryBuilder = dao.queryBuilder();
		userQueryBuilder.where().ge(EXPENSE_eventDate, currentMonth.getTime());
		//userQueryBuilder.orderBy(EXPENSE_eventDate, false);
		return userQueryBuilder.query();
	}
	
	@Override
	public String toString() {
		return spentByUser.getName() +" spent "+ amount+" on "+Utils.getDateInFormat(eventDate);
	}
	
	public int update(Context context) throws SQLException{
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<Expense, Integer> dao = databaseHelper.getExpenseDao();
		return dao.update(this);
	}
}
