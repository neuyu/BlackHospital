package com.neu.blackheartedhospital;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Gradient;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.TileOverlayOptions;
import com.neu.blackheartedhospital.adapter.BlackHeartAdapter;
import com.neu.blackheartedhospital.application.NeuApp;
import com.neu.blackheartedhospital.pojo.HospitalArea;
import com.neu.blackheartedhospital.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSource,AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private boolean isShowEditText;
    private AMapLocation mMapLocation;
    private List<String> mHospitalNameList = new ArrayList<>();

    private MaterialEditText mYourLocationEditText;
    private MaterialEditText mBlackHeartEditText;
    private ListView mListView;
    private Button mButton;
    private BlackHeartAdapter mBlackHeartAdapter;


    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),
            Color.argb(255 / 3 * 2, 0, 255, 0),
            Color.rgb(125, 191, 0),
            Color.rgb(185, 71, 0),
            Color.rgb(255, 0, 0)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = { 0.0f,
            0.10f, 0.20f, 0.60f, 1.0f };
    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(
            ALT_HEATMAP_GRADIENT_COLORS, ALT_HEATMAP_GRADIENT_START_POINTS);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initView();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initDataAndHeatMap();

    }

    private void initView() {
        mYourLocationEditText = (MaterialEditText) findViewById(R.id.your_location);
        mBlackHeartEditText = (MaterialEditText) findViewById(R.id.black_heart_location);
        mButton = (Button) findViewById(R.id.gotoHospital);
        mListView = (ListView)findViewById(R.id.hospital_list);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRoute();
            }
        });

        mBlackHeartEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                    final List<String> list = getHospitalList(NeuApp.mNeuApp.getHospitalAreaList());
                    mBlackHeartAdapter = new BlackHeartAdapter(MainActivity.this,list);
                    mListView.setVisibility(View.VISIBLE);
                    mButton.setVisibility(View.GONE);
                    mListView.setAdapter(mBlackHeartAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mBlackHeartEditText.setText(list.get(position));
                            mListView.setVisibility(View.GONE);
                            mButton.setVisibility(View.VISIBLE);

                        }
                    });
                }
            }
        });


        showEditText();
    }

    private void gotoRoute() {
        if (TextUtils.isEmpty(mBlackHeartEditText.getText().toString())){
            ToastUtil.show(this,"请输入前往医院");
        }else {
            for (String name: mHospitalNameList) {
                if (name.equals(mBlackHeartEditText.getText().toString())){
                    Intent intent = new Intent(this,RouteActivity.class);
                    intent.putExtra("city",mMapLocation.getCity());
                    intent.putExtra("hospitalName",mBlackHeartEditText.getText().toString());
                    intent.putExtra("yourLocation",new LatLng(mMapLocation.getLatitude(),mMapLocation.getLongitude()));
                    startActivity(intent);
                }
            }

        }
    }

    /**
     * 获取黑心医院
     */
    private List<String> getHospitalList(List<HospitalArea> list) {

        mHospitalNameList.clear();
        for (HospitalArea area :list) {
            if (mMapLocation.getCity().contains(area.getAreaName())){
                mHospitalNameList.addAll(area.getHospital());
            }
        }
        return mHospitalNameList;
    }

    private void showEditText() {
        if (!isShowEditText){
            return;
        }
        mYourLocationEditText.setVisibility(View.VISIBLE);
        mBlackHeartEditText.setVisibility(View.VISIBLE);
    }

    private void initDataAndHeatMap() {
        List<LatLng> list = new ArrayList<>();

        for (int i = 0; i < NeuApp.mNeuApp.getHospitalGeos().size(); i++) {
            list.add(new LatLng(NeuApp.mNeuApp.getHospitalGeos().get(i).getGeometry().getCoordinates().get(1),NeuApp.mNeuApp.getHospitalGeos().get(i).getGeometry().getCoordinates().get(0)));
        }


        HeatmapTileProvider heatmapTileProvider = new HeatmapTileProvider.Builder()
                .data(list).gradient(ALT_HEATMAP_GRADIENT)

                .build();
        aMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatmapTileProvider));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(3.8f));
        aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
        mUiSettings.setMyLocationButtonEnabled(true);
        aMap.setLocationSource(this);
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位参数
            mLocationOption.setNeedAddress(true);
            mLocationOption.setOnceLocation(true);
            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (mListener != null) {
            mMapLocation = aMapLocation;
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude()),15));
            isShowEditText = true;
            showEditText();
            setYourLocationText(aMapLocation.getStreet());
        }
    }

    private void setYourLocationText(String address) {
        mYourLocationEditText.setText(address);
    }
}


