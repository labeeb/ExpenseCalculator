package com.libu.expensecalulator.db;

import java.sql.SQLException;
import java.util.Collection;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.stmt.QueryBuilder;

public class User {
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField
	String name;
	
	@DatabaseField
	String emailAddress;
	
	@ForeignCollectionField(eager = false)
    Collection<Expense> allExpenses;
	
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

	public Collection<Expense> getAllExpenses() {
		return allExpenses;
	}

	public void setAllExpenses(Collection<Expense> allExpenses) {
		this.allExpenses = allExpenses;
	}
	
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

	
}
