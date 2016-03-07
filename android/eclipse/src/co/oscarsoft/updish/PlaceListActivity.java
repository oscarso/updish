package co.oscarsoft.updish;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
//import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

class LocationStatus {
	public static boolean gpsEnabled;
	public static boolean wifiEnabled;
	public static boolean pasEnabled;
}

public class PlaceListActivity extends Activity implements LocationListener {
	LocationManager locationManager = null;
	Location location = null;
	protected String url;

	private Button mBtnPlaceAdd;
	private ListView mListView;
	private List<Place> mItemsList;
	private PlaceListAdapter mListAdapter;
	private Timer mTimer;
	private double mLatitude;
	private double mLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);

		initData();
		initLocationManager();
		initView();
		initButton();
		initSubscriber();
	}

	private void initLocationManager() {
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationStatus.gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		LocationStatus.pasEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
		LocationStatus.wifiEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!LocationStatus.wifiEnabled &&
			!LocationStatus.gpsEnabled) {
			showDialog();
		 }

		if (LocationStatus.wifiEnabled) {
			if (DebugInfo.isDebug) {
				Toast.makeText(getApplicationContext(), "WiFi Enabled", Toast.LENGTH_SHORT).show();
				Log.d(DebugInfo.TAG, "");
			}
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);
		}
		if (LocationStatus.pasEnabled) {
			Log.d(DebugInfo.TAG, "");
			if (DebugInfo.isDebug) {
				Toast.makeText(getApplicationContext(), "Passive Network Enabled", Toast.LENGTH_SHORT).show();
				Log.d(DebugInfo.TAG, "");
			}
			locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 1, this);
		}
		if (LocationStatus.gpsEnabled) {
			Log.d(DebugInfo.TAG, "");
			if (DebugInfo.isDebug) {
				Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_SHORT).show();
				Log.d(DebugInfo.TAG, "");
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
		}
	}
	
	private void showDialog() {
		Toast.makeText(getApplicationContext(), R.string.msg_locmgr, Toast.LENGTH_SHORT).show();
	}

	private void initView() {
		mBtnPlaceAdd = (Button)findViewById(R.id.btnPlaceAdd);
		((TextView)findViewById(R.id.tvwMsgPlace)).setText(R.string.msg_place_list);
		mListView = (ListView)findViewById(R.id.lvwPlace);
	}
	
	private void initData() {
		mListView = null;
		mItemsList = new ArrayList<Place>();
		mListAdapter = null;
		mTimer = null;
		mLatitude = 0;
		mLongitude = 0;
	}
	
	private void initButton() {
		mBtnPlaceAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(PlaceListActivity.this, PlaceAddActivity.class);
				i.putExtra(Place.LATITUDE, getLocation().getLatitude());
				i.putExtra(Place.LONGITUDE, getLocation().getLongitude());
				startActivity(i);
			}
		});
	}

	private void initSubscriber() {
		// subscriber (mListView)
		mListAdapter = new PlaceListAdapter(this, R.layout.activity_place_list, mItemsList);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Place p = (Place)parent.getItemAtPosition(position);
				Intent i = new Intent(PlaceListActivity.this, ProductListActivity.class);
				i.putExtra(Place.ID, p.getId());
                startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	public Location getLocation() {
		return this.location;
	}

	@Override
	public void onLocationChanged(final Location location) {
		final long ONE_SECOND = 1000;
		final long ONE_MINUTE = 60 * ONE_SECOND;
		final long FIVE_MINS = 5 * ONE_MINUTE;
		final Handler handler = new Handler();
		
		this.location = location;
		mTimer = new Timer();
		TimerTask timerTask = new TimerTask() {
			int cnt = 1;

			public void run() {
				handler.post(new Runnable() {
					public void run() {
						JSONObject jObj = null;
						Place p = null;
						double mLatitudeNew = location.getLatitude();
						double mLongitudeNew = location.getLongitude();

						if ((mLatitude != mLatitudeNew)
								&&
							(mLongitude != mLongitudeNew)) {
							mLatitude = mLatitudeNew;
							mLongitude = mLongitudeNew;
							p = new Place("", "", "", mLatitudeNew, mLongitudeNew);
							try {
								jObj = p.toJSON_GetPlace();
								jObj.put(SecInfo.SECTOKEN, SecInfo.toJSON(PlaceListActivity.this).toString());
							} catch (JSONException je) {
								Log.d(DebugInfo.TAG, "");
								je.printStackTrace();
							}
							new AsyncPostJSONWebData(PlaceListActivity.this).execute(
									Server.ACTION_GET_PLACE,
									jObj.toString());
						}

						cnt++;
					}/* of Runnable run */
				}/* of Runnable */);
	         }/* of TimerTask run */};/* of TimerTask */
	         mTimer.schedule(timerTask, 0, FIVE_MINS);
	} // of onLocationChanged

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	public void notifyData(JSONObject jsonObjUser, JSONArray jsonArr) {
		if (mTimer != null) {
			mTimer.cancel();
		}

		/*
		 * This is a hack!
		 */
		if (mItemsList.size() > 0)
			return;

		try {
			User user = new User(jsonObjUser);
			UserDB udb = UserDB.getInstance(PlaceListActivity.this);
			udb.setUser(user);

			for (int i=0; i < jsonArr.length(); i++) {
				Place p = new Place((JSONObject)jsonArr.get(i));
				Log.d(DebugInfo.TAG, "Place("+i+"): " + p.getId());
				mItemsList.add(p);
			}
		} catch (Exception e) {
			Log.d(DebugInfo.TAG, "");
			e.printStackTrace();
		}
		// notify observer (mListAdapter)
		mListAdapter.notifyDataSetChanged();
	}
}
