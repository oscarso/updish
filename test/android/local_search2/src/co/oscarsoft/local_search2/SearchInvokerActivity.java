package co.oscarsoft.local_search2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SearchInvokerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_invoker);
		this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_invoker, menu);
		this.onSearchRequested();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.onSearchRequested();
		return true;
	}
	
	@Override
	public boolean onSearchRequested() {
		this.startSearch("coffee", true, null, false);
		return true;
	}
}
