package net.bingyan.hustpass;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import net.bingyan.hustpass.util.AppLog;

public class MyApplication extends Application{
    AppLog mLog = new AppLog(getClass());
	static SharedPreferences pref;
	static MyApplication myApp;
	static Context context;

    public DaoSession daoSession;

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "example-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
  
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MobclickAgent.setDebugMode( false );

        myApp = this;
		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		context = getApplicationContext();

        setupDatabase();
	}
	public static MyApplication getInstance(){
		return myApp;
	}
	public static Context getAppContext(){
		return context;
	}
	
	public static SharedPreferences getSharedPreferences(){		
		return pref;
	}
	

}
