package com.ameldev.tiktokprodownloader.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.databinding.ActivityMusicPlayerBinding;
import com.github.abdularis.buttonprogress.DownloadButtonProgress;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private ActivityMusicPlayerBinding binding;
    private int pos = -1;
    private String musicPath;

    private MediaPlayer mediaPlayer;
    private Uri music;

    private Timer myTimer = new Timer();

    private InterstitialAd mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music_player);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        pos = getIntent().getIntExtra("position", -1);
        musicPath = String.valueOf(MusicActivity.fileArrayList.get(pos));

        // MediaPlayer
        mediaPlayer = new MediaPlayer();
        music = Uri.parse(musicPath);
        // auto play
        readyToPlay();

        // filename
        binding.txtMusicName.setText(MusicActivity.fileArrayList.get(pos).getName());

        binding.buttonPlayer.addOnClickListener(new DownloadButtonProgress.OnClickListener() {
            @Override
            public void onIdleButtonClick(View view) {
                playOrpause();
            }

            @Override
            public void onCancelButtonClick(View view) {
                playOrpause();
            }

            @Override
            public void onFinishButtonClick(View view) {
                readyToPlay();
            }
        });

        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                MusicPlayerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            binding.mSeekBar.setProgress(musiccurrentposition());
                            binding.mSeekBar.setMax(mediaPlayer.getDuration());
                            //play_pause.setBackgroundResource(ic_baseline_pause_24);
                        } else {
                            //play_pause.setBackgroundResource(ic_baseline_play_arrow_24);
                        }
                    }
                });
            }
        }, 0, 500);
        binding.mSeekBar.setMax(mediaPlayer.getDuration());
        binding.mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        binding.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    //mediaPlayer.seekTo(progress * 1000);
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    //mediaPlayer.seekTo(progress * 1000);
                    binding.mSeekBar.setProgress(progress);
                }
            }
        });

        binding.visualizer.setColor(ContextCompat.getColor(this, R.color.black));
        binding.visualizer.setDensity(70);
        binding.visualizer.setPlayer(mediaPlayer.getAudioSessionId());

        // buildInterstitialAds
        buildInterstitialAds();
    }

    private void buildInterstitialAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, getString(R.string.admob_interstitial_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitial = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitial = null;
                    }
                });
    }

    private int musiccurrentposition() {
        int t = 0;
        if (mediaPlayer.isPlaying()) {
            t = mediaPlayer.getCurrentPosition();
        }
        return t;
    }

    private void playOrpause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                binding.buttonPlayer.setIdle();
            } else {
                mediaPlayer.getDuration();
                mediaPlayer.start();
                binding.buttonPlayer.setIndeterminate();
            }
        }
    }

    private void readyToPlay() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setOnCompletionListener(MusicPlayerActivity.this);
                mediaPlayer.setDataSource(MusicPlayerActivity.this, music);//Write your location here
                mediaPlayer.prepare();
                mediaPlayer.start();
                binding.buttonPlayer.setIndeterminate();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayer != null) {
            mp.stop();
            mp.reset();
            binding.buttonPlayer.setFinish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            if (isFinishing()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mInterstitial != null) {
            mInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    finish();
                }

                @Override
                public void onAdShowedFullScreenContent() {

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    finish();
                }
            });
            mInterstitial.show(MusicPlayerActivity.this);
        } else {
            super.onBackPressed();
        }
    }
}
