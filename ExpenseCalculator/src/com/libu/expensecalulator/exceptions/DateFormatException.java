package com.libu.expensecalulator.exceptions;

public class DateFormatException extends Exception {
	private String givenDateString;
	
	public DateFormatException(String givenDateString) {
		this.givenDateString =givenDateString;
	}
	
	@Override
	public String getMessage() {
		return givenDateString;
	}
}
