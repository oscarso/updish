package co.oscarsoft.updish;

import java.net.URL;
import java.net.UnknownHostException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class AsyncGetWebImage extends AsyncTask<String, Void, Bitmap> {
	private URL		_url;
	private String	_imagePath;
	private View	_v;

	/* Default Constructor */
	public AsyncGetWebImage(View v) {
		_imagePath = null;
		_url = null;
		_v = v;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap imageBM = null;
		_imagePath = params[0];
		try {
			_url = new URL(_imagePath); 
			imageBM = BitmapFactory.decodeStream(_url.openConnection().getInputStream());
		} catch (UnknownHostException e) {
			Log.d(DebugInfo.TAG, "");
			e.printStackTrace();
		} catch (Exception e) {
			Log.d(DebugInfo.TAG, "");
			e.printStackTrace();
		 }
		return imageBM;
	}
	
	@Override
    protected void onPostExecute(Bitmap bm) {
		((ImageView)_v).setImageBitmap(bm);
    }
}
