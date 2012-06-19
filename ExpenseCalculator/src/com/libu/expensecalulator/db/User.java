package com.libu.expensecalulator.db;

import java.sql.SQLException;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;

public class User {
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField
	String name;
	
	@DatabaseField(unique=true)
	String emailAddress;
	
	@DatabaseField
	float rent;
	
	float totalSpent;
	
	/*@ForeignCollectionField(eager = false)
    Collection<Expense> allExpenses;*/
	
	public User() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public float getTotalSpent() {
		return totalSpent;
	}

	public void setTotalSpent(float totalSpent) {
		this.totalSpent = totalSpent;
	}

	public float getRent() {
		return rent;
	}

	public void setRent(float rent) {
		this.rent = rent;
	}

	/*public Collection<Expense> getAllExpenses() {
		return allExpenses;
	}

	public void setAllExpenses(Collection<Expense> allExpenses) {
		this.allExpenses = allExpenses;
	}
	*/
	public static User getUser(Context context,String emailAddress) throws SQLException{
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<User, Integer> dao = databaseHelper.getUserDao();
		QueryBuilder<User, Integer> userQueryBuilder = dao.queryBuilder();
		userQueryBuilder.where().eq("emailAddress", emailAddress);
		User user = userQueryBuilder.queryForFirst();
		return user;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public int saveObject(Context context) throws SQLException{
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<User, Integer> dao = databaseHelper.getUserDao();
		return dao.create(this);
	}
	
	public int update(Context context) throws SQLException{
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		Dao<User, Integer> dao = databaseHelper.getUserDao();
		return dao.update(this);
	}

	
}
