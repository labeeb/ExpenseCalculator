package com.libu.expensecalulator.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TabHost;

import com.libu.expensecalulator.R;
import com.libu.expensecalulator.fragment.CustomFragment;
import com.libu.expensecalulator.fragment.TabContent;
import com.libu.expensecalulator.fragment.TabsFragment_1;
import com.libu.expensecalulator.fragment.TabsFragment_2;

public class UserActivity extends FragmentActivity {
	TabHost tHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        tHost = (TabHost) findViewById(android.R.id.tabhost);
        tHost.setup();
        
        TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
        	 
            @Override
            public void onTabChanged(String tabId) {
                android.support.v4.app.FragmentManager fm =   getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                if(tabId.equalsIgnoreCase("One")){
                	 ft.replace(R.id.realtabcontent,new CustomFragment("One"));
                }else {
                	 ft.replace(R.id.realtabcontent,new TabsFragment_2());
                }
                ft.commit();
                
                
                //fm.popBackstack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        };
 
        tHost.setOnTabChangedListener(tabChangeListener);
 
        setTabHost("One");
        setTabHost("Two");
        /*for(String tab:Constants.MAIN_CATEGORIES){
        	setTabHost(tab);
        }*/
    }
    
    public void setTabHost(String name){
   	 /*TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(name);*/
        
   	TabHost.TabSpec tabSpec = tHost.newTabSpec(name);
   	//tabSpec.setIndicator(textView);
       tabSpec.setIndicator(name,getResources().getDrawable(R.drawable.ic_launcher));
       tabSpec.setContent(new TabContent(getBaseContext()));
       tHost.addTab(tabSpec);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user, menu);
        return true;
    }
    
   
    /*private class CustomFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {

            TextView valueTV = new TextView(getActivity());
            valueTV.setText("hallo hallo");
            valueTV.setId(5);
            valueTV.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            return valueTV;
        }
    }*/
}
