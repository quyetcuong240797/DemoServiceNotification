package com.example.demoservicenotification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private ArrayList<Song> arrayListSongs;
    private  OnItemClickListener mListener;
    private LayoutInflater mLayoutInflater;

    public SongAdapter(ArrayList<Song> arrayListSongs, OnItemClickListener mListener, Context context) {
        this.arrayListSongs = arrayListSongs;
        this.mListener = mListener;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public SongAdapter(ArrayList<Song> songs, OnItemClickListener listener) {
        arrayListSongs = songs;
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_song, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(arrayListSongs.get(i),mListener);
    }

    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNameSong,tvNameArtis;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSong = itemView.findViewById(R.id.tv_name_song);
            tvNameArtis = itemView.findViewById(R.id.tv_name_artis);
            itemView.setOnClickListener(this);

        }

        public void setData(final Song song, final OnItemClickListener listener) {
            tvNameArtis.setText(song.getmArtisSong());
            tvNameSong.setText(song.getmNameSong());
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v,this.getLayoutPosition());
        }
    }
}
