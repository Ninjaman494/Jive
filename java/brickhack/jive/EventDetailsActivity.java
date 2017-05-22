package brickhack.jive;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class EventDetailsActivity extends AppCompatActivity{
    String name;
    String desp; //Will be implemented in next update
    String date;
    String hour;
    //TODO Implement building and college, will require updating database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        desp = intent.getStringExtra("desp");
        date = intent.getStringExtra("date");
        hour = intent.getStringExtra("hour");

        //Setting required details
        TextView nameView = (TextView) findViewById(R.id.name);
        DTLFragment dtlFragment = DTLFragment.newInstance(date,hour,"Clark Gym");
        FragmentManager fm = getSupportFragmentManager();

        nameView.setText(name);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.infoLayout,dtlFragment);
        transaction.commit();
    }
}

