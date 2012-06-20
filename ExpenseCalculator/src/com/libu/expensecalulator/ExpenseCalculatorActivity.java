package com.libu.expensecalulator;

import java.sql.SQLException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.libu.expensecalulator.services.MainService;
import com.libu.expensecalulator.services.MainServiceImpl;
import com.libu.expensecalulator.utils.Utils;

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
		Button buttonAddUser = (Button)findViewById(R.id.buttonAddUser);
		Button buttonGenerateAndSend = (Button)findViewById(R.id.buttonGenerateAndSend);
		Button buttonGenerateAndDisplay = (Button)findViewById(R.id.buttonGenerateAndDisplay);
		Button buttonAddExpense = (Button) findViewById(R.id.buttonAddExpense);
		
		buttonAddExpense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ExpenseCalculatorActivity.this, AddExpenseActivity.class));				
			}
		});
		
		buttonGenerateAndDisplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					MainServiceImpl mainService = new MainServiceImpl(ExpenseCalculatorActivity.this);
					String html  = mainService.caluculateRent(Utils.getCurrentMonth());
					String mime = "text/html";
					String encoding = "utf-8";
					Dialog dialog = new Dialog(ExpenseCalculatorActivity.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
					WebView webView = new WebView(ExpenseCalculatorActivity.this);
					
					webView.loadDataWithBaseURL(null, html, mime, encoding, null);
					dialog.setContentView(webView);
					dialog.show();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		buttonGenerateAndSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GenerateReport().execute();
			}
		});
		
		buttonAddUser.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ExpenseCalculatorActivity.this, AddUserActivity.class));
				
			}
		});
		
		
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
	
	
	class GenerateReport extends AsyncTask<Void, Void, Void>{
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
			try {
				mainService.sendReportForThisMonth();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dialog.cancel();
			super.onPostExecute(result);
		}
	}
	
}