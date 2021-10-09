package com.ameldev.tiktokprodownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ameldev.tiktokprodownloader.BuildConfig;
import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.databinding.ActivityAboutBinding;
import com.ameldev.tiktokprodownloader.utils.Utils;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        binding.txtVersion.setText(BuildConfig.VERSION_NAME);

        binding.layoutWebsite.setOnClickListener(v -> {
            Toast.makeText(AboutActivity.this, "" + getString(R.string.under_dev), Toast.LENGTH_SHORT).show();
        });

        binding.layoutPrivacyPolicy.setOnClickListener(v -> {
            Intent i = new Intent(AboutActivity.this, WebviewActivity.class);
            i.putExtra("Key", getString(R.string.privacy_policy));
            i.putExtra("URL", "");
            i.putExtra("Title", getString(R.string.privacy_policy));
            startActivity(i);
        });

        binding.layoutEmail.setOnClickListener(v -> {
            String email = getString(R.string.email);
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] recipients = {email};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        });

        binding.layoutShareApp.setOnClickListener(v -> {
            Utils.ShareApp(AboutActivity.this);
        });

        binding.layoutRateApp.setOnClickListener(v -> {
            Utils.RateApp(AboutActivity.this);
        });

        binding.layoutReportError.setOnClickListener(v -> {
            Toast.makeText(AboutActivity.this, "" + getString(R.string.under_dev), Toast.LENGTH_SHORT).show();
        });
    }
}