package net.bingyan.hustpass.ui.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.app.SherlockFragment;

import net.bingyan.hustpass.util.AppLog;

public class BaseFragment extends SherlockFragment implements OnClickListener {
    public AppLog mLog = new AppLog(getClass());
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}


    BaseActivity activity;
    public void showProgressBar() {
        if(activity==null) {
            activity = ((BaseActivity) getActivity());
        }
        if(activity!=null) {
            activity.showProgressBar();
        }
    }


    public void stopProgressBar() {
        if(activity==null) {
            activity = ((BaseActivity) getActivity());
        }
        if(activity!=null) {
            activity.stopProgressBar();
        }

    }
}
