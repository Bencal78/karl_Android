package com.example.karl.karl.model;

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
    private ArrayList<Integer> bodyparts;
    @SerializedName("colors")
    private ArrayList<String> colors;
    @SerializedName("fabrics")
    private String fabrics;
    @SerializedName("pattern")
    private String pattern;
    @SerializedName("temperature")
    private Number temperature;
    @SerializedName("layer")
    private Number layer;

    public Clothe(String id, String name, String category, ArrayList<String> colors, String fabrics, String pattern, Number temperature, Number layer, ArrayList<Integer> bodyparts){
        this.id = id;
        this.name = name;
        this.category = category;
        this.colors = colors;
        this.fabrics = fabrics;
        this.pattern = pattern;
        this.temperature = temperature;
        this.layer = layer;
        this.bodyparts = bodyparts;
    }

    public Clothe(){
        this.id = null;
        this.name = null;
        this.category = null;
        this.colors = null;
        this.fabrics = null;
        this.pattern = null;
        this.temperature = null;
        this.layer = null;
        this.bodyparts = null;
    }

    public Clothe(JSONObject obj){
        try {
            if(obj.getString("_id") != null){
                this.id = obj.getString("_id");
            }
            if(obj.getString("name") != null){
                this.name = obj.getString("name");
            }
            if(obj.getString("category") != null){
                this.category = obj.getString("category");
            }
            if (obj.getJSONArray("bodyparts") != null) {
                int len = obj.getJSONArray("bodyparts").length();
                for (int i=0;i<len;i++){
                    this.bodyparts.add(parseInt(obj.getJSONArray("bodyparts").get(i).toString()));
                }
            }
            if (obj.getJSONArray("colors") != null) {
                int len = obj.getJSONArray("colors").length();
                for (int i=0;i<len;i++){
                    this.colors.add(obj.getJSONArray("colors").get(i).toString());
                }
            }
            if(obj.getString("fabrics") != null){
                this.fabrics = obj.getString("fabrics");
            }
            if(obj.getString("pattern") != null){
                this.pattern = obj.getString("pattern");
            }
            if(String.valueOf(obj.getInt("temperature")) != null){
                this.temperature = obj.getInt("temperature");
            }
            if(String.valueOf(obj.getInt("layer")) != null){
                this.layer = obj.getInt("layer");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    public ArrayList<Integer> getBodyparts() {
        return bodyparts;
    }

    public void setBodyparts(ArrayList<Integer> bodyparts) {
        this.bodyparts = bodyparts;
    }
}
