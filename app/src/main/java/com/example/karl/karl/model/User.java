package com.example.karl.karl.model;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    private JSONArray tastes;
    @SerializedName("clothes")
    private JSONArray clothes;

    public User(){
        this.id = null;
        this.idGoogle = null;
        this.firstName = null;
        this.lastName = null;
        this.givenName = null;
        this.email = null;
        this.genre = null;
        this.tastes = null;
        this.clothes = null;
    }

    public User(String id, String idGoogle, String firstName, String lastName, String givenName, String email, String genre, JSONArray tastes, JSONArray clothes){
        this.id = id;
        this.idGoogle = idGoogle;
        this.firstName = firstName;
        this.lastName = lastName;
        this.givenName = givenName;
        this.email = email;
        this.genre = genre;
        this.tastes = tastes;
        this.clothes = clothes;
    }

    public User(JSONObject obj){
        try {
            if(obj.getString("_id") != null){
                this.id = obj.getString("_id");
            }
            if(obj.getString("idGoogle") != null){
                this.idGoogle = obj.getString("idGoogle");
            }
            if(obj.getString("firstName") != null){
                this.firstName = obj.getString("firstName");
            }
            if (obj.getJSONArray("tastes") != null) {
                this.tastes = obj.getJSONArray("tastes");
            }
            if (obj.getJSONArray("clothes") != null) {
                this.clothes = obj.getJSONArray("clothes");
            }
            if(obj.getString("lastName") != null){
                this.lastName = obj.getString("lastName");
            }
            if(obj.getString("givenName") != null){
                this.givenName = obj.getString("givenName");
            }
            if(obj.getString("genre") != null){
                this.genre = obj.getString("genre");
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

    public String getIdGoogle() {
        return idGoogle;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public JSONArray getTastes() {
        return tastes;
    }

    public void setTastes(JSONArray tastes) {
        this.tastes = tastes;
    }

    public JSONArray getClothes() {
        return clothes;
    }

    public void setClothes(JSONArray clothes) {
        this.clothes = clothes;
    }
}
