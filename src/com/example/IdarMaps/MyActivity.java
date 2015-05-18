package com.example.IdarMaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Idar Vassdal on 18.05.2015.
 */
public class MyActivity extends Activity {

    static final LatLng NORWAY = new LatLng(64.6075817, 12.3405745);

    private Context context;
    private MapFragment map;
    private LocationManager locationManager;
    private LinearLayout loadingInformation;
    private LatLng currentPosition;
    public ArrayList<Webcamera> webcamerasMyActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;

        initLoadingInformaion();
        setPosition();
        initWebCameras();
    }

    public void initWebCameras(){
        webcamerasMyActivity = new ArrayList<>();
        if (webcamerasMyActivity.size() == 0) {
            WebkameraPullParser w = new WebkameraPullParser(this);
            try {
                w.execute(new URL(getString(R.string.vegvesenWebkameraMetadata)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void initLoadingInformaion(){
        loadingInformation = (LinearLayout) findViewById(R.id.loadingInformation);
        loadingInformation.setVisibility(View.VISIBLE);
    }

    public void removeLoadingInformation(){
        loadingInformation.setVisibility(View.GONE);
    }

    public void setPosition(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        currentPosition = (location == null) ? NORWAY :  new LatLng(location.getLatitude(), location.getLongitude());
    }

    public void setCoords() {
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);

                if (map != null) {
                    initGoogleMapWithCoords(googleMap);
                }

            }
        });
        removeLoadingInformation();
    }

    public void initGoogleMapWithCoords(GoogleMap googleMap){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(4), 3000, null);
        for (Webcamera w : webcamerasMyActivity) {
            if (w.getStedsnavn() != null) {
                LatLng latLng = w.getLatLng();
                String title = w.getStedsnavn() + getString(R.string.colonspace) + w.getVeg();
                String info = w.getInfo();
                Marker me = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(title)
                        .snippet(info));
                w.setMarkerId(me.getId());

            }

        }

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String url;
                url = getUriFromMarkerId(marker.getId());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
    }

    public String getUriFromMarkerId(String markerId) {
        for (Webcamera w : webcamerasMyActivity) {
            if(w.getMarkerId() != null) {
                if (w.getMarkerId().equals(markerId)) {
                    return w.getUrl();
                }
            }
        }
        // TODO: make this quickfix better, Toast or something
        return getString(R.string.googleno);
    }

}
