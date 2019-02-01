package com.example.karl.karl.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karl.karl.R;
import com.example.karl.karl.activity.ClotheList;
import com.example.karl.karl.activity.SavedOutfits;
import com.example.karl.karl.model.SavedOutfitImage;
import com.example.karl.karl.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OutfitsAdapter extends RecyclerView.Adapter {
    private List<SavedOutfitImage> galleryItems;
    private Context context;
    private ArrayList<Boolean> already_loaded = new ArrayList<>();
    //Declare GalleryAdapterCallBacks
    /**
     * Override method On Item selected
     */
    private OutfitsAdapter.GalleryAdapterCallBacks mAdapterCallBacks;

    public OutfitsAdapter(Context context) {
        this.context = context;
        //get GalleryAdapterCallBacks from context
        this.mAdapterCallBacks = (OutfitsAdapter.GalleryAdapterCallBacks) context;
        //Initialize GalleryItem List
        this.galleryItems = new ArrayList<>();
    }

    //This method will take care of adding new Gallery items to RecyclerView
    public void addGalleryItems(List<SavedOutfitImage> galleryItems) {
        int previousSize = this.galleryItems.size();
        this.galleryItems.addAll(galleryItems);
        notifyItemRangeInserted(previousSize, galleryItems.size());
        already_loaded = new ArrayList<>();
        for(int i=0; i<galleryItems.size(); i++){
            already_loaded.add(false);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_outfit, parent, false);
        return new OutfitsAdapter.GalleryItemHolder(row);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //get current Gallery Item
        final SavedOutfitImage currentItem = galleryItems.get(position);
        //Create file to load with Picasso lib
        //File imageViewThoumb = new File(currentItem.imageUri);
        //cast holder with gallery holder
        final OutfitsAdapter.GalleryItemHolder galleryItemHolder = (OutfitsAdapter.GalleryItemHolder) holder;
        galleryItemHolder.chkBox.setOnCheckedChangeListener(null);
        galleryItemHolder.chkBox.setChecked(currentItem.isSelected);
        currentItem.checkBox = galleryItemHolder.chkBox;

        //Load with Picasso
        switch (currentItem.imageUri.size()){
            case 0:
                break;
            case 1:
                //Load with Picasso
                Picasso.with(context)
                        .load(currentItem.imageUri.get(0))
                        .into(galleryItemHolder.outfit1ImageView);

                Picasso.with(context)
                        .load((Uri) null)
                        .into(galleryItemHolder.outfit2ImageView);

                Picasso.with(context)
                        .load((Uri) null)
                        .into(galleryItemHolder.outfit3ImageView);

                Picasso.with(context)
                        .load((Uri) null)
                        .into(galleryItemHolder.outfit4ImageView);

                break;
            case 2:
                //Load with Picasso
                Picasso.with(context)
                        .load(currentItem.imageUri.get(0))
                        .into(galleryItemHolder.outfit1ImageView);

                Picasso.with(context)
                        .load(currentItem.imageUri.get(1))
                        .into(galleryItemHolder.outfit2ImageView);

                Picasso.with(context)
                        .load((Uri) null)
                        .into(galleryItemHolder.outfit3ImageView);

                Picasso.with(context)
                        .load((Uri) null)
                        .into(galleryItemHolder.outfit4ImageView);
                break;
            case 3:
                //Load with Picasso
                Picasso.with(context)
                        .load(currentItem.imageUri.get(0))
                        .into(galleryItemHolder.outfit1ImageView);

                Picasso.with(context)
                        .load(currentItem.imageUri.get(1))
                        .into(galleryItemHolder.outfit2ImageView);

                Picasso.with(context)
                        .load(currentItem.imageUri.get(2))
                        .into(galleryItemHolder.outfit3ImageView);

                Picasso.with(context)
                        .load((Uri) null)
                        .into(galleryItemHolder.outfit4ImageView);
                break;
            case 4:
                //Load with Picasso
                Picasso.with(context)
                        .load(currentItem.imageUri.get(0))
                        .into(galleryItemHolder.outfit1ImageView);

                Picasso.with(context)
                        .load(currentItem.imageUri.get(1))
                        .into(galleryItemHolder.outfit2ImageView);

                Picasso.with(context)
                        .load(currentItem.imageUri.get(2))
                        .into(galleryItemHolder.outfit3ImageView);

                Picasso.with(context)
                        .load(currentItem.imageUri.get(3))
                        .into(galleryItemHolder.outfit4ImageView);
                break;
        }
        already_loaded.set(position, true);
        //set name of Image
        //galleryItemHolder.textViewImageName.setText(currentItem.imageName);
        //set on click listener on imageViewThumbnail
        galleryItemHolder.cardViewThumbnail.setOnClickListener(new View.OnClickListener() {
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

        if(context instanceof SavedOutfits){
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
        CardView cardViewThumbnail;
        //TextView textViewImageName;
        ImageView outfit1ImageView;
        ImageView outfit2ImageView;
        ImageView outfit3ImageView;
        ImageView outfit4ImageView;

        public GalleryItemHolder(View itemView) {
            super(itemView);
            cardViewThumbnail = itemView.findViewById(R.id.cardViewThumbnail);
            //textViewImageName = itemView.findViewById(R.id.textViewImageName);
            outfit1ImageView = itemView.findViewById(R.id.Outfit1ImageView);
            outfit2ImageView = itemView.findViewById(R.id.Outfit2ImageView);
            outfit3ImageView = itemView.findViewById(R.id.Outfit3ImageView);
            outfit4ImageView = itemView.findViewById(R.id.Outfit4ImageView);
            chkBox = itemView.findViewById(R.id.chkBox);
            //chkBox.setEnabled(false);
        }
    }

    //Interface for communication of Adapter and MainActivity
    public interface GalleryAdapterCallBacks {
        void onItemSelected(int position);
    }

}
