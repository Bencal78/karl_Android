package com.example.karl.karl.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserClothe {
    @SerializedName("_id")
    private String id;
    @SerializedName("clothes")
    private ArrayList<Clothe> clothes;

    public UserClothe(String id, ArrayList<Clothe> clothes){
        this.id = id;
        this.clothes = clothes;
    }
}
