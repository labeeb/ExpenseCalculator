package com.libu.expensecalulator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.libu.expensecalulator.services.MainService;
import com.libu.expensecalulator.services.MainServiceImpl;

public class ExpenseCalculatorActivity extends Activity {
	public static final String TAG = "ExpenseCalculatorActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
		Button buttonCheckEmail = (Button)findViewById(R.id.buttonCheckEmail);
		Button buttonListExpense = (Button)findViewById(R.id.buttonListExpense);
		
		buttonSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*EmailManager emailManager = new EmailManager();
				String body = "<H1>Hello</H1>" +
						 "<img src=\"www.trevorromain.com/.../father&son2.jpg\">";
				try {
					emailManager.sendHtmlMail("HTml1", body, "p.labeeb@gmail.com");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				startActivity(new Intent(ExpenseCalculatorActivity.this, SendMessageActivity.class));

			}
		});
		
		buttonCheckEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ProccessEmail().execute();

			}
		});
		
		buttonListExpense.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(ExpenseCalculatorActivity.this, ListEditor.class));

			}
		});

		/*ListView listView = (ListView) findViewById(R.id.listViewEmails);
		ArrayAdapter<Expense> adapter;
		try {
			adapter = new ArrayAdapter<Expense>(this,
					android.R.layout.simple_list_item_1, Expense.getAllExpense(this));
			listView.setAdapter(adapter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	class ProccessEmail extends AsyncTask<Void, Void, Void>{
		ProgressDialog dialog ;
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ExpenseCalculatorActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			MainService mainService = new MainServiceImpl(ExpenseCalculatorActivity.this);
			mainService.processInbox();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.cancel();
			super.onPostExecute(result);
		}
	}
	
	
}