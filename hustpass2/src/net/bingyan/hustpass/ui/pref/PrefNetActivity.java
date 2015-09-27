package net.bingyan.hustpass.ui.pref;

import android.os.Bundle;
import android.widget.TextView;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.widget.LineSeekBar;
import net.bingyan.hustpass.widget.LineSeekBar.OnSeekChangeListener;

public class PrefNetActivity extends BaseActivity {
	int mProgress = Pref.getPref().getInt(Pref.INT_NET_ALARM, 0);
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_pref_set_net);
		initView();
	}

	private void initView() {
        final TextView dayLeft = (TextView) findViewById(R.id.pref_net_day);
        dayLeft.setText("" + mProgress);

        LineSeekBar lineSeekBar = (LineSeekBar) findViewById(R.id.seekbar_line);
		lineSeekBar.setMaxProgress(6);
        lineSeekBar.setProgress(mProgress);
        lineSeekBar.invalidate();
		
		lineSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {

			@Override
			public void onProgressChange(int newProgress) {
				mProgress = newProgress;
				dayLeft.setText(""+mProgress);
			}
		});

	}

    @Override
    protected void onStop() {
        super.onStop();
        Pref.getPref().edit().putInt(Pref.INT_NET_ALARM, mProgress).commit();
    }
}
