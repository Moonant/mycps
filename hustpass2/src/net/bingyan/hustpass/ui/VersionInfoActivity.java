package net.bingyan.hustpass.ui;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.base.BaseActivity;
import net.bingyan.hustpass.util.Pref;
import net.bingyan.hustpass.util.Util;

public class VersionInfoActivity extends BaseActivity {
	ImageView btn;
	public final int STATE_CHECK = 0;
	public final int STATE_CHECKING = 1;
	public final int STATE_DOWNLOAD = 2;
	int state = 0;
	int[] imgIds = { R.drawable.version_check, R.drawable.version_loading,
			R.drawable.version_download };
	SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_version_info);
		mPref = Pref.getPref();
		initView();
	}

	private void initView() {
		btn = (ImageView) findViewById(R.id.version_info_btn);
		btn.setOnClickListener(this);
		if (mPref.getBoolean("is_has_new_version", false)) {
			state = STATE_DOWNLOAD;
		} else {
			state = STATE_CHECK;
		}
		btn.setImageResource(imgIds[state]);
		((TextView) findViewById(R.id.version_info_version_code)).setText(""
				+ getVersionName());
	}

	private String getVersionName() {
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "error";

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		if (arg0.getId() == R.id.version_info_btn) {
			switch (state) {
			case STATE_CHECK:
				state = STATE_CHECKING;
				btn.setImageResource(imgIds[state]);
				Util.toast("正在检查更新");
				UmengUpdateAgent.update(VersionInfoActivity.this);
				// 从服务器获取更新信息的回调函数
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						state = STATE_CHECK;
						switch (updateStatus) {
						case 0: // 有更新
							UmengUpdateAgent.showUpdateDialog(
									VersionInfoActivity.this, updateInfo);
							break;
						case 1: // 无更新
							Util.toast("当前已是最新版.");
							break;
						case 2: // 如果设置为wifi下更新且wifi无法打开时调用
							Util.toast("未连接wifi,关闭'使用wifi时自动更新',再次检查");
							break;
						case 3: // 连接超时
							Util.toast("连接超时，请稍候重试");
							break;
						}
						btn.setImageResource(imgIds[state]);
					}
				});
				break;
			case STATE_CHECKING:
				break;
			case STATE_DOWNLOAD:
				break;
			default:
				break;
			}
		}
	}

}
