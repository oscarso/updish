package co.oscarsoft.updish;

import android.util.Base64;

public class Server {
	final static String PROTOCOL = "http";
	final static String HOST = "www.oscarsoft.co";
	final static String ACTION_SET_PLACE = "/ud/set_place.php";
	final static String ACTION_GET_PLACE = "/ud/get_place.php";
	final static String ACTION_SET_PRODSRV = "/ud/set_prodsrv.php";
	final static String ACTION_GET_VOTE = "/ud/get_vote.php";
	final static String ACTION_SET_VOTE = "/ud/set_vote.php";

	// base64 encode (no padding)
	public static String base64_encode(String data) {
		return (Base64.encodeToString(data.getBytes(), Base64.DEFAULT));
	}
	
	// base64 decode (no padding)
	public static String base64_decode(String encodedData) {
		byte[] dataArr;
		dataArr = Base64.decode(encodedData, Base64.DEFAULT);
		return (new String(dataArr));
	}
	
	// URL encode
	public static String url_encode(String action) {
		String encodedURL = null;
		encodedURL = Server.PROTOCOL +
					"://" + Server.HOST +
					action;
		return (encodedURL);
	}
}
