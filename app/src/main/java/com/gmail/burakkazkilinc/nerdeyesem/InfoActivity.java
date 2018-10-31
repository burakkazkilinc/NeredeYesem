package com.gmail.burakkazkilinc.nerdeyesem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Bundle gelenveri=getIntent().getExtras();
        String url =gelenveri.getString("url");
        WebView web = findViewById(R.id.webview);
        web.setWebViewClient(new WebViewClient());

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl(url);
    }
}
