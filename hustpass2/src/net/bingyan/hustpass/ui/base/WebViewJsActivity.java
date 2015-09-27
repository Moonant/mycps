package net.bingyan.hustpass.ui.base;

import net.bingyan.hustpass.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Web 界面
 * */
public class WebViewJsActivity extends BaseActivity {
	private WebView mWebView;
	ProgressBar pb;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_iknow);

        pb = (ProgressBar) findViewById(R.id.iknow_pb);
		pb.setMax(100);

		mWebView = (WebView) findViewById(R.id.iknow_webview);
//		WebSettings webSettings = mWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});

        mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				pb.setVisibility(View.VISIBLE);
				pb.setProgress(progress);
				if (progress == 100) {
					pb.setVisibility(View.GONE);
				}
			}
		});

        mWebView.loadUrl(getIntent().getStringExtra("url"));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuItem forgetBtn = menu.add(1, 0, 0, "用浏览器打开网页");
		forgetBtn.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT
				| MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getGroupId() == 1 && item.getItemId() == 0) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(getIntent().getStringExtra("url")));
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return;
		}
		super.onBackPressed();
	}
}
