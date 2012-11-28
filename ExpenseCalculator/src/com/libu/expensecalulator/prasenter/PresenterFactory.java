package com.libu.expensecalulator.prasenter;

import android.content.Context;

public class PresenterFactory {
	public static LoginPresenter getLoginPresenter(LoginView loginView,Context context){
		return new LoginPresenterImpl(loginView,context);
	}
	
	public static ViewNavigator getViewNavigator(){
		return new ViewNavigatorImpl();
	}
	
	public static SignUpPresenter getSignUpPresenter(){
		return new SignUpPresenterImpl();
	}

}
