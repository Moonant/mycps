package net.bingyan.hustpass.util;

import net.bingyan.hustpass.MyApplication;
import android.content.SharedPreferences;

public class Pref {
	private static SharedPreferences mPref;
	
	public static SharedPreferences getPref(){
		if(mPref == null){
			mPref = MyApplication.getInstance().getSharedPreferences();
		}
		return mPref;
	}
	
	public static final String IS_FIRST_LOGIN = "is_first_login";
	
	public static final String INT_ELEC_ALARM = "elec_alarm";
	public static final String INT_NET_ALARM = "net_alarm";
	public static final String IS_SCORE_PROTECT = "is_score_pretect";
	public static final String STR_SCORE_PSWORD = "score_psword";
	public static final String IS_SCORE_PUSH = "is_score_push";
	public static final String IS_WIFI_AUTO_UPDATE = "is_wifi_auto_update";
	public static final String IS_VERSION_AUTO_UPDATE = "is_version_auto_update";
	
	public static final String HUB_UID = "hub_uid";
	public static final String HUB_PSWORD = "hub_psword";
	
	public static final String ANNOUNCEMENT_INDEX = "announcement_index";
	
	public static final String ELEC_AREA = "elec_area";
	public static final String ElEC_BUILDING = "elec_building";
	public static final String ELEC_ROOM = "elec_room";
	public static final String ELEC_REMAIN = "elec_remain";
	
	public static final String WIFI_USER = "wifi_user";
	public static final String WIFI_PSW = "wifi_psw";
	public static final String CLASSROOM_BUILDING_ID = "classroom_building_id";
	
	public static final String HOME_SIDE_NEWEST_ID = "home_side_newest_id";
	public static final String HOME_SIDE_IMG = "home_side_img";
	public static final String HOME_SIDE_URL = "HOME_SIDE_URL";

    //招新
    public static final String RECRUIT_LOTTERY_NUM = "recruit_lottery_num";
    public static final String RECRUIT_STATE = "recruit_state";

    public static final String RECRUIT_WIFI = "recruit_wifi";
}
