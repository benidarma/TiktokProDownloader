package com.ameldev.tiktokprodownloader.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.databinding.ActivityWebviewBinding;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

public class WebviewActivity extends AppCompatActivity {

    private ActivityWebviewBinding binding;
    private String IntentKey, IntentURL, IntentTitle;
    private String html = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_0054BB), 0);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        IntentKey = getIntent().getStringExtra("Key");
        IntentURL = getIntent().getStringExtra("URL");
        IntentTitle = getIntent().getStringExtra("Title");

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.TVTitle.setText(IntentTitle);

        binding.swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        binding.swipeRefreshLayout.setRefreshing(true);
                        LoadPage(IntentURL);
                    }
                }
        );

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadPage(IntentURL);
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void LoadPage(String Url) {
        binding.webView1.setWebViewClient(new MyBrowser());
        binding.webView1.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                } else {
                    binding.swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
        binding.webView1.getSettings().setLoadsImagesAutomatically(true);
        binding.webView1.getSettings().setJavaScriptEnabled(true);
        binding.webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (IntentKey.equals(getString(R.string.privacy_policy))) {
            binding.webView1.loadData(html, "text/html", "UTF-8");
        } else {
            binding.webView1.loadUrl(Url);
        }
    }

    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}