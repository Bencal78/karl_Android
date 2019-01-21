package com.example.karl.karl.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Clothe {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("category")
    private String category;
    @SerializedName("bodyparts")
    private ArrayList<Integer> bodyparts = new ArrayList<>();
    @SerializedName("colors")
    private ArrayList<String> colors = new ArrayList<>();
    @SerializedName("fabrics")
    private String fabrics;
    @SerializedName("pattern")
    private String pattern;
    @SerializedName("temperature")
    private Number temperature;
    @SerializedName("layer")
    private Number layer;
    @SerializedName("rl_score")
    private Number rl_score;

    public Clothe(String id, String name, String category, ArrayList<String> colors, String fabrics, String pattern, Number temperature, Number layer, ArrayList<Integer> bodyparts, Number rl_score){
        this.id = id;
        this.name = name;
        this.category = category;
        this.colors = colors;
        this.fabrics = fabrics;
        this.pattern = pattern;
        this.temperature = temperature;
        this.layer = layer;
        this.bodyparts = bodyparts;
        this.rl_score = rl_score;
    }

    public Clothe(){
        this.id = null;
        this.name = null;
        this.category = null;
        this.colors = new ArrayList<>();
        this.fabrics = null;
        this.pattern = null;
        this.temperature = null;
        this.layer = null;
        this.bodyparts = new ArrayList<>();
        this.rl_score = null;
    }

    public Clothe(JSONObject obj){
        try {
            if(obj.has("_id")){
                this.id = obj.getString("_id");
            }
            if(obj.has("name")){
                this.name = obj.getString("name");
            }
            if(obj.has("category")){
                this.category = obj.getString("category");
            }
            if (obj.has("bodyparts")) {
                int len = obj.getJSONArray("bodyparts").length();
                for (int i=0;i<len;i++){
                    int tmp = parseInt(obj.getJSONArray("bodyparts").get(i).toString());
                    this.bodyparts.add(parseInt(obj.getJSONArray("bodyparts").get(i).toString()));
                }
            }
            if (obj.has("colors")) {
                int len = obj.getJSONArray("colors").length();
                for (int i=0;i<len;i++){
                    this.colors.add(obj.getJSONArray("colors").get(i).toString());
                }
            }
            if(obj.has("fabrics")){
                this.fabrics = obj.getString("fabrics");
            }
            if(obj.has("pattern")){
                this.pattern = obj.getString("pattern");
            }
            if(obj.has("temperature")){
                this.temperature = obj.getInt("temperature");
            }
            if(obj.has("layer")){
                this.layer = obj.getInt("layer");
            }
            if(obj.has("rl_score")){
                this.rl_score = obj.getInt("rl_score");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Clothe(Object obj){
        Clothe object = Clothe.class.cast(obj) ;
        if(object.getId() != null) {this.id = object.getId();}
        if(object.getName() != null) {this.name = object.getName();}
        if(object.getCategory() != null) {this.category = object.getCategory();}
        if(object.getColors() != null) {this.colors = object.getColors();}
        if(object.getFabrics() != null) {this.fabrics = object.getFabrics();}
        if(object.getPattern() != null) {this.pattern = object.getPattern();}
        if(object.getTemperature() != null) {this.temperature = object.getTemperature();}
        if(object.getLayer() != null) {this.layer = object.getLayer();}
        if(object.getRl_score() != null) {this.rl_score = object.getRl_score();}
        if(object.getBodyparts() != null) {this.bodyparts = object.getBodyparts();}
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList colors) {
        this.colors = colors;
    }

    public String getFabrics() {
        return fabrics;
    }

    public void setFabrics(String fabrics) {
        this.fabrics = fabrics;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Number getTemperature() {
        return temperature;
    }

    public void setTemperature(Number temperature) {
        this.temperature = temperature;
    }

    public Number getLayer() {
        return layer;
    }

    public void setLayer(Number layer) {
        this.layer = layer;
    }

    public Number getRl_score() {
        return rl_score;
    }

    public void setRl_score(Number rl_score) {
        this.rl_score = rl_score;
    }

    public ArrayList<Integer> getBodyparts() {
        return bodyparts;
    }

    public void setBodyparts(ArrayList<Integer> bodyparts) {
        this.bodyparts = bodyparts;
    }
}
