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

public class ProductAddActivity2 extends Activity {

	private final static int RESULT_TAKE_PHOTO = 0;
	private final static int RESULT_GET_IMAGE = 1;

	private String mPlaceID;
	private String mProductName;
	private String mItemTypes;
	private String mCountryCodes;
	private Bitmap mBmResized;

	private Button mBtnTakePhoto;
	private Button mBtnGetPhoto;
	private ImageView mIvwProductImage;	

	private Button mBtnProductAdd_Back;
	private Button mBtnProductAdd_Next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_add2);

		initParam();
		initView();
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
		mProductName = i.getStringExtra(Product.UNAME_EN);
		mItemTypes = i.getStringExtra(Product.ITEM_TYPES);
		mCountryCodes = i.getStringExtra(Product.COUNTRY_CODES);
	}

	private void initView() {
		mIvwProductImage = (ImageView)findViewById(R.id.ivwProductImage);
		mBtnTakePhoto = (Button)findViewById(R.id.btnTakePhoto);
		mBtnGetPhoto = (Button)findViewById(R.id.btnGetPhoto);

		mBtnProductAdd_Back = (Button)findViewById(R.id.btnProductAdd_Back);
		mBtnProductAdd_Next = (Button)findViewById(R.id.btnProductAdd_Next);
	}

	private void initButton() {
		mBtnTakePhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(i, RESULT_TAKE_PHOTO);
			}
		});

		mBtnGetPhoto.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); 
				startActivityForResult(i, RESULT_GET_IMAGE);
			}
		});
		
		mBtnProductAdd_Back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ProductAddActivity2.this.finish();
			}
		});

		mBtnProductAdd_Next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					addProduct();
			}
		});
	}

	private void addProduct() {
		JSONObject jObj = null;
		Product p = new Product(
				mProductName,
				mItemTypes,
				mCountryCodes,
				mBmResized);
		try {
			jObj = p.toJSON_SetProduct();
			jObj.put(Place.ID, mPlaceID);
			jObj.put(SecInfo.SECTOKEN, SecInfo.toJSON(ProductAddActivity2.this).toString());
		} catch (JSONException je) {
			Log.d(DebugInfo.TAG, "");
			je.printStackTrace();
		}
		new AsyncPostJSONWebData(ProductAddActivity2.this).execute(
				Server.ACTION_SET_PRODSRV,
				jObj.toString());
	}

	public void notifyData(JSONArray jsonArr) {
		Intent i = new Intent(ProductAddActivity2.this, ProductListActivity.class);
		i.putExtra(Place.ID, mPlaceID);
		startActivity(i);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		
		Bitmap bmLarge = null;

		switch (requestCode) {
		case ProductAddActivity2.RESULT_GET_IMAGE:
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			bmLarge = BitmapFactory.decodeFile(picturePath);
			mBmResized = Bitmap.createScaledBitmap(bmLarge, 200, 200, true);
			mIvwProductImage.setImageBitmap(mBmResized);
		break;
		
		case ProductAddActivity2.RESULT_TAKE_PHOTO:
	        Bundle extras = data.getExtras();
	        bmLarge = (Bitmap) extras.get("data");
	        mBmResized = Bitmap.createScaledBitmap(bmLarge, 200, 200, true);
	        mIvwProductImage.setImageBitmap(mBmResized);
		break;
		
		default:
			break;
		} // of switch
	}
}
