package brickhack.jive.detail_page;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import brickhack.jive.R;
import ninjaman494.expandabletextview.ExpandableTextView;

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

        //Desp
        ExpandableTextView etv = (ExpandableTextView)findViewById(R.id.etv);
        TextView blurb = etv.getBlurbView();
        TextView desp = etv.getDespView();

        blurb.setText("RIT's premier hackathon");
        blurb.setTextSize(16);
        blurb.setTypeface(null, Typeface.BOLD);
        blurb.setGravity(Gravity.CENTER);

        desp.setText("BrickHack brings hackers together for 24 hours of design,development, and collaboration. Students create the impossible.");
        desp.setTextSize(16);

        etv.startExpanded(true);
    }
}

