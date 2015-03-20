package co.oscarsoft.updish;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

	// Constructor
	public User(String id, int level) {
		super();
		this.mID = id;
		this.mLevel = level;
	}

	// Constructor
	public User(JSONObject jsonObj) throws JSONException {
		super();
		this.mID = jsonObj.getString(User.ID);
		this.mLevel = jsonObj.getInt(User.LEVEL);
	}

	private String mID = "";
	private int mLevel = 0;

	public static final String ID = "uid";
	public static final String LEVEL = "level";

	public JSONObject toJSON() throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(User.ID, this.mID);
		jObj.put(User.LEVEL, this.mLevel);

		return jObj;
	}

	public String getId() {
		return this.mID;
	}

	public int getLevel() {
		return this.mLevel;
	}
	
	public void setId(String id) {
		this.mID = id;
	}

	public void setLevel(int level) {
		this.mLevel = level;
	}
}
