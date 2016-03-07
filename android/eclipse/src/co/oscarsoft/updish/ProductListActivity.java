package co.oscarsoft.updish;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListActivity extends Activity
{
	private Button mBtnProductAdd;
	private ListView mListView;
	private List<Product> mItemsList;
	private ProductListAdapter mListAdapter;
	private String mPlaceID;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);

		initParam();
		initView();
		initButton();
		initData();
		initSubscriber();
		httpGetProductList();
	}

	private void initParam() {
		// get param from previous activity
		Intent i = getIntent();
		mPlaceID = i.getStringExtra(Place.ID);
	}

	private void initView() {
		mBtnProductAdd = (Button)findViewById(R.id.btnProductAdd);
		((TextView)findViewById(R.id.tvwMsgProduct)).setText(R.string.msg_product_list);
		mListView = (ListView)findViewById(R.id.lvwProduct);
	}
	
	private void initButton() {
		mBtnProductAdd.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ProductListActivity.this, ProductAddActivity1.class);
				i.putExtra(Place.ID, mPlaceID);
				startActivity(i);
			}
		});
	}

	private void initData() {
		mItemsList = new ArrayList<Product>();
	}

	private void initSubscriber() {
		// subscriber (mListView)
		mListAdapter = new ProductListAdapter(this, R.layout.activity_product_list, mItemsList);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Product p = (Product)parent.getItemAtPosition(position);
				updateItemsToList(p);
				Toast.makeText(ProductListActivity.this, p.getUName_en(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void updateItemsToList(Product p) {
		boolean newVoteStatus = !p.getVoted();
		long	numOfVote = p.getNumOfVote();

		Log.d(DebugInfo.TAG, "ProductID: " + p.getId() + "  UName:" + p.getUName_en());
		httpPostVote(p.getId());

		p.setVoted(newVoteStatus);
		p.setNumOfVote(newVoteStatus ? ++numOfVote : --numOfVote);
		mListAdapter.notifyDataSetChanged();
	}

	private JSONObject toJSON_GetVote(
			String placeID
			) throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(SecInfo.SECTOKEN, SecInfo.toJSON(ProductListActivity.this).toString());
		jObj.put(Place.ID, placeID);
		
		return jObj;
	}

	private JSONObject toJSON_SetVote(
			String placeID,
			String productID
			) throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(SecInfo.SECTOKEN, SecInfo.toJSON(ProductListActivity.this).toString());
		jObj.put(Place.ID, placeID);
		jObj.put(Product.ID, productID);

		return jObj;
	}

	private void httpGetProductList() {
		JSONObject jObj = null;
		try {
			jObj = toJSON_GetVote(mPlaceID);
		}
		catch (JSONException je) {
			je.printStackTrace();
		}
		new AsyncPostJSONWebData(ProductListActivity.this).execute(
				Server.ACTION_GET_VOTE,
				jObj.toString());
	}

	private void httpPostVote(String productID) {
		JSONObject jObj = null;
		try {
			jObj = toJSON_SetVote(mPlaceID, productID);
		}
		catch (JSONException je) {
			je.printStackTrace();
		}
		new AsyncPostJSONWebData(ProductListActivity.this).execute(
				Server.ACTION_SET_VOTE,
				jObj.toString());
	}

	public void notifyData(JSONArray jsonArr) {
		/*
		 * This is a hack!
		 */
		if (mItemsList.size() > 0)
			return;

		try {
			for (int i=0; i < jsonArr.length(); i++) {
				Product p = new Product((JSONObject)jsonArr.get(i));
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
