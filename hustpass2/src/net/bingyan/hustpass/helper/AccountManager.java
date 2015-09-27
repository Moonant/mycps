package net.bingyan.hustpass.helper;

import android.content.SharedPreferences;
import android.text.TextUtils;

import net.bingyan.hustpass.util.Pref;

public  class AccountManager {

    public static final int ELEC = 0;
    public static final int HUB = 1;
    public static final int WIFI = 2;

    static SharedPreferences pref = Pref.getPref();

    static public AccountManager get(int type) {
        if (type == ELEC) {
            return new ElecAccountManager();
        } else if (type == HUB) {
            return new HubAccountManager();
        } else if (type == WIFI) {
            return new WifiAccountManager();
        } else {
            return null;
        }
    }

    static public HubAccountManager getHubManger() {
        return new HubAccountManager();
    }

    static public WifiAccountManager getWifiManger() {
        return new WifiAccountManager();
    }

    static public ElecAccountManager getElecManger() {
        return new ElecAccountManager();
    }

    static public HustAccountManager getHustManger() {
        return new HustAccountManager();
    }

    static public class HubAccountManager extends AccountManager {
        public boolean isScorePretected() {
            return pref.getBoolean("is_score_pretect", false);
        }

        public boolean isLogin() {
            return getUid()!=null;
        }

        public String getUid() {
            return pref.getString("hub_uid", null);
        }

        public String getPsw() {
            return pref.getString("hub_psword", null);
        }

        public void updateAccount(String uid, String psw) {

            if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(psw)) {
                throw new NullPointerException("null point account");
            }

            pref.edit().putString("hub_uid", uid)
                    .putString("hub_psword", psw)
                    .putBoolean("is_score_pretect", false)
                    .apply();
        }

        public boolean isBound(){
            return getBoundUid()!=null;
        }

        public String getBoundUid(){
            return pref.getString("hub_bound_uid", null);
        }

        public String getBoundPsw(){
            return pref.getString("hub_bound_psword", null);
        }

        public void updateBound(String uid, String psw){
            if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(psw)) {
                throw new NullPointerException("null point account");
            }

            pref.edit().putString("hub_bound_uid", uid)
                    .putString("hub_bound_psword", psw).apply();
        }
    }

    static public class ElecAccountManager extends AccountManager {
        public String getArea() {
            return pref.getString("elec_area", null);
        }

        public String getBoundArea() {
            return pref.getString("elec_bound_area", null);
        }

        public int getBuilding() {
            return pref.getInt("elec_building", -1);
        }

        public int getBoundBuilding() {
            return pref.getInt("elec_bound_building", -1);
        }

        public String getRoom() {
            return pref.getString("elec_room", null);
        }

        public String getBoundRoom() {
            return pref.getString("elec_bound_room", null);
        }


        public boolean isLogin() {
            return getRoom()!=null;
        }

        public boolean isBound() {
            return getBoundRoom()!=null;
        }

        public void updateAccount(String area, int building, String room) {
            pref.edit().putString("elec_area", area).putInt("elec_building", building).putString("elec_room", room).apply();
        }

        public void updateBoundAccount(String area, int building, String room) {
            pref.edit().putString("elec_bound_area", area).putInt("elec_bound_building", building).putString("elec_bound_room", room).apply();
        }

        public void updateRemain(float remain) {
            pref.edit().putFloat("elec_remain", remain).apply();
        }

        public void updateBoundRemain(float remain) {
            pref.edit().putFloat("elec_bound_remain", remain).apply();
        }

    }

    static public class WifiAccountManager extends AccountManager {
        public boolean isLogin() {
            return getUid()!=null;
        }

        public String getUid() {

            return pref.getString("wifi_user", null);
        }

        public String getPsw() {

            return pref.getString("wifi_psw", null);
        }

        public void updateAccount(String uid, String psw) {

            if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(psw)) {
                throw new NullPointerException("null point account");
            }

            pref.edit().putString("wifi_user", uid).putString("wifi_psw", psw).apply();
        }

        public boolean isBound() {
            return getBoundUid()!=null;
        }

        public String getBoundUid() {

            return pref.getString("wifi_bound_user", null);
        }

        public String getBoundPsw() {

            return pref.getString("wifi_bound_psw", null);
        }

        public void updateBound(String uid, String psw) {

            if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(psw)) {
                throw new NullPointerException("null point account");
            }

            pref.edit().putString("wifi_bound_user", uid).putString("wifi_bound_psw", psw).apply();
        }

    }

    static public class HustAccountManager extends AccountManager {
        public boolean isLogin(){
            return getUserName()!=null;
        }

        public boolean isEmailChecked(){
            return pref.getBoolean("is_hust_email_checked",false);
        }

        public boolean isHubAsyned(){
            return pref.getBoolean("is_hub_account_asyn",false);
        }

        public boolean isWifiAsyned(){
            return pref.getBoolean("is_wifi_account_asyn",false);
        }

        public boolean isElecAsyned(){
            return pref.getBoolean("is_elec_account_asyn",false);
        }

        public Long getUid(){
            return pref.getLong("hust_uid",-1);
        }

        public String getUserName(){
            return pref.getString("hust_username",null);
        }

        public String getEmail(){
            return pref.getString("hust_email",null);
        }

        public String getPsw(){
            return pref.getString("hust_psw",null);
        }

        public void setIsEmailChecked(boolean isEmailChecked) {
            pref.edit().putBoolean("is_hust_email_checked",isEmailChecked).apply();
        }

        public void setIsHubAsyned(boolean isHubUpdate){
            pref.edit().putBoolean("is_hub_account_asyn",isHubUpdate).apply();
        }

        public void setIsWifiAsyned(boolean isWifiUpdate){
            pref.edit().putBoolean("is_wifi_account_asyn",isWifiUpdate).apply();
        }

        public void setIsElecAsyned(boolean isElecUpdate){
            pref.edit().putBoolean("is_elec_account_asyn",isElecUpdate).apply();
        }

        public void putEmailandPsw(String email, String psw){
            pref.edit().putString("hust_email",email).putString("hust_psw",psw).apply();
        }

        public void putUid(Long id){
            pref.edit().putLong("hust_uid", id).apply();
        }

        public void putUserName(String userName){
            pref.edit().putString("hust_username",userName).apply();
        }

    }
}
