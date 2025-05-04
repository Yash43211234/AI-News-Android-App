package com.example.instanews;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview); // this layout must contain a WebView with id "webView"

//
        // Enable WebView debugging for development
        WebView.setWebContentsDebuggingEnabled(true);

        String url = getIntent().getStringExtra("url");
        Log.d("WebViewDebug", "Loading URL: " + url);


        // Initialize WebView
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // if the page needs JS

        // Load the URL
        webView.loadUrl(url);
    }
}
