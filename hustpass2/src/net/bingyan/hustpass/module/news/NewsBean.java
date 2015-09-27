package net.bingyan.hustpass.module.news;

import java.util.List;

public class NewsBean {
	//0 is ok
	public String status;
	public List<Article> articlelist;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Article> getArticlelist() {
		return articlelist;
	}

	public void setArticlelist(List<Article> articlelist) {
		this.articlelist = articlelist;
	}

	public class Article{
		public String Id;
		public String Title;
		public String PublishDate;
		public String getId() {
			return Id;
		}
		public void setId(String id) {
			Id = id;
		}
		public String getTitle() {
			return Title;
		}
		public void setTitle(String title) {
			Title = title;
		}
		public String getPublishDate() {
			return PublishDate;
		}
		public void setPublishDate(String publishDate) {
			PublishDate = publishDate;
		}
		public String getTitlepic() {
			return Titlepic;
		}
		public void setTitlepic(String titlepic) {
			Titlepic = titlepic;
		}
		public String getBrief() {
			return brief;
		}
		public void setBrief(String brief) {
			this.brief = brief;
		}
		public String Titlepic;
		public String brief;
	}
}
