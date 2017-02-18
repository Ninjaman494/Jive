package brickhack.jive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener {
    String name;
    String desp;
    String key;
    String date;
    String hour;
    Location mLastLocation;
    GoogleMap map;
    ServerAPI server;
    Parser parser;
    double[] coords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        desp = intent.getStringExtra("desp");
        coords = intent.getDoubleArrayExtra("coords");
        key = intent.getStringExtra("key");
        System.out.println("key: "+key);
        date = intent.getStringExtra("date");
        hour = intent.getStringExtra("hour");
        //server = new ServerAPI(this;
        parser = new Parser(this);
        System.out.println("coords: "+coords);


        //Setting details
        TextView nameView = (TextView)findViewById(R.id.name);
        TextView dateView = (TextView)findViewById(R.id.date);
        TextView hourView = (TextView)findViewById(R.id.hours);
        TextView despView = (TextView)findViewById(R.id.desp);

        nameView.setText(name);
        dateView.setText(date);
        hourView.setText(hour);
        despView.setText(desp);
        setupBottomBar();

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

    private void setupBottomBar(){
        if(!parser.isInFile(key)){
            FragmentManager fm = getSupportFragmentManager();
            BottomBarFragment bottomBarFragment = (BottomBarFragment)fm.findFragmentById(R.id.bottom_bar);
            TextView tv = (TextView)bottomBarFragment.getView().findViewById(R.id.status_text);
            tv.setText(R.string.not_signed);
            Button btn = (Button)bottomBarFragment.getView().findViewById(R.id.status_button);
            btn.setText(R.string.not_singed_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //parser.addEvent(name,key);
                    parser.addEvent(name,key);
                }
            });
        }else{
            FragmentManager fm = getSupportFragmentManager();
            BottomBarFragment bottomBarFragment = (BottomBarFragment)fm.findFragmentById(R.id.bottom_bar);
            TextView tv = (TextView)bottomBarFragment.getView().findViewById(R.id.status_text);
            tv.setText(R.string.signed);
            Button btn = (Button)bottomBarFragment.getView().findViewById(R.id.status_button);
            btn.setText(R.string.signed_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parser.removeEvent(key);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        System.out.println("Hello map");
        map.addMarker(new MarkerOptions()
                .position(new LatLng(coords[0], coords[1]))
                .title("Marker"));

        System.out.println("OnMapReady: "+coords[0]+","+coords[1]);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(coords[0], coords[1]));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

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


