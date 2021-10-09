package com.ameldev.tiktokprodownloader.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ameldev.tiktokprodownloader.R;

class VideoHolder extends RecyclerView.ViewHolder {

    TextView txtFileName;
    ImageView imageThumbnail;
    CardView mCardView;

    VideoHolder(View view) {
        super(view);

        txtFileName = view.findViewById(R.id.txt_video_FileName);
        imageThumbnail = view.findViewById(R.id.iv_thmnail);
        mCardView = view.findViewById(R.id.my_Card_view);
    }
}
