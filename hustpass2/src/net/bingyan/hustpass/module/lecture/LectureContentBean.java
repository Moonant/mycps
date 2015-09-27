package net.bingyan.hustpass.module.lecture;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LectureContentBean {
	String title;
	String category;
	String speaker;
    @SerializedName("begin_time")
    @Expose
    private String beginTime;
	String location;
	String description;
	
	
	public String getSpeaker() {
		return speaker;
	}
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
