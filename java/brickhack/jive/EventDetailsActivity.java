package brickhack.jive;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {
    String name;
    String desp;
    String key;
    int lat; int lon;
    Location mLastLocation;
    GoogleMap map;
    ServerAPI server;
    double[] coords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        name = getIntent().getStringExtra("name");
        desp = getIntent().getStringExtra("desp");
        coords = getIntent().getDoubleArrayExtra("coords");
        server = new ServerAPI(this,false);
        System.out.println("coords: "+coords);
        //((TextView) findViewById(R.id.name)).setText(name);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
        }

        //Checks how much data this event has and creates fragments based on available data
        //Images - replace with database info
        if(false){
            ImagesFragment imagesFragment = ImagesFragment.newInstance(getImages());
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.add(R.id.frag_container,imagesFragment);
            transaction.commit();
        }

        //Map
        if(true) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        System.out.println("Hello map");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(33, 100))
                .title("Marker"));

        System.out.println("OnMapReady: "+coords[0]+","+coords[1]);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(coords[0], coords[1]));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(25);

            map.moveCamera(center);
            map.animateCamera(zoom);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            System.out.println("u accepted?");
        }
        else {
            // Show rationale and request permission.
            System.out.println("I request");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        map.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //map.setMyLocationEnabled(true);
                System.out.println("YAY");
            } else {
                // Permission was denied. Display an error message.
            }
        }

    private ArrayList<String> getImages(){
        ArrayList<String> images = new ArrayList<>();
        images.add("Image 1");
        images.add("Image 2");
        images.add("Image 3");
        return images;
    }

    @Override
    public void onLocationChanged(Location location) {
        /*CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        map.moveCamera(center);
        map.animateCamera(zoom);*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //Empty on purpose
    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(getBaseContext(), "Gps is turned off! ", Toast.LENGTH_SHORT).show();
    }
}


