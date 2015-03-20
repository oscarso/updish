package co.oscarsoft.updish;

import java.util.ArrayList;
import java.util.Hashtable;

public class ItemType {
	static final Hashtable<String, String> ht;
	static {
		ht = new Hashtable<String, String>();
		ht.put("Appetizer", "Appetizer");
		ht.put("Side", "Side");
		ht.put("Soup", "Soup");
		ht.put("Salad", "Salad");
		ht.put("Entree", "Entree");
		ht.put("Dessert", "Dessert");
		ht.put("Beverage", "Beverage");
	}
	
	static ArrayList<String> list;
	static {
		list = new ArrayList<String>(ht.values());
	}
}
/*
Appetizer
Side
Soup
Salad
Entree
Dessert
Beverage
*/