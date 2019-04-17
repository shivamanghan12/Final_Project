package com.android.newsfeedapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.newsfeedapp.Global;
import com.android.newsfeedapp.R;

public class WebActivity extends AppCompatActivity {

    private WebView mWebview;
    private String mUrl, mTitle;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        mUrl = intent.getStringExtra(Global.INTENT_WEBVIEW_URL);
        mTitle = intent.getStringExtra(Global.INTENT_WEBVIEW_TITLE);

        setupToolbar();

        mProgressBar = (ProgressBar) findViewById(R.id.pb_main);
        mWebview = (WebView) findViewById(R.id.wb_main);

        //Setup webview
        mWebview = (WebView) findViewById(R.id.wb_main);
        mWebview.setWebViewClient(new MyBrowser());
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebview.loadUrl(mUrl);

        mProgressBar.setVisibility(View.VISIBLE);

        mWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("News Detail");

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
