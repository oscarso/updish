package co.oscarsoft.updish;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductAddActivity1 extends Activity {

	private String mPlaceID;
	private String mProductName;
	private String mItemTypes;
	private String mCountryCodes;

	private EditText mEdtProductName;
	private ListView mLvwProductItemType;
	private ListView mLvwProductCountry;

	private Button mBtnProductAdd_Back;
	private Button mBtnProductAdd_Next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_add1);

		initParam();
		initView();
		initSubscriber();
		initButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void initParam() {
		// get param from previous activity
		Intent i = getIntent();
		mPlaceID = i.getStringExtra(Place.ID);
	}

	private void initView() {
		mEdtProductName = (EditText)findViewById(R.id.edtProductName);
		mLvwProductItemType = (ListView)findViewById(R.id.lvwProductItemType);
		mLvwProductCountry = (ListView)findViewById(R.id.lvwProductCountry);

		mBtnProductAdd_Back = (Button)findViewById(R.id.btnProductAdd_Back);
		mBtnProductAdd_Next = (Button)findViewById(R.id.btnProductAdd_Next);
	}

	private void initSubscriber() {
		// mLvwProductItemType
		final ItemTypeListAdapter itListAdapter =
				new ItemTypeListAdapter(this, R.layout.activity_product_add1, ItemType.list);
		mLvwProductItemType.setAdapter(itListAdapter);
		mLvwProductItemType.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mItemTypes = (String)parent.getItemAtPosition(position);
				Log.d(DebugInfo.TAG, "ItemType: " + mItemTypes);
			}
		});

		// mLvwProductCountry
		final CountryListAdapter cListAdapter =
				new CountryListAdapter(this, R.layout.activity_product_add1, Country.list);
		mLvwProductCountry.setAdapter(cListAdapter);
		mLvwProductCountry.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String c = (String)parent.getItemAtPosition(position);
				mCountryCodes = Country.lu.get(c);
				Log.d(DebugInfo.TAG, "CountryCode: " + mCountryCodes);
			}
		});
	}

	private void initButton() {		
		mBtnProductAdd_Back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ProductAddActivity1.this.finish();
			}
		});

		mBtnProductAdd_Next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mProductName = mEdtProductName.getText().toString();
				if (mProductName.length() > 0) {
					Intent i = new Intent(ProductAddActivity1.this, ProductAddActivity2.class);
					i.putExtra(Place.ID, mPlaceID);
					i.putExtra(Product.UNAME_EN, mProductName);
					i.putExtra(Product.ITEM_TYPES, mItemTypes);
					i.putExtra(Product.COUNTRY_CODES, mCountryCodes);
					startActivity(i);
				} else {
					Toast.makeText(getApplicationContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
