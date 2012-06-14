package com.libu.expensecalulator.gmail;

import com.libu.expensecalulator.utils.Constants;

public class EmailAccount {
	public String urlServer;
	public String username;
	public String password;
	public String emailAddress;
	public EmailAccount(String username, String password, String urlServer) {
		this.username = username;
		this.password = password;
		this.urlServer = urlServer;
		this.emailAddress = username + "@" + urlServer;
	}
	public EmailAccount() {
		urlServer = Constants.URLSERVER;
		username = Constants.USERNAME;
		password = Constants.PASSWORD;
		this.emailAddress = username + "@" + urlServer;
	}
}
