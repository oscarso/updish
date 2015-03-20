package co.oscarsoft.updish;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//import android.widget.ImageView;
import android.widget.TextView;

public class PlaceListAdapter extends ArrayAdapter<Place> {
	private LayoutInflater	inflater;

	public PlaceListAdapter(Context ctx, int resourceId, List<Place> objects) {
		super(ctx, resourceId, objects);
		inflater = LayoutInflater.from(ctx);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {		
		// Inflate the custom view
		convertView = inflater.inflate(R.layout.place_list_item, null);
		final Place p = (Place)getItem(position);
		final DecimalFormat df = new DecimalFormat("#.##");
		final String strDist = "" + df.format(p.getDistance()) + " mile";

		((TextView)convertView.findViewById(R.id.tvwPlaceName)).setText(p.getName());
		((TextView)convertView.findViewById(R.id.tvwDist)).setText(strDist);

		return convertView;
	}
}
