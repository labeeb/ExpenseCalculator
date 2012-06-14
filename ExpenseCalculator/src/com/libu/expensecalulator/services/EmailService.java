package com.libu.expensecalulator.services;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public interface EmailService {
	public void sentEmail(String subject,String body,String recipients)throws Exception;
	public Message[] getEmail()throws MessagingException;
	public Folder getReadFolder();
}
