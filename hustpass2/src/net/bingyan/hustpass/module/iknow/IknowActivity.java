package net.bingyan.hustpass.module.iknow;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.bingyan.hustpass.R;
import net.bingyan.hustpass.ui.base.BaseActivity;

public class IknowActivity extends BaseActivity {
    private WebView mWebView;
    ProgressBar pb;


    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_iknow);

        pb = (ProgressBar) findViewById(R.id.iknow_pb);
        pb.setMax(100);

        mWebView = (WebView) findViewById(R.id.iknow_webview);
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

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

        mWebView.loadUrl("http://ik.hustonline.net/wap/");
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}