package com.example.karl.karl.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class User {
    @SerializedName("_id")
    private String id;
    @SerializedName("idGoogle")
    private String idGoogle;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("givenName")
    private String givenName;
    @SerializedName("email")
    private String email;
    @SerializedName("genre")
    private String genre;
    @SerializedName("tastes")
    private ArrayList<Taste> tastes = new ArrayList<>();
    @SerializedName("clothes")
    private ArrayList<Clothe> clothes = new ArrayList<>();
    @SerializedName("rl_cat_score")
    private JsonElement rl_cat_score;

    public User(){
        this.id = null;
        this.idGoogle = null;
        this.firstName = null;
        this.lastName = null;
        this.givenName = null;
        this.email = null;
        this.genre = null;
        this.tastes = new ArrayList<>();
        this.clothes = new ArrayList<>();
    }

    public User(String id, String idGoogle, String firstName, String lastName, String givenName, String email, String genre, ArrayList<Taste> tastes, ArrayList<Clothe> clothes, JsonElement rl_cat_score){
        this.id = id;
        this.idGoogle = idGoogle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.givenName = givenName;
        this.email = email;
        this.genre = genre;
        this.tastes = tastes;
        this.clothes = clothes;
        this.rl_cat_score = rl_cat_score;
    }

    public User(JSONObject obj){
        try {
            if(obj.has("_id")){
                this.id = obj.getString("_id");
            }
            if(obj.has("idGoogle")){
                this.idGoogle = obj.getString("idGoogle");
            }
            if(obj.has("firstName")){
                this.firstName = obj.getString("firstName");
            }
            if(obj.has("email")){
                this.email = obj.getString("email");
            }
            if (obj.has("tastes")) {
                int len = obj.getJSONArray("tastes").length();
                for (int i=0;i<len;i++){
                    this.tastes.add(new Taste(obj.getJSONArray("tastes").get(i)));
                }
            }
            if (obj.has("clothes")) {
                int len = obj.getJSONArray("clothes").length();
                for (int i=0;i<len;i++){
                    this.clothes.add(new Clothe(obj.getJSONArray("clothes").get(i)));
                }
            }
            if(obj.has("lastName")){
                this.lastName = obj.getString("lastName");
            }
            if(obj.has("givenName")){
                this.givenName = obj.getString("givenName");
            }
            if(obj.has("genre")){
                this.genre = obj.getString("genre");
            }
            if(obj.has("rl_cat_score")){
                Gson gson = new Gson();
                this.rl_cat_score = gson.fromJson(rl_cat_score.toString(), JsonElement.class);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(Object obj){
        User object = User.class.cast(obj) ;
        if(object.getId() != null) {this.id = object.getId();}
        if(object.getIdGoogle() != null) {this.idGoogle= object.getIdGoogle();}
        if(object.getFirstName() != null) {this.firstName = object.getFirstName();}
        if(object.getGivenName() != null) {this.givenName = object.getGivenName();}
        if(object.getEmail() != null) {this.email = object.getEmail();}
        if(object.getGenre() != null) {this.genre = object.getGenre();}
        if(object.getTastes() != null) {this.tastes = object.getTastes();}
        if(object.getClothes() != null) {this.clothes = object.getClothes();}
        if(object.getRl_cat_score() != null) {this.rl_cat_score = object.getRl_cat_score();}
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGoogle() {
        return idGoogle;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getEmail() {
        return email;
    }

    public String getGenre() {
        return genre;
    }

    public ArrayList<Taste> getTastes() {
        return tastes;
    }

    public void setTastes(ArrayList<Taste> tastes) {
        this.tastes = tastes;
    }

    public ArrayList<Clothe> getClothes() {
        return clothes;
    }

    public void setClothes(ArrayList<Clothe> clothes) {
        this.clothes = clothes;
    }

    public JsonElement getRl_cat_score(){return rl_cat_score;}

    public void setRl_cat_score(JsonElement rl_cat_score) {
        this.rl_cat_score = rl_cat_score;
    }
}
