package net.bingyan.hustpass.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import net.bingyan.hustpass.R;

public class WelcomeFragment extends SherlockFragment {
	
	int imgId ;
	public static WelcomeFragment newInstance(Bundle bundle) {
		WelcomeFragment welcomeFragment = new WelcomeFragment();
		welcomeFragment.setArguments(bundle);
		return welcomeFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imgId = getArguments().getInt("imgId");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.welcome_fragment, container,false);
		View imageView = view.findViewById(R.id.welcome_main);
		imageView.setBackgroundResource(imgId);
		return view;
	}
}




