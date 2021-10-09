package com.ameldev.tiktokprodownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.activities.VideoActivity;
import com.ameldev.tiktokprodownloader.activities.VideoPlayerActivity;

import java.io.File;
import java.util.ArrayList;

public class VideoAdapter  extends RecyclerView.Adapter<VideoHolder> {

    private Context context;
    private ArrayList<File> videoArrayList;

    public VideoAdapter(Context context, ArrayList<File> videoArrayList) {
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_item, viewGroup, false);
        return new VideoHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoHolder videoHolder, int i) {

        videoHolder.txtFileName.setText(VideoActivity.fileArrayList.get(i).getName());
        Bitmap bitmapThumbnail = ThumbnailUtils.createVideoThumbnail(videoArrayList.get(i).getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        videoHolder.imageThumbnail.setImageBitmap(bitmapThumbnail);

        videoHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("position", videoHolder.getAdapterPosition());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (videoArrayList.size() > 0) {
            return videoArrayList.size();
        } else
            return 1;
    }
}
