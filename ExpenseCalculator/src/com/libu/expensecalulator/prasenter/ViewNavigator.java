package com.libu.expensecalulator.prasenter;

import com.libu.expensecalulator.db.User;

import android.content.Context;

public interface ViewNavigator {
	public void goToSignUpPage(Context context);
	public void goToLoginPage(Context context);
	public void goToUserHomePage(Context context,User user);
}
