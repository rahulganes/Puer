package com.puer.rahul.puer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Anxiety_therapy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_anxiety_therapy);

        WebView youtubeWebView = (WebView)findViewById(R.id.yt_anxiety_therapy_1); //todo find or bind web view
        String myVideoYoutubeId = "EtBcgcA8xZI";

        youtubeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        youtubeWebView.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId);

        WebView youtubeWebView2 = (WebView)findViewById(R.id.yt_anxiety_therapy_2); //todo find or bind web view
        String myVideoYoutubeId2 = "rQZMQMprrKU";

        youtubeWebView2.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings2 = youtubeWebView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);
        webSettings2.setLoadWithOverviewMode(true);
        webSettings2.setUseWideViewPort(true);

        youtubeWebView2.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId2);


        WebView youtubeWebView3 = (WebView)findViewById(R.id.yt_anxiety_therapy_3); //todo find or bind web view
        String myVideoYoutubeId3 = "KjU1-Zr4FuE";

        youtubeWebView3.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings3 = youtubeWebView3.getSettings();
        webSettings3.setJavaScriptEnabled(true);
        webSettings3.setLoadWithOverviewMode(true);
        webSettings3.setUseWideViewPort(true);

        youtubeWebView3.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId3);
        
        
    }
}
