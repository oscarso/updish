package co.oscarsoft.updish;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<Product> {
	private LayoutInflater	inflater;
	private AsyncGetWebImage asyncGetWebImage;
	//private AsyncTask<String, Void, Bitmap> atGetWebImage;

	public ProductListAdapter(Context ctx, int resourceId, List<Product> objects) {
		super(ctx, resourceId, objects);
		inflater = LayoutInflater.from(ctx);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {		
		// Inflate the custom view
		convertView = inflater.inflate(R.layout.product_list_item, null);
		final Product p = (Product)getItem(position);
		ImageView imageViewProd = (ImageView)convertView.findViewById(R.id.ivwProduct);
		if (imageViewProd != null) {
			asyncGetWebImage = new AsyncGetWebImage(imageViewProd);
			asyncGetWebImage.execute(p.getImagePath());
		}

		((TextView)convertView.findViewById(R.id.tvwProductName)).setText(p.getUName_en());
		((TextView)convertView.findViewById(R.id.tvwProductNumOfVote)).setText(p.getNumOfVoteMsg());
		
		ImageView imageViewVoted = (ImageView)convertView.findViewById(R.id.ivwProductVoteStatus);
		if (p.getVoted()) {
			imageViewVoted.setImageResource(R.drawable.ud_vote_up);
		} else {
			imageViewVoted.setImageResource(R.drawable.ud_vote_down);
		}

		return convertView;
	}

}
