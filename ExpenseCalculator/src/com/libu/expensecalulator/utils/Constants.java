	package com.libu.expensecalulator.utils;

public class Constants {
	public static final String TAG= "expense";
	public static final String URLSERVER = "gmail.com";
	public static final String USERNAME = "labeeb4testing";
	public static final String PASSWORD = "labeeb4testing";
	public static final String EMAILADDRESS = "labeeb4testing@gmail.com";
	
	public static final String EXPENSE = "Expense";
	
	public static final String SUBJECT_REGEX = "("+EXPENSE+":([0-9]*\\.?[0-9]*)Date:(\\d{1,2}/\\d{1,2}/\\d{2,4}))";
	
	public static final int ET_EXPENSE = 0x01;
	public static final int ET_RENT = 0x02;
	public static final int ET_ADDED = 0x03;
	
}
