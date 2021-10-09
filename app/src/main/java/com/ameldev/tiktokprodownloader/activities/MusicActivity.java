package com.ameldev.tiktokprodownloader.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.adapter.MusicAdapter;
import com.ameldev.tiktokprodownloader.databinding.ActivityMusicBinding;
import com.google.android.gms.ads.AdRequest;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.ameldev.tiktokprodownloader.utils.Utils.RootDirectoryMusic;

public class MusicActivity extends AppCompatActivity {

    private ActivityMusicBinding binding;
    private MusicAdapter musicAdapter;
    public static ArrayList<File> fileArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_0054BB), 0);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.listMusicRecycler.setLayoutManager(layoutManager);

        // check permission for to continue read file music in Specific folder
        if ((ContextCompat.checkSelfPermission(MusicActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {

            fileArrayList.clear();
            getFile(RootDirectoryMusic);
            if (fileArrayList.size() > 0) {
                musicAdapter = new MusicAdapter(MusicActivity.this, fileArrayList);
                binding.listMusicRecycler.setAdapter(musicAdapter);
            } else {
                binding.layoutNotFound.setVisibility(View.VISIBLE);
                binding.listMusicRecycler.setVisibility(View.GONE);
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
                    if (listFile[i].getName().endsWith(".mp3")) {
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

    ArrayList<HashMap<String, String>> getPlayList(String rootPath){
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        fileList.clear();
        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : Objects.requireNonNull(files)) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }
}
