package co.oscarsoft.updish;

import java.util.ArrayList;
import java.util.Hashtable;

public class PlaceType {
	static final Hashtable<String, String> ht;
	static {
		ht = new Hashtable<String, String>();
		ht.put("cafe", "cafe");
		ht.put("restaurant", "restaurant");
	}
	
	static ArrayList<String> list;
	static {
		list = new ArrayList<String>(ht.values());
	}
}
