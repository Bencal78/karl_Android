package com.example.karl.karl.model;

import com.google.gson.annotations.SerializedName;

public class UserTaste {
    @SerializedName("_id")
    private String id;
    @SerializedName("tastes")
    private Taste tastes;

    public UserTaste(String id, Taste taste){
        this.id = id;
        this.tastes = taste;
    }
}
