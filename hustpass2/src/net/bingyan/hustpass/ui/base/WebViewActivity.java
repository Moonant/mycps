package net.bingyan.hustpass.ui.base;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.http.HttpUtil;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 *  获取 html 字符串 然后 处理 展示
 * */
public class WebViewActivity extends BaseActivity {
	String url;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		url = getIntent().getExtras().getString("url");
        setContentView(R.layout.webview);

		new InitTask().execute();
	}

	private class InitTask extends AsyncTask<Void, Void, String> {
		String jsonRespon;

		@Override
		protected void onCancelled() {
			finish();
		}

		@Override
		protected String doInBackground(Void... params) {

			try {
				return jsonRespon = HttpUtil.getString(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null){
				try {
					WebView webview = (WebView) findViewById(R.id.webView1);
					String html = getFormatHtml(jsonRespon);
					webview.loadDataWithBaseURL(null, html, "text/html","utf-8", null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public String getFormatHtml(String jsonRespon) {
		return jsonRespon;
	}

}
