package net.bingyan.hustpass;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SLIDE.
 */
public class Slide {

    private Long id;
    private String imageurl;
    private String siteurl;

    public Slide() {
    }

    public Slide(Long id) {
        this.id = id;
    }

    public Slide(Long id, String imageurl, String siteurl) {
        this.id = id;
        this.imageurl = imageurl;
        this.siteurl = siteurl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSiteurl() {
        return siteurl;
    }

    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl;
    }

}
