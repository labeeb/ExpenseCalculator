package com.libu.expensecalulator.prasenter;

import java.sql.SQLException;

import android.content.Context;

import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.utils.Logger;

public class LoginPresenterImpl implements LoginPresenter {
	Logger logger = new Logger(LoginPresenterImpl.class.getSimpleName());
	
	private LoginView loginView;
	private Context context;
	
	public LoginPresenterImpl(LoginView loginView,Context context) {
		this.loginView = loginView;
		this.context = context;
	}

	@Override
	public User doLogin(String userName, String password) {
		User user = null;
		try {
			user = User.getUser(context, userName, password);
			if(null != user){
				loginView.goToUserPage(user);
			}else{
				loginView.displayAlert("Cannot find");
				logger.d("didn't find a user with email ="+userName+" password = "+password);
			}
		} catch (SQLException e) {
			//Logger.e(tag, message);
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void signUp() {
		loginView.goToSignUpPage();
	}
	
	
}
