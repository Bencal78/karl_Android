package com.example.karl.karl.model;

/**
 * Created by Lisa on 14/01/2019.
 */

public class Model {
    public String title, image;

    public Model(String title, String image){
        this.title = title;
        this.image = image;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getTitle(){
        return title;
    }

    public String getImage(){
        return image;
    }
}
