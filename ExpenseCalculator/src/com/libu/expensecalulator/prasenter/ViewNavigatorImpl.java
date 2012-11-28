package com.libu.expensecalulator.prasenter;

import com.libu.expensecalulator.activity.LoginActivity;
import com.libu.expensecalulator.activity.SignUpActivity;
import com.libu.expensecalulator.activity.UserActivity;
import com.libu.expensecalulator.db.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ViewNavigatorImpl implements ViewNavigator {

	@Override
	public void goToSignUpPage(Context context) {
		Intent signUp = new Intent(context,SignUpActivity.class);
		context.startActivity(signUp);
		((Activity) context).finish();
	}

	@Override
	public void goToLoginPage(Context context) {
		Intent signUp = new Intent(context,LoginActivity.class);
		context.startActivity(signUp);
		//((Activity) context).finish();
	}

	@Override
	public void goToUserHomePage(Context context, User user) {
		Intent signUp = new Intent(context,UserActivity.class);
		context.startActivity(signUp);
		((Activity) context).finish();
		
	}

}
