package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rohithreddy on 09/07/17.
 */

public class getpincodes {
    ArrayList<String>  formList = new ArrayList<String>() ;
   ArrayList<String> pinlist = new ArrayList<String>();
   Context mContext;

    public getpincodes(Context context) {
        this.mContext = context;
        something();
    }
public ArrayList<String>  getformlist(){
    return formList;
}
public ArrayList<String> getPinlist(){
        return pinlist;

    }
    public  int something() {
        int resID=0;
        String json = null;
        try {
            String pkgName = mContext.getPackageName();
            resID = mContext.getResources().getIdentifier("pincode", "raw", pkgName);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        try {
            InputStream is = mContext.getResources().openRawResource(resID);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
       // System.out.print(json);
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry= obj.getJSONArray("pincodes");

            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String id = jo_inside.getString("id");
                String name = jo_inside.getString("name");
                String pincode = jo_inside.getString("pincode");
                String pin=pincode+"("+name+")";
                String pinrev=name+"("+pincode+")";
                m_li = new HashMap<String, String>();
                m_li.put(pin, id);
                pinlist.add(pin);
                pinlist.add(pinrev);
                formList.add(id);
                formList.add(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
return 1;
    }
}

