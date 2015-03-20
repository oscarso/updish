package com.androidbook.search.nosearch;

import com.androidbook.search.nosearch.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

//filename: NoSearchActivity.java
public class LocalSearchEnabledActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_search_enabled_activity);
        this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);
        onSearchRequested();
        return;
    }
    
    @Override
    public boolean onSearchRequested() {
        startSearch("coffee", true, null, false); 
        return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater(); 
 	   	inflater.inflate(R.menu.search_invoker_menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
		return false;
		/*if (item.getItemId() == R.id.mid_si_search)
    	{
    		onSearchRequested();
    		return true;
    	}
    	return super.onOptionsItemSelected(item);*/
    }
    
}
