package com.example.IdarMaps;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    static final LatLng OSLO = new LatLng(59.9500, 10.7500);
    private MapFragment map;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);
                // Create Locationmanager
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LatLng currentPosition;
                if(location != null){
                    Log.d("onMapReady", "Location is not null : " + location.toString());
                    currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
                }
                else{
                    Log.d("onMapReady", "Location is null");
                    currentPosition = KIEL;

                }

                if (map != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    Marker me = googleMap.addMarker(new MarkerOptions()
                            .position(currentPosition)
                            .title("Her er jeg!")
                            .snippet("Hei hei!"));

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 15));
                    //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 4000, null);
                }

            }
        });
    }

    public LatLng locationToLatLng(Location location){
        if(location == null){
            return KIEL;
        }

        return new LatLng(location.getLatitude(), location.getLongitude());

    }

}
