package co.oscarsoft.updish;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class SecInfo {
	private static UserDB mUserDB = null;
	public static final String SECTOKEN = "SECTOKEN";

	public static JSONObject toJSON(Context ctx) throws JSONException {
		JSONObject jObj = new JSONObject();

		Log.d(DebugInfo.TAG, "SecInfo:");

		if (SecInfo.mUserDB == null) {
			SecInfo.mUserDB = UserDB.getInstance(ctx);
		}

		User user = SecInfo.mUserDB.getUser();
		if (user == null) {
			Log.d(DebugInfo.TAG, "SecInfo: user == null");
			jObj.put(User.ID, "");
			jObj.put(User.LEVEL, 0);
		} else {
			Log.d(DebugInfo.TAG, "SecInfo: user != null");
			jObj.put(User.ID, user.getId());
			jObj.put(User.LEVEL, user.getLevel());
			Log.d(DebugInfo.TAG, "SecInfo:    ID:" + user.getId());
			Log.d(DebugInfo.TAG, "SecInfo:  level:" + user.getLevel());
		}

		return jObj;
	}
}
