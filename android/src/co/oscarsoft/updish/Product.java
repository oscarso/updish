package co.oscarsoft.updish;

import java.io.ByteArrayOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.util.Base64;

public class Product {
	// Constructor
	public Product(
			String uname_en,
			String itemType,
			String ccs,
			Bitmap bmImage) {
		super();
		this.id = null;
		this.uname_en = uname_en;
		this.itemType = itemType;
		this.countryCodes = ccs;
		this.iname_en = null;
		this.imagePath = null;
		this.bmImage = bmImage;
		this.b64eImage = null;
		this.numOfVote = 0;
		this.voted = false;
	}

	// Constructor
	public Product(
			String id,
			String uname_en,
			String iname_en,
			String imagePath,
			Bitmap bmImage,
			String b64eImage,
			long numOfVote,
			boolean voted) {
		super();
		this.id = id;
		this.uname_en = uname_en;
		this.iname_en = iname_en;
		this.itemType = null;
		this.countryCodes = null;
		this.imagePath = imagePath;
		this.bmImage = bmImage;
		this.b64eImage = b64eImage;
		this.numOfVote = numOfVote;
		this.voted = voted;
	}

	// Constructor
	public Product(JSONObject jsonObj) throws JSONException {
		super();
		this.id = jsonObj.getString(Product.ID);
		this.uname_en = jsonObj.getString(Product.UNAME_EN);
		this.iname_en = jsonObj.getString(Product.INAME_EN);
		this.imagePath = jsonObj.getString(Product.IMAGE_PATH);
		this.bmImage = null;
		this.b64eImage = null;
		this.numOfVote = jsonObj.getLong(Product.VOTE);
		this.voted = (jsonObj.getDouble(Product.USER_VOTED) > 0) ? true : false;
	}
	
	private String id;
	private String uname_en;
	private String iname_en;
	private String itemType;
	private String countryCodes; // i.e. us, cn|in, fr|vn
	private String imagePath;
	private Bitmap bmImage;
	private String b64eImage;
	private long numOfVote;
	private boolean voted;

	public static final String ID = "prod_id";
	public static final String UNAME_EN = "uname_en";
	public static final String INAME_EN = "iname_en";
	public static final String ITEM_TYPES = "item_types";
	public static final String COUNTRY_CODES = "ccs";
	public static final String IMAGE_PATH = "photo_path";
	public static final String IMAGE_FILE = "photo_file";
	public static final String VOTE = "vote";
	public static final String USER_VOTED = "user_voted";


	public JSONObject toJSON_SetProduct() throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(Product.UNAME_EN, this.uname_en);
		jObj.put(Product.ITEM_TYPES, this.itemType);
		jObj.put(Product.COUNTRY_CODES, this.countryCodes);
		this.b64eImage = Bitmap2B64PNG(this.bmImage);
		jObj.put(Product.IMAGE_FILE, this.b64eImage);
		
		return jObj;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject jObj = new JSONObject();

		jObj.put(Product.ID, this.id);
		jObj.put(Product.UNAME_EN, this.uname_en);
		jObj.put(Product.INAME_EN, this.iname_en);
		jObj.put(Product.IMAGE_PATH, this.imagePath);

		if (this.bmImage != null) {
			this.b64eImage = Bitmap2B64PNG(this.bmImage);
			jObj.put(Product.IMAGE_FILE, this.b64eImage);
		}

		jObj.put(Product.VOTE, this.numOfVote);
		jObj.put(Product.USER_VOTED, this.voted);

		return jObj;
	}
	
	private String Bitmap2B64PNG(Bitmap bmFile) {
		ByteArrayOutputStream babs = new ByteArrayOutputStream();
		bmFile.compress(Bitmap.CompressFormat.PNG, 100, babs);
		byte[] b = babs.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}

	public String getId() {
		return this.id;
	}
	
	public String getUName_en() {
		return this.uname_en;
	}

	public String getIName_en() {
		return this.iname_en;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}

	public long getNumOfVote() {
		return this.numOfVote;
	}
	
	public String getNumOfVoteMsg() {
		String msg;
		if (0 == this.numOfVote) {
			msg = "No vote";
		} else if (1 == this.numOfVote) {
			msg = "1 vote";
		} else {
			msg = "" + this.numOfVote + " votes";
		}
		return msg;
	}

	public boolean getVoted() {
		return this.voted;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setUName_en(String name) {
		this.uname_en = name;
	}

	public void setIName_en(String name) {
		this.iname_en = name;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	public void setNumOfVote(long numOfVote) {
		this.numOfVote = numOfVote;
	}

	public void setVoted(boolean b) {
		this.voted = b;
	}
}
