package co.oscarsoft.updish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
//import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AsyncGetJSONWebData extends AsyncTask<String, Void, String> {
	/* HTTP info */
	private HttpClient mHttpClient;
	private Context mCtx;

	/* Default Constructor */
	public AsyncGetJSONWebData(final Context ctx) {
		mHttpClient = new DefaultHttpClient();
		mCtx = ctx;
	}

	@Override
	protected String doInBackground(String... params) {
		HttpGet req = null;
		String action = params[0];
		String data = params[1];
		String encodedURL = null;
		String encodedData = null;
		String decodedData = null;
		StringBuilder sb = null;

		if ((action == null || action.length() == 0) ||
			(data == null || data.length() == 0)) {
			Log.d(DebugInfo.TAG, "Bad Arguments");
			return "";
		}
		encodedData = Server.base64_encode(data);
		//decodedData = Server.base64_decode(encodedData);
		//if (DebugInfo.isDebug) System.out.println(DebugInfo.PREFIX + "encodedData: " + encodedData);
		//if (DebugInfo.isDebug) System.out.println(DebugInfo.PREFIX + "decodedData: " + data);

		encodedURL = Server.url_encode(action);
		//String good = "http://oscarsoft.co/ud/get_place.php?data=eyJsbmciOi0xMjIuMDQ4MjU1MTcsImxhdCI6MzcuMzA5ODg4NzQsImRpc3QiOjJ9%0A";
		//String good = "http://oscarsoft.co/ud/get_place.php?data=eyJsbmciOi0xMjIuMDQ4MzM5OCwibGF0IjozNy4zMDk4NTgxLCJkaXN0IjoyfQ%3D%3D%0A";
		Log.d(DebugInfo.TAG, "HTTP GET encodedURL: " + encodedURL);
		
		req = new HttpGet(encodedURL);
		try {
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
			jsonArr = new JSONArray(_resp);

			// GPSPlaceListActivity
			if (mCtx instanceof PlaceListActivity) {
				((PlaceListActivity)mCtx).notifyData(null, jsonArr);
			}

			// ProductListActivity
			if (mCtx instanceof ProductListActivity) {
				((ProductListActivity)mCtx).notifyData(jsonArr);
			}
       } catch (JSONException e) {
    	   Log.d(DebugInfo.TAG, "");
    	   e.printStackTrace();
       }
    }
}
