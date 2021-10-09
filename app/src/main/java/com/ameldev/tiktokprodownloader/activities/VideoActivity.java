package com.ameldev.tiktokprodownloader.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.adapter.VideoAdapter;
import com.ameldev.tiktokprodownloader.databinding.ActivityVideoBinding;
import com.google.android.gms.ads.AdRequest;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.ameldev.tiktokprodownloader.utils.Utils.RootDirectoryVideo;

public class VideoActivity extends AppCompatActivity {

    private ActivityVideoBinding binding;
    private VideoAdapter videoAdapter;
    public static ArrayList<File> fileArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_0054BB), 0);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // GridLayout
        GridLayoutManager manager = new GridLayoutManager(VideoActivity.this, 2);
        binding.listVideoRecycler.setLayoutManager(manager);

        // check permission for to continue read file video in Specific folder
        if ((ContextCompat.checkSelfPermission(VideoActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            fileArrayList.clear();
            getFile(RootDirectoryVideo);
            if (fileArrayList.size() > 0) {
                videoAdapter = new VideoAdapter(VideoActivity.this, fileArrayList);
                binding.listVideoRecycler.setAdapter(videoAdapter);
            } else {
                binding.layoutNotFound.setVisibility(View.VISIBLE);
                binding.listVideoRecycler.setVisibility(View.GONE);
            }

        } else {
            // show something if not have permission
        }

        showBannerAds();
    }

    private void showBannerAds() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("") //ca-app-pub-3940256099942544/6300978111
                .build();
        binding.adView.loadAd(adRequest);
    }

    public ArrayList<File> getFile(File directory) {
        File listFile[] = directory.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getFile(listFile[i]);
                } else {
                    if (listFile[i].getName().endsWith(".mp4")) {
                        for (int j = 0; j < fileArrayList.size(); j++) {
                            if (fileArrayList.get(j).getName().equals(listFile[i].getName())) {

                            } else {

                            }
                        }
                        fileArrayList.add(listFile[i]);
                    }
                }
            }
        }
        return fileArrayList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
