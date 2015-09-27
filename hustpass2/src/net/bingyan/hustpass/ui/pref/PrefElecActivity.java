package net.bingyan.hustpass.ui.pref;

import android.os.Bundle;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.widget.CircularSeekBar;
import net.bingyan.hustpass.widget.CircularSeekBar.OnSeekChangeListener;

public class PrefElecActivity extends BaseActivity {
	int mProgress = Pref.getPref().getInt(Pref.INT_ELEC_ALARM, 0);
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_pref_set_elec);
		initView();
	}


	private void initView() {
        CircularSeekBar circularSeekbar = (CircularSeekBar) findViewById(R.id.seekbar_circular);
		circularSeekbar.setMaxProgress(60);
		circularSeekbar.setProgress(mProgress);
		circularSeekbar.invalidate();

		circularSeekbar.setSeekBarChangeListener(new OnSeekChangeListener() {

			@Override
			public void onProgressChange(CircularSeekBar view, int newProgress) {
				mProgress = newProgress;
			}
		});
	}

    @Override
    protected void onStop() {
        super.onStop();
        Pref.getPref().edit().putInt(Pref.INT_ELEC_ALARM, mProgress).commit();
    }
}
