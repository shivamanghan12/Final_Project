package com.android.newsfeedapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedFlights extends AppCompatActivity {

    private ArrayList<flight> flights;
    private activity_adapter saved_adapter;
    ListView saved_listView;


    Database databaseHelp;
    ExtraStuff e;
    flight f;
    Cursor cursor;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_flights);

        saved_listView = (ListView) findViewById(R.id.listView_sFlights);


        databaseHelp = new Database(this);

        flights = new ArrayList<flight>();
        saved_adapter = new activity_adapter(flights, this);
        saved_listView.setAdapter(saved_adapter);

        cursor = databaseHelp.viewData();
        int idColumnIndex = cursor.getColumnIndex(databaseHelp.COL_FLIGHTID);
        int fNumberColIndex = cursor.getColumnIndex(databaseHelp.COL_FNUMBER);
        int isArrivingColIndex = cursor.getColumnIndex(databaseHelp.COL_ISARRIVING);
        int latitudeColIndex = cursor.getColumnIndex(databaseHelp.COL_LATITUDE);
        int longitudeColIndex = cursor.getColumnIndex(databaseHelp.COL_LONGITUDE);
        int speedColIndex = cursor.getColumnIndex(databaseHelp.COL_SPEED);
        int altitudeColIndex = cursor.getColumnIndex(databaseHelp.COL_ALTITUDE);
        int statusColIndex = cursor.getColumnIndex(databaseHelp.COL_STATUS);
        int airportCodeColIndex = cursor.getColumnIndex(databaseHelp.COL_AIRPORT);


        if (cursor.getCount() != 0) {

            while (cursor.moveToNext()) {
                f = new flight(cursor.getString(fNumberColIndex), cursor.getDouble(isArrivingColIndex) == 0, cursor.getDouble(latitudeColIndex), cursor.getDouble(longitudeColIndex), cursor.getDouble(speedColIndex), cursor.getDouble(altitudeColIndex), cursor.getString(statusColIndex),cursor.getString(airportCodeColIndex));

                flights.add(f);
                f.setId(cursor.getInt(idColumnIndex));


                saved_adapter.notifyDataSetChanged();
            }
        }

        saved_listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent in = new Intent(SavedFlights.this, saved_details.class);
            in.putExtra("id", saved_adapter.getItem(position).getId());
            in.putExtra("position", position);
            in.putExtra("latitude", saved_adapter.getItem(position).getLatitude());
            in.putExtra("longitude", saved_adapter.getItem(position).getLongitude());
            in.putExtra("speed", saved_adapter.getItem(position).getSpeed());
            in.putExtra("altitude", saved_adapter.getItem(position).getAltitude());
            in.putExtra("status", saved_adapter.getItem(position).getStatus());
            in.putExtra("flightNumber", saved_adapter.getItem(position).getfNumber());
            startActivityForResult(in, 360);
        });

        // saved_adapter=new activity_adapter(flights,this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 360) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra("id", 0);
                int position = data.getIntExtra("position", 0);
                delete_msg(id, position);
            }
        }
    }


    public void delete_msg(long id,int position){
        Log.i("Delete this message:", " id=" + id);
        databaseHelp.delete(id);
        int i=cursor.getCount();
        flights.remove(position);


        saved_adapter.notifyDataSetChanged();
    }
}

