package com.example.Models;


import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class DeliveryBoyLocation {

    public GeoPoint geo_point;
    public  @ServerTimestamp
    Date timestamp;

    public DeliveryBoyLocation(GeoPoint geo_point, Date timestamp) {
        this.geo_point = geo_point;
        this.timestamp = timestamp;
    }

    public DeliveryBoyLocation() {
    }

    public GeoPoint getGeo_point() {
        return geo_point;
    }

    public void setGeo_point(GeoPoint geo_point) {
        this.geo_point = geo_point;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
