package net.bingyan.hustpass.module.ann;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class AnnListBean {

    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object other) {
        if(other == null){
            return false;
        }
        return getData().get(0).getId().equals(((AnnListBean)other).getData().get(0).getId());
    }


    public class Datum {

        @Expose
        private String id;
        @Expose
        private String title;
        @Expose
        private String url;
        @Expose
        private String date;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }



    }
}

