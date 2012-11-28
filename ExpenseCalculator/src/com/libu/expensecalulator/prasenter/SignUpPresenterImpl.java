package com.libu.expensecalulator.prasenter;

import android.content.Context;

import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.services.MainServiceImpl;

public class SignUpPresenterImpl implements SignUpPresenter {
	SignUpView signUpView;
	
	@Override
	public void doSignUp(Context context,User user) throws Exception {
		new MainServiceImpl(context).setWelcomeMail(user.getEmailAddress());
		user.saveObject(context);
	}

	
	
}
