package com.libu.expensecalulator.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "expense.db";
	private static final int DATABASE_VERSION =13;
	private Dao<User, Integer> userDao = null;
		
	private Dao<Expense, Integer> expenseDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, Expense.class);
			insertInitialValuesToDb();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void insertInitialValuesToDb() throws SQLException {
		/*Dao<User, Integer> dao = getUserDao();
		User user = new User();
		user.setEmailAddress("p.labeeb@gmail.com");
		user.setName("Labeeb");
		user.setRent(1400);
		dao.create(user);
		
		user = new User();
		user.setEmailAddress("labeebp@rapidvaluesolutions.com");
		user.setName("rvsLabeeb");
		user.setRent(1000);
		dao.create(user);*/
		
		
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, User.class, true);
			TableUtils.dropTable(connectionSource, Expense.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Dao<User, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(User.class);
		}
		return userDao;
	}
	
	public Dao<Expense, Integer> getExpenseDao() throws SQLException {
		if (expenseDao == null) {
			expenseDao = getDao(Expense.class);
		}
		return expenseDao;
	}
	
	@Override
	public void close() {
		super.close();
		expenseDao = null;
		userDao = null;
	}
}
