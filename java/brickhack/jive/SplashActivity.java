package brickhack.jive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity implements ServerListener{
    ServerAPI server;
    Parser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server = new ServerAPI(this);
        parser = new Parser(this);
        server.refreshEvents();
    }

    @Override
    public void onResult(boolean success) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("DATA_MAP",HomePageActivity.buildMap(server,parser));
        startActivity(intent);
        finish();
    }
}
