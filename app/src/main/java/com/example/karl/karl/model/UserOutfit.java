package com.example.karl.karl.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserOutfit {
    @SerializedName("_id")
    private String id;
    @SerializedName("tastes")
    private ArrayList<Taste> tastes;

    public UserOutfit(String id, ArrayList<Taste> tastes){
        this.id = id;
        this.tastes = tastes;
    }
}
