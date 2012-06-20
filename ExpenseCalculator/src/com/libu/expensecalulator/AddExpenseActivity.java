package com.libu.expensecalulator;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.libu.expensecalulator.db.Expense;
import com.libu.expensecalulator.db.User;
import com.libu.expensecalulator.services.MainService;
import com.libu.expensecalulator.services.MainServiceImpl;

public class AddExpenseActivity extends Activity {
	private List<User> allUser = null;
	
	EditText editTextAddExpenseAmount;
	EditText editTextAddExpenseDiscription;
	Spinner spinnerUser;
	DatePicker datePicker;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_expense);
		
		spinnerUser = (Spinner) findViewById(R.id.spinnerUsers);
		//spin.setOnItemSelectedListener(this);
		MainService mainService = new MainServiceImpl(this);
		try {
			allUser = mainService.getAllUsers();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
			return;
		}
		ArrayAdapter<User> aa = new ArrayAdapter<User>(
				this,
				android.R.layout.simple_spinner_item, 
				allUser);

		aa.setDropDownViewResource(
		   android.R.layout.simple_spinner_dropdown_item);
		spinnerUser.setAdapter(aa);
		editTextAddExpenseAmount = (EditText)findViewById(R.id.editTextAddExpenseAmount);
		editTextAddExpenseDiscription = (EditText)findViewById(R.id.editTextAddExpenseDiscp);
		datePicker = (DatePicker)findViewById(R.id.datePickerAddUserDate);
		
		Button buttonSave = (Button)findViewById(R.id.buttonAddExpenseSave);
		
		buttonSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddExpense().execute();
			}
		});
	/*	Button button = (Button)findViewById(R.id.buttonAddUserSave);
		editTextAddUserEmailAddress = (EditText)findViewById(R.id.editTextAddUserEmailAddress);
		editTextAddUserName = (EditText)findViewById(R.id.editTextAddUserName);
		editTextAddUserRent = (EditText)findViewById(R.id.editTextAddUserRent);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AddUser().execute();
			}
		});
		*/
	}
	
	private class AddExpense extends AsyncTask<Void, Void, String>{
		private ProgressDialog dialog ;
		private final String OK = "ok"; 
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(AddExpenseActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... arg0) {
			String amount = editTextAddExpenseAmount.getText().toString();
			String discription = editTextAddExpenseDiscription.getText().toString();
			Calendar eventDate = Calendar.getInstance();
			eventDate.set(Calendar.MONTH, datePicker.getMonth());
			eventDate.set(Calendar.YEAR, datePicker.getYear());
			eventDate.set(Calendar.DATE, datePicker.getDayOfMonth());
			
			User user = (User) spinnerUser.getSelectedItem();
			try {
				Expense expense = new Expense(); 
				expense.setAmount(Float.parseFloat(amount));
				expense.setDiscription(discription);
				expense.setEventDate(eventDate.getTime().getTime());
				expense.setAddedDate(new Date().getTime());
				expense.setSpentByUser(user);
				expense.saveObject(AddExpenseActivity.this);
				return OK;
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
			Toast.makeText(AddExpenseActivity.this,"Result:"+result, Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
			if(OK.equals(result)){
				AddExpenseActivity.this.onBackPressed();
			}
		}
	}
}
