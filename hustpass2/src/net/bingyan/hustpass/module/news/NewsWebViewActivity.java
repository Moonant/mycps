package net.bingyan.hustpass.module.news;

import net.bingyan.hustpass.module.news.NewsContentBean;
import net.bingyan.hustpass.ui.base.WebViewActivity;

import com.google.gson.Gson;

public class NewsWebViewActivity extends WebViewActivity {
	@Override
	public String getFormatHtml(String jsonRespon) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		NewsContentBean.Article article = gson.fromJson(jsonRespon, NewsContentBean.class)
				.getArticle();

		return "<div style=\"font-size: x-large;\">" + article.getTitle()
				+ "</div><div style=\"color:#999\">"
				+ article.getPublishDate() + "</div><hr/>"
				+ article.getContent();
	}
}