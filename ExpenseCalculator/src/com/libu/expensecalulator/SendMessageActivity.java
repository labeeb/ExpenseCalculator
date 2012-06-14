package com.libu.expensecalulator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.libu.expensecalulator.services.MainService;
import com.libu.expensecalulator.services.MainServiceImpl;

public class SendMessageActivity extends Activity {
	
	EditText editTextSubject;
	EditText editTextBody;
	EditText editTextEmailAddress;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sentmessage);
        
        Button buttonSend = (Button)findViewById(R.id.buttonSend);
        editTextSubject = (EditText)findViewById(R.id.editTextSubject);
        editTextBody = (EditText)findViewById(R.id.editTextBody);
        editTextEmailAddress = (EditText)findViewById(R.id.editTextEmailAddress);
        buttonSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainService mainService = new MainServiceImpl(SendMessageActivity.this);
				String subject = editTextSubject.getText().toString();
				String body = editTextBody.getText().toString();
				String emailAddress = editTextEmailAddress.getText().toString();
				//mainService.processEmail(emailAddress,subject, body);
			}
		});
       
        //super.loadUrl("file:///android_asset/www/index.html");
    }
}
