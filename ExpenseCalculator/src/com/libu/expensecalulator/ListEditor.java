package com.libu.expensecalulator;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.libu.expensecalulator.db.Expense;

public class ListEditor extends Activity {
	List<Expense> expenses;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		ListView listView = (ListView) findViewById(R.id.editorListView);
		ArrayAdapter<Expense> adapter;
		try {
			expenses = Expense.getAllExpense(this);
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
				Expense expense= expenses.get(position);
				Dialog dialog = new Dialog(ListEditor.this);
				dialog.setContentView(R.layout.editor);
				
				EditText editText_editior_Amount = (EditText)dialog.findViewById(R.id.editText_editior_Amount);
				EditText editText_editor_date = (EditText)dialog.findViewById(R.id.editText_editor_discription);
				EditText editText_editor_discription = (EditText)dialog.findViewById(R.id.editText_editor_discription);;
				
				editText_editior_Amount.setText(String.valueOf(expense.getAmount()));
				editText_editor_date.setText(String.valueOf(expense.getEventDate()));
				editText_editor_discription.setText(String.valueOf(expense.getDiscription()));
				
				dialog.show();
				
				//TODO dialog with input field 
			}});
		
	}
}
