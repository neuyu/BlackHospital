package com.neu.blackheartedhospital.pojo;

import java.util.List;

/**
 * Created by neu on 16/5/3.
 */
public class HospitalArea {

    /**
     * areaName : 泰州
     * count : 3
     * hospital : ["江苏泰州红房子医院","泰州市海陵医院","泰州海陵女子医院"]
     */

    private String areaName;
    private String count;
    private List<String> hospital;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<String> getHospital() {
        return hospital;
    }

    public void setHospital(List<String> hospital) {
        this.hospital = hospital;
    }
}
