package com.zlb.takephoto;

public class CipherText {

    /**
     * original_time : 1531831245
     * original_time_reliability : 1
     * staff_id : 12334
     * geo : {"latitude":220.12,"longitude":22.5,"name":"万科大厦"}
     * name : 小等
     */

    private long original_time;
    private int original_time_reliability;  //0，不可信 1，不确定  2 可信
    private int staff_id;
    private GeoBean geo;
    private String name;

    public long getOriginal_time() {
        return original_time;
    }

    public void setOriginal_time(long original_time) {
        this.original_time = original_time;
    }

    public int getOriginal_time_reliability() {
        return original_time_reliability;
    }

    public void setOriginal_time_reliability(int original_time_reliability) {
        this.original_time_reliability = original_time_reliability;
    }

    public int getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }

    public GeoBean getGeo() {
        return geo;
    }

    public void setGeo(GeoBean geo) {
        this.geo = geo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class GeoBean {
        /**
         * latitude : 220.12
         * longitude : 22.5
         * name : 万科大厦
         */

        private double latitude;
        private double longitude;
        private String name;

        public GeoBean(double latitude, double longitude, String name) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
