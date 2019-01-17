package com.example.karl.karl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.karl.karl.DownLoadImageTask;
import com.example.karl.karl.R;

import java.util.ArrayList;
import java.util.List;

public class OotdAdapter extends BaseAdapter {

    Context context;
    private final String[] images;
    private LayoutInflater layoutInflater;
    View view;


    public OotdAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images.toArray(new String[0]);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){

            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            new DownLoadImageTask(imageView).execute(String.valueOf(images[position]));

        }
        return view;
    }
}
