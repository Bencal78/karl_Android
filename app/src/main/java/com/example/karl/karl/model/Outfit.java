package com.example.karl.karl.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Outfit {
    @SerializedName("outfit")
    private ArrayList<Clothe> outfit = new ArrayList<>();

    public Outfit(ArrayList<Clothe> outfit) {
        this.outfit = outfit;
    }

    public Outfit(){
        this.outfit = new ArrayList<>();
    }

    public void setOutfit(ArrayList<Clothe> outfit){
        this.outfit = outfit;
    }

    public ArrayList<Clothe> getOutfit(){
        return this.outfit;
    }
}
