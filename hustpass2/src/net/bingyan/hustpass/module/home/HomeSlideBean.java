package net.bingyan.hustpass.module.home;

import java.util.List;

public class HomeSlideBean {
	List<Data> data;
	int success;

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public class Data {
		String id;
		String imageurl;
		String siteurl;

		public String getId() {
			return id;
		}

		public void setId(String id) {
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
}
