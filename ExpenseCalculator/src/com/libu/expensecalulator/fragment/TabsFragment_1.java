package com.libu.expensecalulator.fragment;

import com.libu.expensecalulator.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class TabsFragment_1 extends Fragment  {

	private static final String TAG = "FragmentTabs";
	public static final String TAB_WORDS = "words";
	public static final String TAB_NUMBERS = "numbers";

	private View mRoot;
	private TabHost mTabHost;
	private int mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.activity_user, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
       	 
            @Override
            public void onTabChanged(String tabId) {
                android.support.v4.app.FragmentManager fm =   getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                if(tabId.equalsIgnoreCase("One")){
                	 ft.replace(R.id.realtabcontent,new CustomFragment("One ONE"));
                }else {
                	 ft.replace(R.id.realtabcontent,new CustomFragment("TWO TWO"));
                }
                ft.commit();
                
                
                //fm.popBackstack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        };
        
        mTabHost.setOnTabChangedListener(tabChangeListener);
        
        setTabHost("One");
        setTabHost("Two");
	}

	public void setTabHost(String name){
	   	 /*TextView textView = new TextView(this);
	        textView.setGravity(Gravity.CENTER);
	        textView.setText(name);*/
	        
	   	TabHost.TabSpec tabSpec = mTabHost.newTabSpec(name);
	   	//tabSpec.setIndicator(textView);
	       tabSpec.setIndicator(name,getResources().getDrawable(R.drawable.ic_launcher));
	       tabSpec.setContent(new TabContent(getActivity()));
	       mTabHost.addTab(tabSpec);
	   }
	

}
