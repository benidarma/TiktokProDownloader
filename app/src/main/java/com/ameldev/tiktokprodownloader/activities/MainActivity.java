package com.ameldev.tiktokprodownloader.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jaeger.library.StatusBarUtil;
import com.permissionx.guolindev.PermissionX;

import java.util.Objects;

import static com.ameldev.tiktokprodownloader.utils.Utils.RootDirectoryMusic;
import static com.ameldev.tiktokprodownloader.utils.Utils.RootDirectoryVideo;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    private Handler handler;
    private Bundle extras;

    private String sharedUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // ads initialize
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d("initialize ads", initializationStatus.toString());
            }
        });

        // handle layout splash gone in 5 second
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                showBannerAds();
                binding.layoutSplash.setVisibility(View.GONE);
                binding.layoutMain.setVisibility(View.VISIBLE);
            }
        }, 5000);

        // check string from intent
        extras = getIntent().getExtras();
        if (extras != null) {
            sharedUrl = extras.getString(Intent.EXTRA_TEXT);
            if (sharedUrl.contains("tiktok.com")) {
                checkStoragePermission(DownloadActivity.class, sharedUrl);
            }
        } else {
            sharedUrl = "";
        }

        // set open tiktok app or another
        binding.iconApp.setOnClickListener(v -> {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                startActivity(launchIntent1);
            } else {
                Toast.makeText(MainActivity.this, R.string.tiktok_app_not_available, Toast.LENGTH_SHORT).show();
            }
        });

        // open webview with tiktok url
        binding.openWeb.setOnClickListener(v -> openActivity(TiktokWebActivity.class, sharedUrl));

        // open video list
        binding.openVideo.setOnClickListener(v -> checkStoragePermission(VideoActivity.class, sharedUrl));

        // open downloader
        binding.openDownload.setOnClickListener(v -> checkStoragePermission(DownloadActivity.class, sharedUrl));

        // open music
        binding.openMusic.setOnClickListener(v -> checkStoragePermission(MusicActivity.class, sharedUrl));

        // open settings
        binding.openSettings.setOnClickListener(v -> checkStoragePermission(AboutActivity.class, sharedUrl));
    }

    private void showBannerAds() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("") //ca-app-pub-3940256099942544/6300978111
                .build();
        binding.adView.loadAd(adRequest);
    }

    private void checkStoragePermission(Class<?> classOf, String someString) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) ==
                        PackageManager.PERMISSION_GRANTED) {
            openActivity(classOf, someString);
        } else {
            PermissionX.init(MainActivity.this)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO)
                    .explainReasonBeforeRequest()
                    .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
//                    CustomDialog customDialog = new CustomDialog(MainActivity.this, "PermissionX needs following permissions to continue", deniedList);
//                    scope.showRequestReasonDialog(customDialog);
                        scope.showRequestReasonDialog(deniedList, getString(R.string.app_name) + " " +
                                getString(R.string.notif_need_permission), getString(R.string.allow));
                    })
                    .onForwardToSettings((scope, deniedList) -> {
                        scope.showForwardToSettingsDialog(deniedList, getString(R.string.please_allow), getString(R.string.allow));
                    })
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            //Toast.makeText(MainActivity.this, "All permissions are granted", Toast.LENGTH_SHORT).show();
                            openActivity(classOf, someString);
                        } else {
                            Toast.makeText(MainActivity.this, "" + getString(R.string.permissions_are_denied) +
                                    deniedList, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void openActivity(Class<?> classOf, String someString) {
        if (!RootDirectoryVideo.exists()) {
            RootDirectoryVideo.mkdirs();
        }
        if (!RootDirectoryMusic.exists()) {
            RootDirectoryMusic.mkdirs();
        }
        Intent intent = new Intent(getApplicationContext(), classOf);
        intent.putExtra("CopyIntent", someString);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}