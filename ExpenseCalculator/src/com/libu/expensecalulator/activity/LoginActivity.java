package com.libu.expensecalulator.activity;

import java.sql.SQLException;
import java.util.List;

import com.libu.expensecalulator.R;
import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.prasenter.LoginPresenter;
import com.libu.expensecalulator.prasenter.LoginView;
import com.libu.expensecalulator.prasenter.PresenterFactory;
import com.libu.expensecalulator.prasenter.ViewNavigator;
import com.libu.expensecalulator.services.MainServiceImpl;
import com.libu.expensecalulator.utils.Logger;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener ,LoginView {
	Logger logger = new Logger(LoginActivity.class.getSimpleName());
	
	LoginPresenter loginPresenter;
	ViewNavigator navigator;
	
	EditText userName;
	EditText password;
	Button signUp;
	Button login;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        loginPresenter = PresenterFactory.getLoginPresenter(this,this); 
        navigator = PresenterFactory.getViewNavigator();
        
        goToUserPage(null);
        
        userName = (EditText)findViewById(R.id.login_editTextUserName);
        password = (EditText)findViewById(R.id.login_editTextPassword);
        signUp = (Button)findViewById(R.id.login_buttonSignUp);
        login = (Button)findViewById(R.id.login_buttonLogin);
        
        userName.setOnClickListener(this);
        password.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
        
        //TODO remove this, just toTest
        try {
			List<User> allUsers = new MainServiceImpl(this).getAllUsers();
			if(null != allUsers){
				logger.d("USER","User count = "+allUsers.size());
				for(User user:allUsers){
					logger.d("USER","User is  = "+user.getEmailAddress()+" and "+user.getPassword());
				}
			}else
				logger.d("USER","allUsers is null");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       
        
    }

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_buttonLogin:
			loginPresenter.doLogin(userName.getText().toString(), password.getText().toString());
			break;
		case R.id.login_buttonSignUp:
			loginPresenter.signUp();
			break;
		default:
			logger.e("not expected!, default in onClick");
			break;
		}
	}



	@Override
	public void goToSignUpPage() {
		navigator.goToSignUpPage(this);
	}

	@Override
	public void displayAlert(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void goToUserPage(User user) {
		navigator.goToUserHomePage(this, user);
	}
}
