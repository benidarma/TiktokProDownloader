package com.ameldev.tiktokprodownloader.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ameldev.tiktokprodownloader.R;
import com.ameldev.tiktokprodownloader.activities.MusicActivity;
import com.ameldev.tiktokprodownloader.activities.MusicPlayerActivity;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {

    private Context context;
    private ArrayList<File> musicArrayList;

    public MusicAdapter(Context context, ArrayList<File> musicArrayList) {
        this.context = context;
        this.musicArrayList = musicArrayList;
    }

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_item, viewGroup, false);
        return new MusicHolder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicHolder musicHolder, int i) {

        musicHolder.txtFileName.setText(MusicActivity.fileArrayList.get(i).getName());
        //Bitmap bitmapThumbnail = ThumbnailUtils.createAudioThumbnail(musicArrayList.get(i).getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
        //musicHolder.imageThumbnail.setImageBitmap(bitmapThumbnail);

        musicHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("position", musicHolder.getAdapterPosition());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (musicArrayList.size() > 0) {
            return musicArrayList.size();
        } else
            return 1;
    }
}
