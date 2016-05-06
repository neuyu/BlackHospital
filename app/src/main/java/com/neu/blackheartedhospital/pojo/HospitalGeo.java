package com.neu.blackheartedhospital.pojo;

import java.util.List;

/**
 * Created by neu on 16/5/3.
 */
public class HospitalGeo {

    private String type;
    /**
     * name : 江苏泰州红房子医院
     */

    private PropertiesBean properties;
    /**
     * type : Point
     * coordinates : [119.923116,32.455778]
     */

    private GeometryBean geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public static class PropertiesBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class GeometryBean {
        private String type;
        private List<Double> coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }
    }
}
