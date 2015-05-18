package com.example.IdarMaps;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;

public class MyActivity extends Activity {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    static final LatLng OSLO = new LatLng(59.9500, 10.7500);
    private Context context;
    private MapFragment map;
    private LocationManager locationManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate");
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));

        /**
         * TEST
         */
        new Thread() {
            @Override
            public void run() {
                WebkameraPullParser w = new WebkameraPullParser();
                try {
                    w.execute(new URL("http://webkamera.vegvesen.no/metadata"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.run();

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
