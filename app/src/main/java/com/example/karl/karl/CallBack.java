package com.example.karl.karl;

/**
 * Created by Edouard on 23/10/2018.
 */

public interface CallBack {
    public void onProgress();
    public void onResult(String result);
    public void onCancel();
}
