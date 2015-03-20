package co.oscarsoft.updish;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CountryListAdapter extends ArrayAdapter<String> {
	private LayoutInflater	inflater;

	public CountryListAdapter(Context ctx, int resourceId, List<String> objects) {
		super(ctx, resourceId, objects);
		inflater = LayoutInflater.from(ctx);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {		
		// Inflate the custom view
		convertView = inflater.inflate(R.layout.country_list_item, null);
		final String pt = (String)getItem(position);
		((TextView)convertView.findViewById(R.id.tvwCountry)).setText(pt);

		return convertView;
	}
}
