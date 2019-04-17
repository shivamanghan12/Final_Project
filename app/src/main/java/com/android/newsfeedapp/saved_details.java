package com.android.newsfeedapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class saved_details extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_details);

        Intent i = getIntent();

        TextView loc = (TextView) findViewById(R.id.tView_saved_location);
        TextView speed = (TextView) findViewById(R.id.tView_saved_speed);
        TextView alt = (TextView) findViewById(R.id.tView_saved_altitude);
        TextView stat = (TextView) findViewById(R.id.tView_saved_status);
        Button delete = (Button) findViewById(R.id.bDelete);
        TextView details= (TextView) findViewById(R.id.tView_extra_details);
        long id = i.getLongExtra("id", 0);
        int position = i.getIntExtra("position", 0);
        double fNumber = i.getDoubleExtra("flightNumber", 0);
        double lati = i.getDoubleExtra("latitude", 0.0);
        double lon = i.getDoubleExtra("longitude", 0.0);
        double spee = i.getDoubleExtra("speed", 0);
        double alti = i.getDoubleExtra("altitude", 0);
        String sta = i.getStringExtra("status");
        String airport=i.getStringExtra("airportCode");
        //details.setText("DETAILS "+"("+airport+")");

        delete.setOnClickListener(v -> {
            Intent backToSavedFlights = new Intent();
            backToSavedFlights.putExtra("id", id);
            backToSavedFlights.putExtra("position", position);
            setResult(Activity.RESULT_OK, backToSavedFlights);
            finish();
            Toast toast= Toast.makeText(getApplicationContext(),getString(R.string.confirm_delete), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            toast.show();


        });


        loc.setText(getString(R.string.location)+"\n\t\t\t\t\t"+getString(R.string.latitude)+": " + String.valueOf(lati) + "\n\t\t\t\t\t"+getString(R.string.longitude)+": " + String.valueOf(lon));
        speed.setText(getString(R.string.speed)+": " + String.valueOf(spee));
        alt.setText(getString(R.string.altitude)+": " + String.valueOf(alti));
        stat.setText(getString(R.string.status)+": " + sta);
    }

}
