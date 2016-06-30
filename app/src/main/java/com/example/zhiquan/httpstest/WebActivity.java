package com.example.zhiquan.httpstest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by Zhiquan on 2016/6/30.
 */
public class WebActivity extends AppCompatActivity{

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = (WebView)findViewById(R.id.web_view);

        String text = getIntent().getStringExtra("html");

        webView.loadDataWithBaseURL(null,text, "text/html; charset=UTF-8", null,null);

    }
}
