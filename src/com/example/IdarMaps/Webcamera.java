package com.example.IdarMaps;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;

/**
 * Created by Cyzla on 18.05.2015.
 */
public class Webcamera {
    private URL url;
    private String stedsnavn;
    private String veg;
    private String landsdel;
    private String breddegrad;
    private String lengdegrad;

    public URL getUrl() {
        return url;
    }

    public LatLng getLatLng(){
        return new LatLng(Double.parseDouble(breddegrad), Double.parseDouble(lengdegrad));
    }

    public void setUrl(URL url) {
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

    public String getLengdegrad() {
        return lengdegrad;
    }

    public void setLengdegrad(String lengdegrad) {
        this.lengdegrad = lengdegrad;
    }

}
