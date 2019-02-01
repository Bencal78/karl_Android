package com.example.karl.karl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karl.karl.R;
import com.example.karl.karl.model.ClotheImage;
import com.example.karl.karl.activity.ClotheList;
import com.example.karl.karl.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClotheAdapter extends RecyclerView.Adapter {
    //Declare GalleryItems List
    List<ClotheImage> galleryItems;
    Context context;
    //Declare GalleryAdapterCallBacks
    GalleryAdapterCallBacks mAdapterCallBacks;
    private Boolean isOnLongPressed = false;

    public ClotheAdapter(Context context) {
        this.context = context;
        //get GalleryAdapterCallBacks from contex
        this.mAdapterCallBacks = (GalleryAdapterCallBacks) context;
        //Initialize GalleryItem List
        this.galleryItems = new ArrayList<>();
    }

    //This method will take care of adding new Gallery items to RecyclerView
    public void addGalleryItems(List<ClotheImage> galleryItems) {
        int previousSize = this.galleryItems.size();
        this.galleryItems.addAll(galleryItems);
        notifyItemRangeInserted(previousSize, galleryItems.size());

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_clothe, parent, false);
        return new GalleryItemHolder(row);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //get current Gallery Item
        final ClotheImage currentItem = galleryItems.get(position);
        //Create file to load with Picasso lib
        //File imageViewThoumb = new File(currentItem.imageUri);
        //cast holder with gallery holder
        final GalleryItemHolder galleryItemHolder = (GalleryItemHolder) holder;
        galleryItemHolder.chkBox.setOnCheckedChangeListener(null);
        galleryItemHolder.chkBox.setChecked(currentItem.isSelected);
        currentItem.checkBox = galleryItemHolder.chkBox;

        //Load with Picasso
        Picasso.with(context)
                .load(currentItem.imageUri)
                .resize(ScreenUtils.getScreenWidth(context) / 2, ScreenUtils.getScreenHeight(context) / 3)//Resize image to width half of screen and height 1/3 of screen height
                .into(galleryItemHolder.imageViewThumbnail);
        //set name of Image
        galleryItemHolder.textViewImageName.setText(currentItem.imageName);

        galleryItemHolder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call onItemSelected method and pass the position and let activity decide what to do when item selected
                mAdapterCallBacks.onItemSelected(position);
            }
        });


        galleryItemHolder.chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //call onItemSelected method and pass the position and let activity decide what to do when item selected
                mAdapterCallBacks.onItemSelected(position);
            }
        });


        galleryItemHolder.imageViewThumbnail.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View pView) {
                // Do something when your hold starts here.
                isOnLongPressed = true;
                mAdapterCallBacks.onItemLongSelected(position);
                return true;
            }
        });

        galleryItemHolder.imageViewThumbnail.setOnTouchListener( new View.OnTouchListener() {

            @Override
            public boolean onTouch(View pView, MotionEvent pEvent) {
                pView.onTouchEvent(pEvent);
                // We're only interested in when the button is released.
                if (pEvent.getAction() == MotionEvent.ACTION_UP || pEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    // We're only interested in anything if our speak button is currently pressed.
                    if (isOnLongPressed) {
                        // Do something when the button is released.
                        isOnLongPressed = false;
                        mAdapterCallBacks.onReleaseItem(position);
                    }
                }
                return false;
            }
        });

        if(context instanceof ClotheList){
            currentItem.checkBox.setVisibility(View.INVISIBLE);
        }
        else{
            currentItem.checkBox.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class GalleryItemHolder extends RecyclerView.ViewHolder {
        public CheckBox chkBox;
        ImageView imageViewThumbnail;
        TextView textViewImageName;

        public GalleryItemHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewImageName = itemView.findViewById(R.id.textViewImageName);
            chkBox = itemView.findViewById(R.id.chkBox);
            //chkBox.setEnabled(false);
        }
    }

    //Interface for communication of Adapter and MainActivity
    public interface GalleryAdapterCallBacks {
        void onItemSelected(int position);
        void onReleaseItem(int position);
        void onItemLongSelected(int position);
    }
}
