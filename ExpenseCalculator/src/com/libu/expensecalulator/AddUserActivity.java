package com.libu.expensecalulator;

import java.sql.SQLException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.services.MainServiceImpl;

public class AddUserActivity extends Activity {
	EditText editTextAddUserEmailAddress;
	EditText editTextAddUserName;
	EditText editTextAddUserRent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_user);
		
		Button button = (Button)findViewById(R.id.buttonAddUserSave);
		editTextAddUserEmailAddress = (EditText)findViewById(R.id.editTextAddUserEmailAddress);
		editTextAddUserName = (EditText)findViewById(R.id.editTextAddUserName);
		editTextAddUserRent = (EditText)findViewById(R.id.editTextAddUserRent);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddUser().execute();
			}
		});
		
	}
	
	class AddUser extends AsyncTask<Void, Void, String>{
		ProgressDialog dialog ;
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AddUserActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... arg0) {
			String emailAddress = editTextAddUserEmailAddress.getText().toString();
			String name = editTextAddUserName.getText().toString();
			String rent = editTextAddUserRent.getText().toString();
			
			
			
			try {
				
				new MainServiceImpl(AddUserActivity.this).setWelcomeMail(emailAddress);
				User user = new User();
				user.setEmailAddress(emailAddress);
				user.setName(name);
				user.setRent(Float.parseFloat(rent));
				user.saveObject(AddUserActivity.this);
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
			Toast.makeText(AddUserActivity.this,"Result:"+result, Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}
}
