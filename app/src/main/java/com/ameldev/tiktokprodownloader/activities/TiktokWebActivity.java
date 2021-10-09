package com.ameldev.tiktokprodownloader.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.databinding.ActivityWebTiktokBinding;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import me.jingbin.web.ByWebView;

public class TiktokWebActivity extends AppCompatActivity {

    private ActivityWebTiktokBinding binding;
    private String mUrl = "https://www.tiktok.com/foryou";
    private ByWebView byWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_tiktok);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black), 0);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        byWebView = ByWebView
                .with(this)
                .setWebParent(binding.layoutTiktokWeb, new LinearLayout.LayoutParams(-1, -1))
                .useWebProgress(ContextCompat.getColor(this, R.color.design_default_color_background))
                .loadUrl(mUrl);
        byWebView.getWebView().getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
    }

    @Override
    protected void onPause() {
        super.onPause();
        byWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        byWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        byWebView.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (byWebView.handleKeyEvent(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
