package com.example.karl.karl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Edouard on 20/09/2018.
 */

class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    CircleImageView imageView;
    ProgressBar spinner;
    DownloadImageFromInternet(CircleImageView imageView,ProgressBar spinner) {
        this.imageView = imageView;
        this.spinner = spinner;
    }


    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap bimage = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bimage = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bimage;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        spinner.setVisibility(View.GONE);
    }
}


