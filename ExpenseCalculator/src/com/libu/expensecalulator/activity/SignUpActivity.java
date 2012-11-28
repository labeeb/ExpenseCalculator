package com.libu.expensecalulator.activity;

import java.sql.SQLException;

import com.libu.expensecalulator.AddUserActivity;
import com.libu.expensecalulator.R;
import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.prasenter.PresenterFactory;
import com.libu.expensecalulator.prasenter.SignUpPresenter;
import com.libu.expensecalulator.prasenter.SignUpView;
import com.libu.expensecalulator.prasenter.ViewNavigator;
import com.libu.expensecalulator.services.MainServiceImpl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity implements SignUpView , OnClickListener {
	SignUpPresenter signUpPresenter;
	ViewNavigator navigator;
	
	EditText password1;
	Button cancel;
	Button signUp;
	EditText password2;
	EditText et_rent;
	EditText userName;
	EditText email;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        signUpPresenter = PresenterFactory.getSignUpPresenter();
        navigator = PresenterFactory.getViewNavigator();
        
        email = (EditText)findViewById(R.id.signUp_editTextEmail);
        password1 = (EditText)findViewById(R.id.signU_editTextPassword1);
        password2 = (EditText)findViewById(R.id.signUp_editTextPassword2);
        et_rent = (EditText)findViewById(R.id.signUp_editTextRent);
        userName = (EditText)findViewById(R.id.signUp_editTextUserName);
        cancel = (Button)findViewById(R.id.signUp_buttonCancel);
        signUp = (Button)findViewById(R.id.signUp_buttonSignUp);
        
        cancel.setOnClickListener(this);
        signUp.setOnClickListener(this);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sign_up, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUp_buttonSignUp:
			//TODO need to check empty
			//TODO validate email
			//TODO set rule for password
			String pass1 = password1.getText().toString();
			String pass2 = password2.getText().toString();
			if(pass1.equals(pass2)){
				new AddUser().execute();
			}else{
				displayAlert(getString(R.string.passworddoesnotMatch));
			}
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayAlert(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	
	class AddUser extends AsyncTask<Void, Void, String>{
		ProgressDialog dialog ;
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(SignUpActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... arg0) {
			String emailAddress = email.getText().toString().trim();
			String name = userName.getText().toString();
			String rent = et_rent.getText().toString();
			String password = password1.getText().toString();
			
			try {
				User user = new User();
				user.setEmailAddress(emailAddress);
				user.setName(name);
				user.setRent(Float.parseFloat(rent));
				user.setPassword(password);
				signUpPresenter.doSignUp(SignUpActivity.this,user);
				return "ok";
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.cancel();
			displayAlert("Result:"+result);
			navigator.goToLoginPage(SignUpActivity.this);
			super.onPostExecute(result);
		}
	}
}
