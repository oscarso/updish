package co.oscarsoft.updish;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

public class Place {
	// Constructor
	public Place(
			String name,
			String types,
			String ccs,
			double latitude,
			double longitude
			) {
		super();
		this.id = "";
		this.name = name;
		this.types = types;
		this.countryCodes = ccs;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distMile = Place.R_MILE_DEBUG;
		this.imagePath = "";
		this.imageFile = null;
	}

	// Constructor
	public Place(
			String id,
			String name,
			String types,
			String ccs,
			double latitude,
			double longitude,
			double distMile,
			String imagePath,
			Bitmap imageFile
			) {
		super();
		this.id = id;
		this.name = name;
		this.types = types;
		this.countryCodes = ccs;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distMile = distMile;
		this.imagePath = imagePath;
		this.imageFile = null;
	}

	// Constructor
	public Place(JSONObject jsonObj) throws JSONException {
		super();
		this.id = jsonObj.getString(Place.ID);
		this.name = jsonObj.getString(Place.NAME);
		this.types = "";
		this.countryCodes = "";
		this.latitude = 0;
		this.longitude = 0;
		this.distMile = jsonObj.getDouble(Place.R_MILE);
		this.imagePath = "";//jsonObj.getString(Place.IMAGE_PATH);
		this.imageFile = null;
	}
	
	private String id;
	private String name;
	private String types; // i.e. restaurant|cafe, etc
	private String countryCodes; // i.e. us, cn|in, fr|vn
	private double latitude;
	private double longitude;
	private double distMile;
	private String imagePath;
	private Bitmap imageFile;

	public static final String ID = "gplace_id";
	public static final String NAME = "name";
	public static final String TYPES = "types";
	public static final String COUNTRY_CODES = "ccs";
	public static final String LATITUDE = "lat";
	public static final String LONGITUDE = "lng";
	public static final String R_MILE = "dist";
	public static final String IMAGE_PATH = "photo_path";
	public static final String IMAGE_FILE = "photo_file";
	
	public static final double R_MILE_DEBUG = 2;
	public static final double R_MILE_PRODUCTION = 0.3;


	public JSONObject toJSON_GetPlace() throws JSONException {
		JSONObject jObj = new JSONObject();
		
		jObj.put(Place.LATITUDE, this.latitude);
		jObj.put(Place.LONGITUDE, this.longitude);
		jObj.put(Place.R_MILE, this.distMile);
		
		return jObj;
	}

	public JSONObject toJSON_SetPlace() throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(Place.NAME, this.name);
		jObj.put(Place.TYPES, this.types);
		jObj.put(Place.COUNTRY_CODES, this.countryCodes);
		jObj.put(Place.LATITUDE, this.latitude);
		jObj.put(Place.LONGITUDE, this.longitude);
		
		return jObj;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(Place.ID, this.id);
		jObj.put(Place.NAME, this.name);
		jObj.put(Place.TYPES, this.types);
		jObj.put(Place.COUNTRY_CODES, this.countryCodes);
		jObj.put(Place.LATITUDE, this.latitude);
		jObj.put(Place.LONGITUDE, this.longitude);
		jObj.put(Place.R_MILE, this.distMile);
		jObj.put(Place.IMAGE_PATH, this.imagePath);
		jObj.put(Place.IMAGE_FILE, this.imageFile);

		return jObj;
	}

	public String getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}

	public double getDistance() {
		return this.distMile;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void setDistance(double dist) {
		this.distMile = dist;
	}
}
