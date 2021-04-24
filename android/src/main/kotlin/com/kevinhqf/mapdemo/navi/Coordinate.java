package com.kevinhqf.mapdemo.navi;


import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

public class Coordinate {
    public double latitude;
    public double longitude;

    CameraPosition toCameraPosition() {
        return new CameraPosition(new LatLng(latitude,longitude), 10, 0, 0);
    }

    LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
