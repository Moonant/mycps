package net.bingyan.hustpass.module.home;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.http.ImageWorkIon;
import net.bingyan.hustpass.http.ImageWorker;
import net.bingyan.hustpass.ui.base.WebViewJsActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.app.SherlockFragment;

public class SlideImageFragment extends SherlockFragment {

	String imgUrl;
	String url;
	ImageWorker mImageWorker;

	public static SlideImageFragment newInstance(Bundle bundle) {
		SlideImageFragment imageFragment = new SlideImageFragment();
		imageFragment.setArguments(bundle);
		return imageFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        mImageWorker = new ImageWorkIon(getActivity());
        imgUrl = getArguments().getString("imgurl");
		url = getArguments().getString("url");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.image_fragment, container, false);
		final ImageView imageView = (ImageView) view.findViewById(R.id.image);

        mImageWorker.displayImageView(imageView, imgUrl);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), WebViewJsActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);
			}
		});

		return view;
	}
}
