package com.libu.expensecalulator.prasenter;

import com.libu.expensecalulator.db.User;

public interface LoginView extends BaseView {
	public void goToSignUpPage();
	public void goToUserPage(User user);

}
