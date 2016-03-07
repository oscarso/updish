package co.oscarsoft.updish;

import java.util.ArrayList;
import java.util.Hashtable;

public class Country {
	static final Hashtable<String, String> lu; //lookup
	static {
		lu = new Hashtable<String, String>();
		lu.put("Afghanistan", "af");
		lu.put("Argentina", "ar");
		lu.put("Australia", "au");
		lu.put("Brazil", "br");
		lu.put("China", "cn");
		lu.put("France", "fr");
		lu.put("Hong Kong", "hk");
		lu.put("India", "in");
		lu.put("United Kingdom", "gb");
		lu.put("United States", "us");
		//lu.put("dd", "dd");
		}

	static final Hashtable<String, String> ht;
	static {
		ht = new Hashtable<String, String>();
		ht.put("af", "Afghanistan");
		ht.put("ar", "Argentina");
		ht.put("au", "Australia");
		ht.put("br", "Brazil");
		ht.put("cn", "China");
		ht.put("fr", "France");
		ht.put("hk", "Hong Kong");
		ht.put("in", "India");
		ht.put("gb", "United Kingdom");
		ht.put("us", "United States");
		//ht.put("dd", "dd");
		}

	static ArrayList<String> list;
	static {
		list = new ArrayList<String>(ht.values());
	}
}
