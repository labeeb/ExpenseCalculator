package com.libu.expensecalulator.prasenter;

import android.content.Context;

import com.libu.expensecalulator.db.User;

public interface SignUpPresenter {
	public void doSignUp(Context context,User user) throws Exception;

}
