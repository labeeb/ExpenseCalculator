package com.libu.expensecalulator.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;

import android.util.Log;

import com.libu.expensecalulator.db.Expense;
import com.libu.expensecalulator.exceptions.SubjectFormatException;

public class Utils {
	public static String getDetails(String body) {
		return null;
	}

	public static long getAmount(String subject) {
		return 0;
	}

	public static Date getDate(String subject) {
		return null;
	}

	public static Expense getExpenseObjectFromSubject(String subject) throws SubjectFormatException, DataFormatException{
		
		Pattern p = Pattern.compile(Constants.SUBJECT_REGEX);
		Matcher matcher = p.matcher(subject);
		Expense expense = null;
		if (matcher.matches()) {
		    expense = new Expense();
		    for(int i =0;i <= matcher.groupCount();i++){
		    	//System.out.println((i+1)+" groups:"+matcher.group(i));
		    	if(i==2){
		    		float amount = Float.parseFloat(matcher.group(i));
		    		expense.setAmount(amount);
		    	}else if(i==3){
		    		String dateString = matcher.group(i);
		    		if(validateDayMonth(dateString)){
		    			Calendar date = getDateFromString(dateString);
		    			int year = date.get(Calendar.YEAR);
		    			int month = date.get(Calendar.MONTH); 
		    			Calendar currentMonthDate = Utils.getCurrentMonth();
		    			int currentYear = currentMonthDate.get(Calendar.YEAR);
		    			int currentMonth = currentMonthDate.get(Calendar.MONTH); 
		    			
		    			if(year != currentYear || month != currentMonth){
		    				StringBuilder error= new StringBuilder(dateString);
		    				error.append(", Need to be in ");
		    				error.append(currentMonth+1).append("/").append(currentYear);
		    				throw new DataFormatException(error.toString());
		    			}else{
		    				expense.setEventDate(date.getTime().getTime());
		    			}
		    			
		    		}else{
		    			throw new DataFormatException(dateString);
		    		}
		    	}
		    }
		}else{
			throw new SubjectFormatException();
		}
		
		return expense;
	}
	
	public static Calendar getDateFromString(String dateString) {
		String RFC1123_PATTERN1 = "dd/MM/yyyy";
		DateFormat rfc1123Formate1 = new SimpleDateFormat(RFC1123_PATTERN1);
		Calendar cal = null;
		try {
			cal = Calendar.getInstance();
			cal.setTime(rfc1123Formate1.parse(dateString));
		} catch (ParseException e) {
			Log.e(Constants.TAG, "ParseException in getDateOnly");
		}
		return cal;
	}
	
	public static String getAbsoluteAddress(String email) {
		String absoluteAddress = email;
		if (email.contains("<")) {
			int frist = email.indexOf("<");
			int last = email.indexOf(">");
			absoluteAddress = email.substring(frist + 1, last);
		}
		return absoluteAddress;
	}

	public static boolean validateDayMonth(String strDate) {
		boolean isValid = false;

		String[] dateArray = strDate.split("/");

		int day = Integer.valueOf(dateArray[0]).intValue();
		int month = Integer.valueOf(dateArray[1]).intValue();
		int year = Integer.valueOf(dateArray[2]).intValue();

		if ((day > 0 && day <= 31) && (month > 0 && month <= 12)) {
			/*
			* should be correct for most cases but still will not be correct in fringe cases like 
			* feb having 30 days or april having 31 days.
			*/

			isValid = true;
			try {
				GregorianCalendar cal = new GregorianCalendar();

				/* 
				*  setLenient to false to force calendar to throw 
				* IllegalArgumentException in case 
				*  any field, day, month or year is not valid (invalid year would be '00')
				*/
				cal.setLenient(false);
				// month - 1 is done because Calander uses 0-11 for months
				cal.set(year, (month - 1), day);
				/* 
				* add is called just to invoke the method 
				* Calendar.complete(). complete() is the method 
				*  that throws the IllegalArgumentException. 
				*  
				*  Note : Calendar.set() does not compute the date fields 
				* only methods like add(), 
				*  roll() or getTime() force the Calendar object to calculate 
				* field values
				*/
				// done only to force Calendar to compute all fields
				cal.add(Calendar.SECOND, 1);
			} catch (IllegalArgumentException iae) {
				System.out.println("DATE ERROR:" + iae.getMessage());
				isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * Parse the Multipart to find the body
	 * 
	 * @param mPart
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static String parseMultipart(Multipart mPart) throws MessagingException, IOException {
		// Loop through all of the BodyPart's
		for (int i = 0; i < mPart.getCount(); i++) {
			// Grab the body part
			BodyPart bp = mPart.getBodyPart(i);
			// Grab the disposition for attachments
			String disposition = bp.getDisposition();

			// It's not an attachment
			if (disposition == null && bp instanceof MimeBodyPart) {
				MimeBodyPart mbp = (MimeBodyPart) bp;

				// Time to grab and edit the body
				if (mbp.isMimeType("text/plain")) {
					// Grab the body containing the text version
					String body = (String) mbp.getContent();
					return body;
					/* // Add our custom message
					 body += stripTags( mesgStr );

					 // Reset the content
					 mbp.setContent( body, "text/plain" );*/
				} else if (mbp.isMimeType("text/html")) {
					// Grab the body containing the HTML version
					String body = (String) mbp.getContent();
					return body;
					/*// Add our custom message to the HTML before
					// the closing </body>
					body = addStrToHtmlBody( mesgStr, body );

					// Reset the content
					mbp.setContent( body, "text/html" );*/
				}
			}
		}
		return null;
	}
	
	
	public static String getMonthName(int month) {
	    DateFormatSymbols symbols = new DateFormatSymbols();
	    String[] monthNames = symbols.getMonths();
	    return monthNames[month];
	}
	
	public static Calendar getCurrentMonth(){
		Calendar currentDateCol = Calendar.getInstance();
		currentDateCol.set(Calendar.DAY_OF_MONTH,1);
		currentDateCol.set(Calendar.HOUR,0);
		currentDateCol.set(Calendar.MINUTE,0);
		currentDateCol.set(Calendar.SECOND,0);
		currentDateCol.set(Calendar.MILLISECOND,0);
		return currentDateCol;
	}

	public static String getDateInFormat(long dateLong){
		String RFC1123_PATTERN1 = "dd/MM/yyyy";
		DateFormat rfc1123Formate1 = new SimpleDateFormat(RFC1123_PATTERN1);
		Date date = new Date(dateLong);
		return rfc1123Formate1.format(date);
		
	}
}
