package com.android.newsfeedapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Ashish Kuamar
 * This activity is used to see the detail information displayed when clicked on the listView
 */
public class ExtraStuff extends AppCompatActivity {
    /**
     * Flight object to get info
     */
    private flight f;
    /**
     * Database object
     */
    public Database databaseHelp;

    /**
     * get information from the passed intent as well insert functionality on clicking the save button
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extra_stuff);
        Intent i = getIntent();


        TextView loc = (TextView) findViewById(R.id.tView_extra_location);
        TextView speed = (TextView) findViewById(R.id.tView_extra_speed);
        TextView alt = (TextView) findViewById(R.id.tView_extra_altitude);
        TextView stat = (TextView) findViewById(R.id.tView_extra_status);
        Button save = (Button) findViewById(R.id.bSave);
        final String fNumber = i.getStringExtra("flightNumber");
        final double lati = i.getDoubleExtra("latitude", 0.0);
        final double lon = i.getDoubleExtra("longitude", 0.0);
        final double spee = i.getDoubleExtra("speed", 0);
        final double alti = i.getDoubleExtra("altitude", 0);
        final String sta = i.getStringExtra("status");
        final boolean isArriving = i.getBooleanExtra("isArriving", false);

        final String airport=i.getStringExtra("airportCode");
        databaseHelp = new Database(this);


        loc.setText("Location\n\t\t\t\t\tLatitude: " + String.valueOf(lati) + "\n\t\t\t\t\tLongitude: " + String.valueOf(lon));
        speed.setText("Speed: " + String.valueOf(spee));
        alt.setText("Altitude: " + String.valueOf(alti));
        stat.setText("Status: " + sta);

        save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        f = new flight(fNumber, isArriving, lati, lon, spee, alti, sta,airport);

                                        long id = databaseHelp.insertFlight(f);

                                        if (id > -1) {
                                            f.setId(id);
                                        }
                                       Toast toast= Toast.makeText(getApplicationContext(),getString(R.string.confirm_save), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                        toast.show();
                                    }


                                }
        );

    }


}
