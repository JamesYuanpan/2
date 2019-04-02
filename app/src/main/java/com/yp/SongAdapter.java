package com.yp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {
    private int resoucre;

    public SongAdapter(Context context, int resource,List<Song> objects) {
        super(context, resource, objects);
        this.resoucre = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(resoucre,parent,false);

        Song song = getItem(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.song_image);
        TextView songerText = (TextView) view.findViewById(R.id.text_songer);
        TextView songText = (TextView) view.findViewById(R.id.text_song);

        songerText.setText(song.getSongerName());
        songText.setText(song.getSongName());
        Picasso.with(parent.getContext()).load(song.getImageUrl()).resize(160,0).into(imageView);

        return view;

    }
}
