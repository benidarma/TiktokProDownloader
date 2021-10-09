package com.ameldev.tiktokprodownloader.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.api.CommonClassForAPI;
import com.ameldev.tiktokprodownloader.databinding.ActivityDownloadBinding;
import com.ameldev.tiktokprodownloader.databinding.DialogResultNullBinding;
import com.ameldev.tiktokprodownloader.databinding.DialogResultsDownloadBinding;
import com.ameldev.tiktokprodownloader.models.Tiktok;
import com.ameldev.tiktokprodownloader.utils.AesCipher;
import com.ameldev.tiktokprodownloader.utils.Keys;
import com.github.abdularis.buttonprogress.DownloadButtonProgress;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.ameldev.tiktokprodownloader.utils.Utils.RootDirectoryMusicDown;
import static com.ameldev.tiktokprodownloader.utils.Utils.RootDirectoryVideoDown;
import static com.ameldev.tiktokprodownloader.utils.Utils.startDownload;

public class DownloadActivity extends AppCompatActivity {

    private DownloadActivity activity;
    private ActivityDownloadBinding binding;

    private ClipboardManager clipBoard;

    private CommonClassForAPI commonClassForAPI;

    private boolean cancelAction = false;
    private boolean noAction = false;
    private boolean adsAction = false;
    private boolean adsClose = false;
    private boolean adsReward = false;
    private boolean adsShowError = false;
    private boolean adsOpen = false;

    private RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);

        // hide toolbar
        Objects.requireNonNull(getSupportActionBar()).hide();

        activity = this;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        commonClassForAPI = CommonClassForAPI.getInstance(activity);

        binding.imagePaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.imageClear.setOnClickListener(v -> {
            binding.editQuery.setText("");
        });

        binding.editQuery.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.editQuery.getText().toString().equals("")) {
                    binding.imagePaste.setVisibility(View.VISIBLE);
                    binding.imageClear.setVisibility(View.GONE);
                } else {
                    binding.imagePaste.setVisibility(View.GONE);
                    binding.imageClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.buttonProgress2.addOnClickListener(new DownloadButtonProgress.OnClickListener() {
            @Override
            public void onIdleButtonClick(View view) {
                // called when download button/icon is clicked

                if (!binding.editQuery.getText().toString().equals("") &&
                        binding.editQuery.getText().toString().contains("tiktok.com")) {

                    binding.buttonProgress2.setIndeterminate();

                    adsAction = true;
                    processDownloadWithAds();

                } else {

                }
            }

            @Override
            public void onCancelButtonClick(View view) {
                // called when cancel button/icon is clicked
                binding.buttonProgress2.setIdle();
                //observer.dispose();
                cancelAction = true;
                Toast.makeText(DownloadActivity.this, getString(R.string.download_canceled), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinishButtonClick(View view) {
                // called when finish button/icon is clicked
            }
        });

        // ads
        buildRewardedAd();
    }

    private void processDownloadEnd() {
        if (commonClassForAPI != null) {
            String URL_Tiktok = "https://www.tiktok.com/@akusukapandaaa/video/7008812044791139611?sender_device=pc&sender_web_id=7010979019849336322&is_from_webapp=v1&is_copy_url=0";
            commonClassForAPI.CallTiktokApi(observer, Keys.getApiURL(), AesCipher.encrypt(Keys.getApiKey(),
                    binding.editQuery.getText().toString()).getData());
        } else {

        }
    }

    private void processDownloadWithAds() {
        if (rewardedAd.isLoaded()) {
            Activity activityContext = DownloadActivity.this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                    adsOpen = true;
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                    adsClose = true;
                    buildRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                    adsReward = true;
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                    adsShowError = true;
                }
            };
            rewardedAd.show(activityContext, adCallback);

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (adsShowError || adsReward) {
                        adsShowError = false;
                        adsReward = false;
                        adsClose = false;
                        adsOpen = false;
                        adsAction = false;
                        processDownloadEnd();
                    } else if (adsClose) {
                        adsShowError = false;
                        adsReward = false;
                        adsOpen = false;
                        adsClose = false;
                        adsAction = false;
                        showDialogAdsCanceled(DownloadActivity.this);
                    } else {
                        handler.postDelayed(this, 500);
                    }
                }
            };
            //Start
            handler.postDelayed(runnable, 500);

        } else {
            buildRewardedAd();
        }
    }

    private void buildRewardedAd() {
        rewardedAd = new RewardedAd(DownloadActivity.this, getString(R.string.admob_rewarded_id));
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
                if (adsAction) {
                    if (adsOpen)
                        adsAction = false;
                    processDownloadWithAds();
                }
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
                processDownloadEnd();
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    @SuppressLint("SetTextI18n")
    private void showDialogAdsCanceled(Context context) {
        Dialog dialogDialogResultNull = new Dialog(context);
        DialogResultNullBinding bindingNull = DialogResultNullBinding.inflate(LayoutInflater.from(context));
        dialogDialogResultNull.setContentView(bindingNull.getRoot());
        dialogDialogResultNull.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDialogResultNull.setCancelable(false);
        dialogDialogResultNull.setCanceledOnTouchOutside(false);

        bindingNull.cardOne.setCardBackgroundColor(Color.RED);
        bindingNull.txtTitle.setText("Ads canceled");
        bindingNull.txtMsg.setText("Download failed, please try again.\n" +
                "Make sure the ad is not skipped, thank you.");
        bindingNull.btnOke.setTextColor(Color.RED);
        bindingNull.btnOke.setOnClickListener(v -> {
            binding.buttonProgress2.setIdle();
            dialogDialogResultNull.dismiss();
        });

        dialogDialogResultNull.show();
    }

    private DisposableObserver<Tiktok> observer = new DisposableObserver<Tiktok>() {

        @Override
        public void onNext(Tiktok tiktok) {
            if (!cancelAction) {
                if (tiktok.getCode().equals("200")) {
                    showDialogResultsDownload(DownloadActivity.this, tiktok);
                } else {
                    showDialogResultNull(DownloadActivity.this, tiktok);
                }

                //Log.d("DataMain", tiktok.toString());
            }
        }

        @Override
        public void onError(Throwable e) {
            binding.buttonProgress2.setIdle();
            e.printStackTrace();
            Log.e("View onError", e.getMessage());
        }

        @Override
        public void onComplete() {
            if (!cancelAction)
                binding.buttonProgress2.setFinish();
            else
                cancelAction = false;
        }
    };

    @SuppressLint("SetTextI18n")
    private void showDialogResultNull(Context context, Tiktok tiktok) {
        Dialog dialogDialogResultNull = new Dialog(context);
        DialogResultNullBinding bindingNull = DialogResultNullBinding.inflate(LayoutInflater.from(context));
        dialogDialogResultNull.setContentView(bindingNull.getRoot());
        dialogDialogResultNull.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDialogResultNull.setCancelable(false);
        dialogDialogResultNull.setCanceledOnTouchOutside(false);

        bindingNull.txtTitle.setText(getString(R.string.error_code) + " " + tiktok.getCode());
        bindingNull.txtMsg.setText(tiktok.getMessage());
        bindingNull.btnOke.setOnClickListener(v -> {
            binding.buttonProgress2.setIdle();
            dialogDialogResultNull.dismiss();
        });

        dialogDialogResultNull.show();
    }

    @SuppressLint("SetTextI18n")
    public void showDialogResultsDownload(Context context, Tiktok tiktok) {

        Dialog dialogResultsDownload = new Dialog(context);
        DialogResultsDownloadBinding bindingDialog = DialogResultsDownloadBinding.inflate(LayoutInflater.from(context));
        dialogResultsDownload.setContentView(bindingDialog.getRoot());
        dialogResultsDownload.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogResultsDownload.setCancelable(false);
        dialogResultsDownload.setCanceledOnTouchOutside(false);

        bindingDialog.webPosterImage.getSettings().setLoadWithOverviewMode(true);
        bindingDialog.webPosterImage.getSettings().setUseWideViewPort(true);
        bindingDialog.webPosterImage.loadUrl(tiktok.getThumbnail());

        bindingDialog.txtTitle.setText("" + tiktok.getTitle());
        bindingDialog.txtUsername.setText("" + tiktok.getUsername());
        bindingDialog.txtNickname.setText("" + tiktok.getNickname());
        bindingDialog.txtMusic.setText("" + tiktok.getMusic());

        bindingDialog.btnCancel.setOnClickListener(v -> {
            binding.buttonProgress2.setIdle();
            if (noAction) {
                binding.editQuery.setText("");
                noAction = false;
            }
            dialogResultsDownload.cancel();
            dialogResultsDownload.dismiss();
        });

        bindingDialog.btnDownVideo.setOnClickListener(v -> {
            noAction = true;
            bindingDialog.btnCancel.setText(R.string.close);
            String str = cutTitle(tiktok.getTitle());
            startDownload(tiktok.getUrl(), RootDirectoryVideoDown, activity,
                    "" + str.replaceAll("[^A-Za-z]+", "-") + "-tpd.mp4");
        });

        bindingDialog.btnDownMusic.setOnClickListener(v -> {
            noAction = true;
            bindingDialog.btnCancel.setText(R.string.close);
            String str = cutTitle(tiktok.getMusic());
            startDownload(tiktok.getMusicUrl(), RootDirectoryMusicDown, activity,
                    "" + str.replaceAll("[^A-Za-z]+", "-") + "-tpd.mp3");
        });

        dialogResultsDownload.show();
    }

    private String cutTitle(String str_) {
        String str;
        if (str_.length() > 25) {
            str = str_.substring(0, 25);
        } else {
            str = str_;
        }
        return str;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void PasteText() {
        try {
            binding.editQuery.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent == null || CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok")) {
                        binding.editQuery.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok")) {
                        binding.editQuery.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("tiktok")) {
                    binding.editQuery.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // add error paste or getStringExtra
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
