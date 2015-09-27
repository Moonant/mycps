package net.bingyan.hustpass.module.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ant on 14-8-13.
 */
public class NewsSlideBean {

    @Expose
    private String status;
    @Expose
    private List<Articlelist> articlelist = new ArrayList<Articlelist>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Articlelist> getArticlelist() {
        return articlelist;
    }

    public void setArticlelist(List<Articlelist> articlelist) {
        this.articlelist = articlelist;
    }

    public class Articlelist {

        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Titlepic")
        @Expose
        private String titlepic;

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

        public String getTitlepic() {
            return titlepic;
        }

        public void setTitlepic(String titlepic) {
            this.titlepic = titlepic;
        }

    }
}