package com.libu.expensecalulator.prasenter;

import com.libu.expensecalulator.db.User;

public interface LoginPresenter {
	
	public User doLogin(String userName,String password);
	public void signUp();
}
