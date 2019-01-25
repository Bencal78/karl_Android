package com.example.karl.karl.activity;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

public class ClotheImage {
    public String imageUri;
    public String imageName;
    public boolean isSelected = false;
    public CheckBox checkBox;

    public ClotheImage(String imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }
}
