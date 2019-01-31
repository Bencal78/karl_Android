package com.example.karl.karl.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Taste {
    @SerializedName("_id")
    private String id;
    @SerializedName("decision")
    private Boolean decision;
    @SerializedName("clothes")
    private ArrayList<Clothe> clothes = new ArrayList<>();
    @SerializedName("rl_used")
    private Boolean rl_used = false;
    @SerializedName("nb_used")
    private Boolean nb_used = false;

    public Taste(){
        this.id = null;
        this.decision = null;
        this.clothes = new ArrayList<>();
    }

    public Taste(String id, Boolean decision, ArrayList<Clothe> clothes){
        this.id = id;
        this.decision = decision;
        this.clothes = clothes;
    }

    public Taste(JSONObject obj){
        try {
            if(obj.has("_id")){
                this.id = obj.getString("_id");
            }
            if(obj.has("decision")){
                this.decision = Boolean.valueOf(obj.getString("decision"));
            }
            if (obj.has("clothes")) {
                int len = obj.getJSONArray("bodyparts").length();
                for (int i=0;i<len;i++){
                    this.clothes.add(new Clothe(obj.getJSONArray("clothes").get(i)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Taste(Object obj){
        Taste object = Taste.class.cast(obj) ;
        if(object.getId() != null) {this.id = object.getId();}
        if(object.getDecision() != null) {this.decision = object.getDecision();}
        if(object.getClothes() != null) {this.clothes= object.getClothes();}
    }


    public ArrayList<Clothe> getClothes() {
        return this.clothes;
    }

    public void setClothes(ArrayList<Clothe> clothes) {
        this.clothes = clothes;
    }

    public Boolean getDecision(){
        return this.decision;
    }

    public void setDecision(Boolean decision) {
        this.decision = decision;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
