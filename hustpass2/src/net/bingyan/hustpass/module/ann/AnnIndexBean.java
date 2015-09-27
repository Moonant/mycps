package net.bingyan.hustpass.module.ann;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ant on 14-8-5.
 */
public class AnnIndexBean {

    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("dep_name")
        @Expose
        private String depName;
        @SerializedName("simp_name")
        @Expose
        private String simpName;
        @Expose
        private List<String> category = new ArrayList<String>();

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public String getSimpName() {
            return simpName;
        }

        public void setSimpName(String simpName) {
            this.simpName = simpName;
        }

        public List<String> getCategory() {
            return category;
        }

        public void setCategory(List<String> category) {
            this.category = category;
        }

    }

}