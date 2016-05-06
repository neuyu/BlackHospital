package com.neu.blackheartedhospital.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.google.gson.Gson;
import com.neu.blackheartedhospital.pojo.HospitalArea;
import com.neu.blackheartedhospital.pojo.HospitalGeo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neu on 16/5/3.
 */
public class Utility {

    public static String getProcessName(Context cxt,int pid){
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
        if (runningAppProcessInfos == null){
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos){
            if (processInfo.pid == pid){
                return processInfo.processName;
            }
        }
        return null;
    }

    public static String getAppName(Context context){
        if (context != null){
            return context.getPackageName();
        }
        return null;
    }

    public static <T> T parserJsonByGson(String jsonString,Class<T> className){
        try {
            T object = new Gson().fromJson(jsonString,className);
            return object;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getStringFromAssets(Context context, String name) {
        InputStream in = null;
        try {
            in = context.getResources().getAssets().open(name);
            byte[] arrayOfByte = new byte[in.available()];
            in.read(arrayOfByte);
            return new String(arrayOfByte, "UTF-8");
        } catch (IOException e) {
            return null;
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static List<HospitalArea> parserHospital(String json){

        JSONArray jsonList;

        List<HospitalArea> mList = null;

        try {
            jsonList = new JSONArray(json);
            mList = new ArrayList<>();
            for (int i = 0; i < jsonList.length(); i++) {
                mList.add(Utility.parserJsonByGson(jsonList.get(i).toString(),HospitalArea.class));
            }
            return mList;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return mList;
    }
    public static List<HospitalGeo> parserGeo(String json){
        List<HospitalGeo> list = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(json);
            JSONArray array = response.optJSONArray("features");
            if (array != null){
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    list.add(gson.fromJson(array.get(i).toString(),HospitalGeo.class));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
