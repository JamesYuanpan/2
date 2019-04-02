package com.yp;

import android.content.Context;
import android.media.Image;
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

public class SongerAdapter extends ArrayAdapter<Songer> {

    private int resourceId;

    public SongerAdapter(Context context, int textViewResourceId, List<Songer> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Songer songer = getItem(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent,false);

        ImageView imageView = (ImageView) view.findViewById(R.id.songer_image);
        TextView textView = (TextView) view.findViewById(R.id.songer_text);

        Picasso.with(parent.getContext()).load(songer.getImageUrl()).resize(160,0).into(imageView);
        textView.setText(songer.getName());

        return view;
    }
}
