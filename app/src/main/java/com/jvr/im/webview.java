package com.jvr.im;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        String web = intent.getStringExtra("url");

        WebView webView = this.findViewById(R.id.webview);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        if(web.contains(".pdf")){
            web = "http://docs.google.com/gview?embedded=true&url=" + web;        }
        webView.loadUrl(web);

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}