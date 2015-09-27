package net.bingyan.hustpass.ui.pref;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.module.score.HubLoginActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.module.score.ScoreActivity;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Util;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PrefScorePswordActivity extends BaseActivity {
	// 输入新密码 成功》1
	public static final int STATE_SET_PSWORD = 0;
	// 重复密码 成功 》设置
	public static final int STATE_SET_PSWORD_REPETE = 1;
	// 输入密码 成功》成绩
	public static final int STATE_ENTER_PSWORD = 2;
	// 输入密码 成功》0
	public static final int STATE_CHANGE_PSWORD = 3;
	// 输入密码 成功》0
	public static final int STATE_RM_PSWORD = 4;
	int state;
	String psWordTemp;
	String psWordPref;
	PswordKeyBoardHelper mBoardHelper;
	SharedPreferences mPref;

    PswordState pswordState;
    TextView alertTV;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		state = getIntent().getIntExtra("state", 0);

		mPref = Pref.getPref();
		psWordPref = mPref.getString(Pref.STR_SCORE_PSWORD, null);

		setContentView(R.layout.activity_password);
        alertTV = (TextView) findViewById(R.id.ps_type_info);

        switch (state) {
            case STATE_SET_PSWORD:
                pswordState = new SetPswordState();
                break;
            case STATE_SET_PSWORD_REPETE:
                pswordState = new SetTwicePswordState();
                break;
            case STATE_ENTER_PSWORD:
                pswordState = new EnterPswordState();
                break;
            case STATE_CHANGE_PSWORD:
                pswordState = new ChangePswordState();
                break;
            case STATE_RM_PSWORD:
                pswordState = new RmPswordState();
                break;
        }

		mBoardHelper = new PswordKeyBoardHelper();
		mBoardHelper.reset();
	}

    private void onInputFinished(String inputPsword) {
        pswordState.onInputFinished(inputPsword);
        mBoardHelper.reset();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		pswordState.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		pswordState.forgetPsword();
		return super.onOptionsItemSelected(item);
	}



    class SetPswordState extends PswordState {
        public  SetPswordState(){
            alertTV.setText("请输入新密码");
        }
        public void onCreateOptionsMenu(Menu menu){
        }
        public void forgetPsword(){
        }
        public void onInputFinished(String inputPsword){
            psWordTemp = inputPsword;
            pswordState = new SetTwicePswordState();
        }
    }

    class SetTwicePswordState extends PswordState {
        public  SetTwicePswordState(){
            alertTV.setText("请重复新密码");
        }
        public void onInputFinished(String inputPsword){
            if (psWordTemp.equals(inputPsword)) {
                mPref.edit().putBoolean(Pref.IS_SCORE_PROTECT, true).putString(Pref.STR_SCORE_PSWORD, inputPsword).apply();
                finish();
            } else {
                Util.toast("密码错误");
                pswordState = new SetPswordState();
            }
        }
    }

    class EnterPswordState extends PswordState {
        public  EnterPswordState(){
            alertTV.setText("请输入密码");
        }
        public void onInputFinished(String inputPsword){
            if (inputPsword.equals(psWordPref)) {
                Intent intent = new Intent(PrefScorePswordActivity.this, ScoreActivity.class);
                startActivity(intent);
                finish();
            } else {
                Util.toast("密码错误");
            }
        }
    }

    class ChangePswordState extends PswordState {
        public  ChangePswordState(){
            alertTV.setText("请输入旧密码");
        }
        public void onInputFinished(String inputPsword){
            if (inputPsword.equals(psWordPref)) {
                pswordState = new SetPswordState();
            } else {
                Util.toast("密码错误");
            }
        }
    }

    class RmPswordState extends PswordState {
        public  RmPswordState(){
            alertTV.setText("请输入旧密码");
        }

        public void onInputFinished(String inputPsword){
            if (inputPsword.equals(psWordPref)) {
                mPref.edit().putBoolean(Pref.IS_SCORE_PROTECT, false).commit();
                finish();
            } else {
                Util.toast("密码错误");
            }
        }
    }


    abstract class PswordState{
        abstract public void onInputFinished(String inputPsword);
        public void onCreateOptionsMenu(Menu menu){
            MenuItem forgetBtn = menu.add(1, 0, 0, "忘记密码");
            forgetBtn.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT
                    | MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        public void forgetPsword(){
            Intent intent = new Intent(PrefScorePswordActivity.this, HubLoginActivity.class);
            startActivity(intent);
        };
    }

	class PswordKeyBoardHelper implements View.OnClickListener{
		ImageView[] dots = new ImageView[4];
		Drawable psDotEdit;
		Drawable psDotUnedit;
		int btnIds[] = { R.id.ps_key_0, R.id.ps_key_1, R.id.ps_key_2,
				R.id.ps_key_3, R.id.ps_key_4, R.id.ps_key_5, R.id.ps_key_6,
				R.id.ps_key_7, R.id.ps_key_8, R.id.ps_key_9,
				R.id.ps_key_cancle, R.id.ps_key_back };
		int count = 0;
		String pswordInput = "";

		public PswordKeyBoardHelper() {
			for (int i = 0; i < btnIds.length; i++) {
				findViewById(btnIds[i]).setOnClickListener(this);
			}
			dots[0] = (ImageView) findViewById(R.id.psword_dot_1);
			dots[1] = (ImageView) findViewById(R.id.psword_dot_2);
			dots[2] = (ImageView) findViewById(R.id.psword_dot_3);
			dots[3] = (ImageView) findViewById(R.id.psword_dot_4);
			psDotEdit = getResources()
					.getDrawable(R.drawable.psword_dot_edited);
			psDotUnedit = getResources().getDrawable(
					R.drawable.psword_dot_unedited);
		}

		private void add(int num) {
			if (count == 4)
				return;
			count++;
			pswordInput += num;
			dots[count - 1].setImageDrawable(psDotEdit);
			if (count == 4) {
				onInputFinished(pswordInput);
			}
		}

		private void cancle() {
		}

		private void back() {
			if (count > 0) {
				count--;
				pswordInput = pswordInput
						.substring(0, pswordInput.length() - 1);
				dots[count].setImageDrawable(psDotUnedit);
			}
		}

		public void reset() {
			count = 0;
			pswordInput = "";
			for (int i = 0; i < dots.length; i++) {
				dots[i].setImageDrawable(psDotUnedit);
			}
		}

		public void onClick(View arg0) {
			for (int i = 0; i < btnIds.length; i++) {
				if (btnIds[i] == arg0.getId()) {
					if (i == 10) {
						cancle();
					} else if (i == 11) {
						back();
					} else {
						add(i);
					}
				}
			}
		}

	}

}
