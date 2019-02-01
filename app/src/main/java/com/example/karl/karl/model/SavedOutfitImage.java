package com.example.karl.karl.model;

import android.widget.CheckBox;

import java.util.ArrayList;

public class SavedOutfitImage {
    public ArrayList<String> imageUri;
    public String imageName;
    public boolean isSelected = false;
    public CheckBox checkBox;

    public SavedOutfitImage(ArrayList<String> imageUri, String imageName) {
        this.imageUri = imageUri;
        this.imageName = imageName;
    }
}
