package net.bingyan.hustpass.module.map;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.LargeImageOpener;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.koushikdutta.ion.Ion;
import com.polites.android.GestureImageView;

public class MapActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
        setContentView(R.layout.activity_map);
        new MapTask().execute();
	}

	class MapTask extends AsyncTask<Void, Void, Bitmap> {
		protected final ProgressDialog progressDialog;

		public MapTask() {
			progressDialog = ProgressDialog
					.show(MapActivity.this, "", "加载中", true);
			progressDialog.setCancelable(true);
			progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});

		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Uri path = Uri.parse("android.resource://net.bingyan.hustpass/"
					+ R.drawable.hustmap);
			LargeImageOpener largeImageOpener = new LargeImageOpener( MapActivity.this);
			return largeImageOpener.openImage(path);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			progressDialog.dismiss();
			try {
				GestureImageView gView = (GestureImageView) findViewById(R.id.image);
				gView.setImageBitmap(result);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
