package com.example.karl.karl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class HTTPAsyncTask extends AsyncTask<String, Void, String> {
    private CallBack mCb;
    LinkedHashMap<Object, Object> mData = null;
    //List mParams= new ArrayList();
    LinkedHashMap<Object, Object> mParams = new LinkedHashMap<>();
    String mTypeOfRequest;
    String mStrToBeAppended = "?";
    boolean isPostDataInJSONFormat = false;
    JSONObject mJSONPostData = null;
    Context mContext = null;

    public HTTPAsyncTask(Context context, CallBack c, HashMap<Object, Object> data, JSONObject jsonObj, String request) {
        mContext = context;
        mCb = c;
        mTypeOfRequest = request;
        mJSONPostData = jsonObj;
        //Log.i("JSONDATA", mJSONPostData.toString());
        if((data != null) && (jsonObj == null)){
            mData = (LinkedHashMap)data;
            //GET
            if(mTypeOfRequest.equalsIgnoreCase("GET")){
                Object key = null;
                Iterator<Object> it = mData.keySet().iterator();
                while(it.hasNext()){
                    key = it.next();
                    mParams.put(key, mData.get(key));
                    Log.d("Data", key.toString() + " " + mData.get(key).toString());
                }
                Iterator<Object>itParams = mParams.keySet().iterator();
                int sizeOfParams = mParams.size();
                int index = 0;
                while(itParams.hasNext()){
                    Object keyParams = itParams.next();
                    index++;
                    if (index == sizeOfParams){
                        mStrToBeAppended+=  keyParams + "=" + mParams.get(keyParams);
                        break;
                    }
                    mStrToBeAppended+=  keyParams + "=" + mParams.get(keyParams)+ "&";
                }
            }
//POST
            if(mTypeOfRequest.equalsIgnoreCase("POST")){
                Object key = null;
                isPostDataInJSONFormat = false;
                Iterator<Object> it = mData.keySet().iterator();
                while(it.hasNext()){
                    key = it.next();
                    mParams.put(key, mData.get(key));
                }
            }
        }

//POST with Data in jSON format
        if ((mData == null) && (mJSONPostData != null) && (mTypeOfRequest.equalsIgnoreCase("POST") == true)){
            isPostDataInJSONFormat = true;
        }
    }

    @Override
    protected String doInBackground(String... baseUrls) {
        //android.os.Debug.waitForDebugger();
        publishProgress(null);
        if(mTypeOfRequest.equalsIgnoreCase("GET")){
            String finalURL = baseUrls[0]+ mStrToBeAppended;
            return HttpUtility.GET(finalURL);
        }

        if (mTypeOfRequest.equalsIgnoreCase("POST")){
            if(isPostDataInJSONFormat == false){
                return HttpUtility.POST(baseUrls[0],mParams );
            }
            if(isPostDataInJSONFormat == true){
                Log.i("JSONDATAPOSTMETHOd","JSON POST method to be called...");
                return HttpUtility.POST(baseUrls[0], mJSONPostData);
            }
        }
        return null;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        mCb.onResult(result);
    }

    @Override
    protected void onProgressUpdate(Void...voids ) {
        mCb.onProgress();
    }
}