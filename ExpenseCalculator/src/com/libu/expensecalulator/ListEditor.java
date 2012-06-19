package com.libu.expensecalulator;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.libu.expensecalulator.db.Expense;
import com.libu.expensecalulator.utils.Utils;

public class ListEditor extends Activity {
	List<Expense> expenses;
	ListView listView;
	ArrayAdapter<Expense> adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		listView = (ListView) findViewById(R.id.editorListView);
		Button buttonListFull = (Button)findViewById(R.id.buttonListFull);
		Button buttonLisForMonth = (Button)findViewById(R.id.buttonLisForMonth);
		
		buttonListFull.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					expenses = Expense.getAllExpense(getApplicationContext());
					adapter = new ArrayAdapter<Expense>(ListEditor.this,
							android.R.layout.simple_list_item_1, expenses);
					listView.setAdapter(adapter);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		buttonLisForMonth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					expenses = Expense.getExpensesForThisMonth(ListEditor.this, Utils.getCurrentMonth().getTime());
					adapter = new ArrayAdapter<Expense>(ListEditor.this,
							android.R.layout.simple_list_item_1, expenses);
					listView.setAdapter(adapter);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		try {
			expenses = Expense.getExpensesForThisMonth(ListEditor.this, Utils.getCurrentMonth().getTime());
			//expenses = Expense.getAllExpense(getApplicationContext());
			adapter = new ArrayAdapter<Expense>(this,
					android.R.layout.simple_list_item_1, expenses);
			listView.setAdapter(adapter);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				final Expense expense= expenses.get(position);
				final Dialog dialog = new Dialog(ListEditor.this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
				dialog.setContentView(R.layout.editor);
				
				final EditText editTextEditiorAmount = (EditText)dialog.findViewById(R.id.editText_editior_Amount);
				final EditText editTextEditorDate = (EditText)dialog.findViewById(R.id.editText_editor_date);
				final EditText editTextEditorDiscription = (EditText)dialog.findViewById(R.id.editText_editor_discription);;
				
				Button buttonEditorSave = (Button)dialog.findViewById(R.id.button_editor_save);
				Button buttonEditorCancel = (Button)dialog.findViewById(R.id.button_editor_cancel);
				
				buttonEditorCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				
				buttonEditorSave.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String amount = editTextEditiorAmount.getText().toString();
						String date = editTextEditorDate.getText().toString();
						String discription = editTextEditorDiscription.getText().toString();
						new SaveExpense(expense).execute(amount,date,discription);
					}
				});
				
				editTextEditiorAmount.setText(String.valueOf(expense.getAmount()));
				editTextEditorDate.setText(Utils.getDateInFormat(expense.getEventDate()));
				editTextEditorDiscription.setText(String.valueOf(expense.getDiscription()));
				
				dialog.show();
				
				//TODO dialog with input field 
			}});
		
	}
	
	class SaveExpense extends AsyncTask<String, Void, String>{
		private ProgressDialog dialog ;
		private Expense expense;
		private final String OK= "ok";
		
		public SaveExpense(Expense expense) {
			this.expense = expense;
		}
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ListEditor.this);
			dialog.show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... arg0) {
			
			
			try {
				float amount = Float.parseFloat(arg0[0].trim());
				Calendar date = Utils.getDateFromString(arg0[1].trim());
				String discription = arg0[2].trim();
				expense.setAmount(amount);
				expense.setEventDate(date.getTime().getTime());
				expense.setDiscription(discription);
				expense.update(ListEditor.this);
				
				return OK;
			} catch (SQLException e) {
				e.printStackTrace();
				return e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			dialog.cancel();
			Toast.makeText(ListEditor.this,"Result:"+result, Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
			if(result.equals(OK)){
				ListEditor.this.onBackPressed();
			}
		}
	}
}
