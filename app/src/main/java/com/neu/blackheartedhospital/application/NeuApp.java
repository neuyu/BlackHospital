package com.neu.blackheartedhospital.application;

import android.app.Application;
import android.os.AsyncTask;

import com.neu.blackheartedhospital.pojo.HospitalArea;
import com.neu.blackheartedhospital.pojo.HospitalGeo;
import com.neu.blackheartedhospital.utils.Utility;

import java.util.List;

/**
 * neu app
 * Created by neu on 16/5/3.
 */
public class NeuApp extends Application {

    private List<HospitalArea> mHospitalAreaList;
    private List<HospitalGeo> mHospitalGeos;
    public static NeuApp mNeuApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mNeuApp = this;
        boolean defaultProcess = false;
        String processName = Utility.getProcessName(this,android.os.Process.myPid());
        String packageName = Utility.getAppName(this);
        if (processName != null){
            defaultProcess = processName.equals(packageName);
        }

        if (defaultProcess){
            new GetHospitalNameTask().execute();
            new GetHospitalGeoLocationTask().execute();
        }
    }


    public List<HospitalArea> getHospitalAreaList() {
        return mHospitalAreaList;
    }

    public List<HospitalGeo> getHospitalGeos() {
        return mHospitalGeos;
    }

    private class GetHospitalNameTask extends AsyncTask<Void,Void,List<HospitalArea>>{
        @Override
        protected List<HospitalArea> doInBackground(Void... params) {
            String areaJson = Utility.getStringFromAssets(getApplicationContext(),"putian.json");
            return Utility.parserHospital(areaJson);
        }

        @Override
        protected void onPostExecute(List<HospitalArea> areas) {
            mHospitalAreaList = areas;
        }
    }

    private class GetHospitalGeoLocationTask extends AsyncTask<Void,Void,List<HospitalGeo>>{
        @Override
        protected List<HospitalGeo> doInBackground(Void... params) {

            String geoJson = Utility.getStringFromAssets(getApplicationContext(),"putian.geojson");

            return Utility.parserGeo(geoJson);
        }

        @Override
        protected void onPostExecute(List<HospitalGeo> geoList) {
            mHospitalGeos = geoList;
        }
      }
}
