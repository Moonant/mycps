package net.bingyan.hustpass.module.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ant on 14-8-24.
 */
public class AccountBean {
    public class HubAccountBean {

        @SerializedName("hub_id")
        @Expose
        private String hubId;
        @Expose
        private Integer id;
        @SerializedName("hustonline_id")
        @Expose
        private Integer hustonlineId;
        @SerializedName("hub_passwd")
        @Expose
        private String hubPasswd;

        public String getHubId() {
            return hubId;
        }

        public void setHubId(String hubId) {
            this.hubId = hubId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getHustonlineId() {
            return hustonlineId;
        }

        public void setHustonlineId(Integer hustonlineId) {
            this.hustonlineId = hustonlineId;
        }

        public String getHubPasswd() {
            return hubPasswd;
        }

        public void setHubPasswd(String hubPasswd) {
            this.hubPasswd = hubPasswd;
        }

    }

    public class WifiAccountBean {

        @SerializedName("wifi_id")
        @Expose
        private String wifiId;
        @Expose
        private Integer id;
        @SerializedName("hustonline_id")
        @Expose
        private Integer hustonlineId;
        @SerializedName("wifi_passwd")
        @Expose
        private String wifiPasswd;

        public String getWifiId() {
            return wifiId;
        }

        public void setWifiId(String wifiId) {
            this.wifiId = wifiId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getHustonlineId() {
            return hustonlineId;
        }

        public void setHustonlineId(Integer hustonlineId) {
            this.hustonlineId = hustonlineId;
        }

        public String getWifiPasswd() {
            return wifiPasswd;
        }

        public void setWifiPasswd(String wifiPasswd) {
            this.wifiPasswd = wifiPasswd;
        }
    }

    public class ElecAccountBean {

        @SerializedName("area_id")
        @Expose
        private String areaId;
        @Expose
        private Integer id;
        @SerializedName("hustonline_id")
        @Expose
        private Integer hustonlineId;
        @SerializedName("building_id")
        @Expose
        private Integer buildingId;
        @SerializedName("room_id")
        @Expose
        private String roomId;

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getHustonlineId() {
            return hustonlineId;
        }

        public void setHustonlineId(Integer hustonlineId) {
            this.hustonlineId = hustonlineId;
        }

        public Integer getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(Integer buildingId) {
            this.buildingId = buildingId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

    }
}
