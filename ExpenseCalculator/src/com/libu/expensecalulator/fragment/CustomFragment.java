package com.libu.expensecalulator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libu.expensecalulator.R;

public class CustomFragment extends Fragment  {
	String message; 
	
	public CustomFragment(String message) {
		this.message = message;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        /*TextView valueTV = new TextView(getActivity());
        valueTV.setText(message);
        valueTV.setId(5);
        
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT,Gravity.CENTER);
        
       // layoutParams.gravity = Gravity.CENTER;
        
        valueTV.setLayoutParams(layoutParams);*/
		
		View view = inflater.inflate(R.layout.one_view, null);
		TextView textView = (TextView)view.findViewById(R.id.text);
		textView.setText(message);
        return view;
    }
}
