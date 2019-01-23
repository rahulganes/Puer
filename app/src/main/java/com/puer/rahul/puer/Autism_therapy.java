package com.puer.rahul.puer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Autism_therapy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.hide();
        }
        setContentView(R.layout.activity_autism_therapy);

        WebView youtubeWebView = (WebView)findViewById(R.id.yt_autism_therapy_1); //todo find or bind web view
        String myVideoYoutubeId = "iyCx-OLzgJw";

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

        WebView youtubeWebView2 = (WebView)findViewById(R.id.yt_autism_therapy_2); //todo find or bind web view
        String myVideoYoutubeId2 = "42fU-Md6Y3Q";

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

        WebView youtubeWebView3 = (WebView)findViewById(R.id.yt_autism_therapy_3); //todo find or bind web view
        String myVideoYoutubeId3 = "vkymZzmg4jw";

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

        WebView youtubeWebView4 = (WebView)findViewById(R.id.yt_autism_therapy_4); //todo find or bind web view
        String myVideoYoutubeId4 = "Hs-412lhXb0";

        youtubeWebView4.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings4 = youtubeWebView4.getSettings();
        webSettings4.setJavaScriptEnabled(true);
        webSettings4.setLoadWithOverviewMode(true);
        webSettings4.setUseWideViewPort(true);

        youtubeWebView4.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId4);

        WebView youtubeWebView5 = (WebView)findViewById(R.id.yt_autism_therapy_5); //todo find or bind web view
        String myVideoYoutubeId5 = "37qPEWQMQa4";

        youtubeWebView5.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings5 = youtubeWebView5.getSettings();
        webSettings5.setJavaScriptEnabled(true);
        webSettings5.setLoadWithOverviewMode(true);
        webSettings5.setUseWideViewPort(true);

        youtubeWebView5.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId5);

        WebView youtubeWebView6 = (WebView)findViewById(R.id.yt_autism_therapy_6); //todo find or bind web view
        String myVideoYoutubeId6 = "YUdsgQGHSR8";

        youtubeWebView6.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings6 = youtubeWebView6.getSettings();
        webSettings6.setJavaScriptEnabled(true);
        webSettings6.setLoadWithOverviewMode(true);
        webSettings6.setUseWideViewPort(true);

        youtubeWebView6.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId6);

        WebView youtubeWebView7 = (WebView)findViewById(R.id.yt_autism_therapy_7); //todo find or bind web view
        String myVideoYoutubeId7 = "GORS1Wzbl8w";

        youtubeWebView7.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings7 = youtubeWebView7.getSettings();
        webSettings7.setJavaScriptEnabled(true);
        webSettings7.setLoadWithOverviewMode(true);
        webSettings7.setUseWideViewPort(true);

        youtubeWebView7.loadUrl("https://www.youtube.com/embed/" + myVideoYoutubeId7);



    }
}
