package com.ameldev.tiktokprodownloader.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.databinding.ActivityVideoPlayerBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import static com.ameldev.tiktokprodownloader.utils.Utils.shareVideoToAnother;

public class VideoPlayerActivity extends AppCompatActivity {

    private ActivityVideoPlayerBinding binding;
    private int position = -1;
    private String videoPath;

    private Animation front_on, turn_back, fab_on, fab_off;
    private Boolean fabTouch = false;

    private InterstitialAd mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_player);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black), 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        position = getIntent().getIntExtra("position", -1);
        videoPath = String.valueOf(VideoActivity.fileArrayList.get(position));

        front_on = AnimationUtils.loadAnimation(this, R.anim.front_on);
        turn_back = AnimationUtils.loadAnimation(this, R.anim.turn_back);
        fab_on = AnimationUtils.loadAnimation(this, R.anim.fab_on);
        fab_off = AnimationUtils.loadAnimation(this, R.anim.fab_off);

        // https://github.com/mehmetaydintr/Animasyonlu_Floating_Action_Button
        binding.fabMainMenu.setOnClickListener(v -> {
            HideOrShow();
        });

        binding.fabAnother.setOnClickListener(v -> {
            shareVideoToAnother(VideoPlayerActivity.this, videoPath);
        });

        binding.fabWa.setOnClickListener(v -> {
            Uri uri = Uri.parse(videoPath);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("*/*");
            videoshare.setPackage("com.whatsapp");
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM,uri);

            startActivity(videoshare);
        });

        try {
            MediaController mediaController = new MediaController(VideoPlayerActivity.this);
            mediaController.setAnchorView(binding.videoView);
            Uri video = Uri.parse(videoPath);
            binding.videoView.setMediaController(mediaController);
            binding.videoView.setVideoURI(video);
            binding.videoView.start();
            binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });
            binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                }
            });
            mediaController.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.layoutFloatingButton.getLayoutParams();
                    params.bottomMargin = /*mediaController.getHeight() + 20*/ 252;
                    binding.layoutFloatingButton.setLayoutParams(params);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.layoutFloatingButton.getLayoutParams();
                    params.bottomMargin = 20;
                    binding.layoutFloatingButton.setLayoutParams(params);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        // buildInterstitialAds
        buildInterstitialAds();
    }

    private void buildInterstitialAds() {
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        mInterstitial.loadAd(new AdRequest.Builder()
                //.addTestDevice("") //ca-app-pub-3940256099942544/1033173712
                .build());
    }

    private void HideOrShow() {
        if (fabTouch) {
            binding.fabMainMenu.startAnimation(turn_back);
            binding.fabAnother.startAnimation(fab_off);
            binding.fabWa.startAnimation(fab_off);
            binding.fabAnother.setClickable(false);
            binding.fabWa.setClickable(false);
            fabTouch = false;
        } else {
            binding.fabMainMenu.startAnimation(front_on);
            binding.fabAnother.startAnimation(fab_on);
            binding.fabWa.startAnimation(fab_on);
            binding.fabAnother.setClickable(true);
            binding.fabWa.setClickable(true);
            fabTouch = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mInterstitial.isLoaded()) {
            mInterstitial.show();
            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
