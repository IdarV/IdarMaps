package com.example.IdarMaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

public class MyActivity extends Activity {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    static final LatLng OSLO = new LatLng(59.9500, 10.7500);
    static final LatLng NORWAY = new LatLng(64.6075817, 12.3405745);
    private Context context;
    private MapFragment map;
    private LocationManager locationManager;
    public ArrayList<Webcamera> webcamerasMyActivity;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        webcamerasMyActivity = new ArrayList<Webcamera>();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        /**
         * TEST
         */
        if (webcamerasMyActivity.size() == 0) {
            WebkameraPullParser w = new WebkameraPullParser(this);
            try {
                w.execute(new URL("http://webkamera.vegvesen.no/metadata"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void setCoords() {
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);
                // Create Locationmanager
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng currentPosition;
                if (location != null) {
                    Log.d("onMapReady", "Location is not null : " + location.toString());
                    currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                } else {
                    Log.d("onMapReady", "Location is null");
                    currentPosition = NORWAY;

                }
                if (map != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
                    for (Webcamera w : webcamerasMyActivity) {
                        LatLng latLng = w.getLatLng();
                        String title = w.getStedsnavn() + ", " + w.getVeg();
                        String info = w.getInfo();
                        if (latLng != null) {
                            Marker me = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(title)
                                    .snippet(info));
                            w.setMarkerId(me.getId());

                        }
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(4), 3, null);
                    }

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            String url;
                            url = getUriFromMarkerId(marker);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                            startActivity(browserIntent);
                        }
                    });
                }

            }
        });

    }

    public String getUriFromMarkerId(Marker marker){
        String id = marker.getId();
        for(Webcamera w : webcamerasMyActivity){
            if(w.getMarkerId().equals(id)){
                return w.getUrl();
            }
        }
        return "http://google.no";
    }


}
