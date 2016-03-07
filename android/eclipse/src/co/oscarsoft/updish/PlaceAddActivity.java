package co.oscarsoft.updish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlaceAddActivity extends Activity {

	private EditText mEdtPlaceName;
	private ListView mLvwPlaceType;
	
	private Button mBtnPlaceAdd_Back;
	private Button mBtnPlaceAdd_Next;

	private String mPlaceID;
	private String mPlaceName;
	private String mPlaceType;
	private double mLatitude;
	private double mLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_add);
		
		initParam();
		initView();
		initSubscriber();
		initButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void initParam() {
		// get param from previous activity
		Intent i = getIntent();
		mLatitude = i.getDoubleExtra(Place.LATITUDE, 0);
		mLongitude = i.getDoubleExtra(Place.LONGITUDE, 0);
	}

	private void initView() {
		mEdtPlaceName = (EditText)findViewById(R.id.edtPlaceName);
		mLvwPlaceType = (ListView)findViewById(R.id.lvwPlaceCat);
		//mLvwPlaceCountry = (ListView)findViewById(R.id.lvwPlaceCountry);
		
		mBtnPlaceAdd_Back = (Button)findViewById(R.id.btnPlaceAdd_Back);
		mBtnPlaceAdd_Next = (Button)findViewById(R.id.btnPlaceAdd_Next);
	}

	private void initSubscriber() {
		// mLvwPlaceType
		final PlaceTypeListAdapter ptListAdapter =
				new PlaceTypeListAdapter(this, R.layout.activity_place_add, PlaceType.list);
		mLvwPlaceType.setAdapter(ptListAdapter);
		mLvwPlaceType.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mPlaceType = (String)parent.getItemAtPosition(position);
				Log.d(DebugInfo.TAG, "PlaceType: " + mPlaceType);
			}
		});

		/*
		final CountryListAdapter cListAdapter =
				new CountryListAdapter(this, R.layout.activity_place_add, Country.list);
		mLvwPlaceCountry.setAdapter(cListAdapter);
		mLvwPlaceCountry.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String c = (String)parent.getItemAtPosition(position);
				mPlaceCountry = Country.lu.get(c);
				if (DebugInfo.isDebug) System.out.println(DebugInfo.PREFIX + "CountryCode: " + mPlaceCountry);
			}
		});
		*/
	}

	private void initButton() {
		mBtnPlaceAdd_Back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PlaceAddActivity.this.finish();
			}
		});

		mBtnPlaceAdd_Next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPlaceName = mEdtPlaceName.getText().toString();
				if (mPlaceName.length() > 0) {
					addPlace();
				} else {
					Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void addPlace() {
		JSONObject jObj = null;
		Place p = new Place(
				mPlaceName,
				mPlaceType,
				"",
				mLatitude,
				mLongitude);
		try {
			jObj = p.toJSON_SetPlace();
			jObj.put(SecInfo.SECTOKEN, SecInfo.toJSON(PlaceAddActivity.this).toString());
		} catch (JSONException je) {
			Log.d(DebugInfo.TAG, "");
			je.printStackTrace();
		}
		new AsyncPostJSONWebData(PlaceAddActivity.this).execute(
				Server.ACTION_SET_PLACE,
				jObj.toString());
	}

	public void notifyData(JSONArray jsonArr) {
		try {
			JSONObject jsonObj = (JSONObject)jsonArr.get(0);
			mPlaceID = jsonObj.getString(Place.ID);
			Log.d(DebugInfo.TAG, " PlaceID: " + mPlaceID);
		} catch (Exception e) {
			Log.d(DebugInfo.TAG, "");
			e.printStackTrace();
		}

		Intent i = new Intent(PlaceAddActivity.this, ProductAddActivity1.class);
		i.putExtra(Place.ID, mPlaceID);
		startActivity(i);
	}
}
