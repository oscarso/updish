package co.oscarsoft.updish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncPostJSONWebData extends AsyncTask<String, Void, String> {
	/* HTTP info */
	private HttpClient mHttpClient;
	private Context mCtx;

	/* Default Constructor */
	public AsyncPostJSONWebData(final Context ctx) {
		mHttpClient = new DefaultHttpClient();
		mCtx = ctx;
	}

	@Override
	protected String doInBackground(String... params) {
		HttpPost req = null;
		String action = params[0];
		String data = params[1];
		String url = null;
		String encodedData = null;
		String decodedData = null;
		StringBuilder sb = null;
		StringEntity strEntity = null;

		if ((action == null || action.length() == 0) ||
			(data == null || data.length() == 0)) {
			Log.d(DebugInfo.TAG, "Bad Arguments");
			return "";
		}

		encodedData = Server.base64_encode(data);
		url = Server.url_encode(action);

		Log.d(DebugInfo.TAG, "HTTP POST url: " + url);
		Log.d(DebugInfo.TAG, "encodedData: " + encodedData);

		req = new HttpPost(url);
		try {
			strEntity = new StringEntity(data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// Set HTTP parameters
		req.setEntity(strEntity);
		req.setHeader("Accept", "application/json");
		req.setHeader("Content-type", "application/json");
		
		try {
			((HttpPost)req).setEntity(new StringEntity(data));
			HttpResponse resp = mHttpClient.execute(req);
			HttpEntity httpEntity = resp.getEntity();
            InputStream is = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
			
		 } catch (ClientProtocolException e) {
			 Log.d(DebugInfo.TAG, "");
			 e.printStackTrace();
			 return e.toString();
		 } catch (IOException e) {
			 Log.d(DebugInfo.TAG, "");
			 e.printStackTrace();
			 return e.toString();
		 } catch (Exception e) {
			 Log.d(DebugInfo.TAG, "");
			 e.printStackTrace();
			 return e.toString();
		 }
		Log.d(DebugInfo.TAG, "doInBackground: " + sb.toString());
		return sb.toString();
	}
	
	@Override
    protected void onPostExecute(String _resp) {
		JSONArray jsonArr;
		try {
			// PlaceListActivity
			if (mCtx instanceof PlaceListActivity) {
				JSONObject jsonObj = new JSONObject(_resp);
				JSONObject jsonObjUser = jsonObj.getJSONObject("user");
				jsonArr = jsonObj.getJSONArray("places");
				((PlaceListActivity)mCtx).notifyData(jsonObjUser, jsonArr);
			}

			// ProductListActivity
			if (mCtx instanceof ProductListActivity) {
				jsonArr = new JSONArray(_resp);
				((ProductListActivity)mCtx).notifyData(jsonArr);
			}
			
			// PlaceAddActivity
			if (mCtx instanceof PlaceAddActivity) {
				jsonArr = new JSONArray(_resp);
				((PlaceAddActivity)mCtx).notifyData(jsonArr);
			}
			
			//ProductAddActivity
			if (mCtx instanceof ProductAddActivity2) {
				jsonArr = new JSONArray(_resp);
				((ProductAddActivity2)mCtx).notifyData(jsonArr);
			}
       } catch (JSONException e) {
    	   Log.d(DebugInfo.TAG, "");
    	   e.printStackTrace();
       }
    }
}
