package com.androidbook.search.nosearch;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

//filename: NoSearchActivity.java
public class ActionBarSearchActivity extends Activity 
{
	private static final String tag = "ActionBarSearchActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar_activity);
        //this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_GLOBAL);
        return;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
 	   	MenuInflater inflater = getMenuInflater(); 
 	   	inflater.inflate(R.menu.action_bar_menu, menu);
        setupSearchView(menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
		onSearchRequested();
		return true;
    	/*if (item.getItemId() == R.id.mid_search) {
    		onSearchRequested();
    		return true;
    	}
    	return super.onOptionsItemSelected(item);*/
    }
    
    private void setupSearchView(Menu menu)
    {
    	SearchView searchView = 
    		(SearchView) menu.findItem(R.id.mid_search).getActionView();
    	if (searchView == null) {
    		Log.d(tag, "Failed to get search view");
    		return;
    	}
    	//setup searchview
    	SearchManager searchManager = 
    		(SearchManager) getSystemService(Context.SEARCH_SERVICE);
    	ComponentName cn = 
    		new ComponentName(this,SearchActivity.class);
    	SearchableInfo info = 
    		searchManager.getSearchableInfo(cn);
    	if (info == null) {
    		Log.d(tag, "Failed to get search info");
    		return;
    	}

    	searchView.setSearchableInfo(info);

		// Do not iconify the widget; expand it by default
		searchView.setIconifiedByDefault(false); 
    }//eof-function    
}//eof-class
