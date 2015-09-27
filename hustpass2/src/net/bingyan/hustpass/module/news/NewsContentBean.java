package net.bingyan.hustpass.module.news;


public class NewsContentBean {
	String status;
	Article article;
	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public class Article{
		String Id;
		String Title;
		String Content;
		String PublishDate;
		public String getPublishDate() {
			return PublishDate;
		}
		public void setPublishDate(String publishDate) {
			PublishDate = publishDate;
		}
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
		public String getContent() {
			return Content;
		}
		public void setContent(String content) {
			Content = content;
		}
	}
}
