package com.example.IdarMaps;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;

/**
 * Created by Cyzla on 18.05.2015.
 */
public class Webcamera {
    private String url;
    private LatLng latLng;
    private String stedsnavn;
    private String veg;
    private String landsdel;
    private String breddegrad;
    private String lengdegrad;
    private String info;
    private String markerId;
    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

    public String getUrl() {
        return url;
    }

    public void setLatLng() {
        if (breddegrad == null || lengdegrad == null) {
            latLng = new LatLng(0, 0);
        } else {
            latLng = new LatLng(Double.parseDouble(breddegrad), Double.parseDouble(lengdegrad));
        }
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStedsnavn() {
        return stedsnavn;
    }

    public void setStedsnavn(String stedsnavn) {
        this.stedsnavn = stedsnavn;
    }

    public String getVeg() {
        return veg;
    }

    public void setVeg(String veg) {
        this.veg = veg;
    }

    public String getLandsdel() {
        return landsdel;
    }

    public void setLandsdel(String landsdel) {
        this.landsdel = landsdel;
    }

    public String getBreddegrad() {
        return breddegrad;
    }

    public void setBreddegrad(String breddegrad) {
        this.breddegrad = breddegrad;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getLengdegrad() {
        return lengdegrad;
    }

    public void setLengdegrad(String lengdegrad) {
        this.lengdegrad = lengdegrad;
    }

}
