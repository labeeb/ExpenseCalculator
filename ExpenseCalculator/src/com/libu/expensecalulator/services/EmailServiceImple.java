package com.libu.expensecalulator.services;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import com.libu.expensecalulator.gmail.EmailManager;
import com.libu.expensecalulator.utils.Constants;

public class EmailServiceImple implements EmailService {
	EmailManager emailManager;
	
	public EmailServiceImple() {
		emailManager = new EmailManager();
	}

	@Override
	public void sentEmail(String subject,String body,String recipients) throws Exception {
		emailManager.sendMail(subject, body, Constants.EMAILADDRESS, recipients);
	}

	@Override
	public Message[] getEmail() throws MessagingException {
		return emailManager.getUnReadMails();
	}

	@Override
	public Folder getReadFolder() {
		return emailManager.getReadFolder();
	}

}
