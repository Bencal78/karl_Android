package com.example.karl.karl.activity;

import android.annotation.SuppressLint;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.IOException;

@SuppressLint("Registered")
public class IdGlobal extends Ootd {

    private String Id;

    public IdGlobal() throws IOException {
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

}
