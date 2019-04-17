package com.android.newsfeedapp.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.newsfeedapp.ExtraStuff;
import com.android.newsfeedapp.R;
import com.android.newsfeedapp.SavedFlights;
import com.android.newsfeedapp.activity_adapter;
import com.android.newsfeedapp.flight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Activity for searching flights information
 *
 * This activity is used to search for various flights on particular airport with Airport code
 *
 * @author Ashish Kumar
 * @version
 *
 */
public class flight_tracker extends AppCompatActivity {
    /**
     * SharedPreferences sp field to get Shared Preferances
     */
    SharedPreferences sp;
    /**
     * EditText to get search edit text from the layout file
     */
    EditText eText;
    /**
     * ListView to display search results
     */
    ListView flightsV;
    /**
     * Flight object to store info
     */
    flight f;
    /**
     * Base adapter to store flightsV
     */
    activity_adapter flightAdapter;
    /**
     * Search button to search for flights
     */
    Button search_b;
    /**
     * pro=gress bar to show progress
     */
    ProgressBar pBar;
    /**
     * List of flight object to add flights
     */
    List<flight> flightList;
    /**
     * Request code for further details
     */
    public static final int EXTRA_STUFF = 345;
    /**
     * request code for saved flights
     */
    public static final int SAVED_FLIGHTS = 350;
    public static final int HELP = 370;

    /**
     * Creates toolbar,toast,executes the async task on clicking listView
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_tracker);
        android.support.v7.widget.Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        //flights = new ArrayList<>();
        flightList = new ArrayList<>();
        search_b = (Button) findViewById(R.id.search_button);


        flightsV = (ListView) findViewById(R.id.listView);


        // databaseHelp = new Database(this);

        flightAdapter = new activity_adapter(flightList, this);
        flightsV.setAdapter(flightAdapter);


        pBar = (ProgressBar) findViewById(R.id.progressBar);

        eText = (EditText) findViewById(R.id.etext_search);
        sp = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveName", "");
        eText.setText(savedString);
        //Show the toast immediately:
        Toast toast= Toast.makeText(this,getString(R.string.welcome), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();


        //  Snackbar code:
        Snackbar sb = Snackbar.make(tBar,getString(R.string.snackbar_text), Snackbar.LENGTH_LONG)
                .setAction("Action text", e -> Log.e("Flight_tracker", "Clicked action_text"));
        sb.show();

//

        search_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightList.clear();
                flightAdapter.notifyDataSetChanged();
                new FlightAsync().execute("http://aviation-edge.com/v2/public/flights?key=1e6a21-d406ef&arrIata=" + eText.getText().toString());
                new FlightAsync().execute("http://aviation-edge.com/v2/public/flights?key=1e6a21-d406ef&depIata=" + eText.getText().toString());


            }
        });
        flightsV.setOnItemClickListener((parent, view, position, id) -> {
                    Intent in = new Intent(flight_tracker.this, ExtraStuff.class);
                    in.putExtra("isArriving", flightAdapter.getItem(position).getArriving());
                    in.putExtra("latitude", flightAdapter.getItem(position).getLatitude());
                    in.putExtra("longitude", flightAdapter.getItem(position).getLongitude());
                    in.putExtra("speed", flightAdapter.getItem(position).getSpeed());
                    in.putExtra("altitude", flightAdapter.getItem(position).getAltitude());
                    in.putExtra("status", flightAdapter.getItem(position).getStatus());
                    in.putExtra("flightNumber", flightAdapter.getItem(position).getfNumber());
                    in.putExtra("airportCode", flightAdapter.getItem(position).getAirportCode());
                    startActivityForResult(in, EXTRA_STUFF);
                }
        );


        pBar.setVisibility(View.VISIBLE);
    }

    /**
     * Inflates menu with menu resource file
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return true;
    }

    /**
     * Function on selecting particuar item in toolbar
     * @param item
     * @return true
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.item1:

                dialogInfo();
                break;

            case R.id.item2:
                Intent i = new Intent(flight_tracker.this, SavedFlights.class);
                startActivityForResult(i, SAVED_FLIGHTS);


                break;
        }
        return true;
    }

    /**
     * method to build and customize the dialog box
     * @return
     */
    public boolean dialogInfo() {
        View middle = getLayoutInflater().inflate(R.layout.info, null);
        TextView author = (TextView) middle.findViewById(R.id.tView_info_author);
        TextView activity_version = (TextView) middle.findViewById(R.id.tView_info_actitvityVersion);
        TextView instructions = (TextView) middle.findViewById(R.id.tView_info_instructions);
        TextView header = (TextView) middle.findViewById(R.id.tView_info_header);

        header.setText(getString(R.string.info));
        author.setText(getString(R.string.author) + ": " + getString(R.string.author_name));

        activity_version.setText(getString(R.string.ativity_v) + ": "+getString(R.string.activityVersion));
        instructions.setText(getString(R.string.inst) + ": \n" + getString(R.string.instruction));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(middle);

        Dialog dialog = builder.create();
        dialog.show();
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 70%
        int dialogWindowHeight = (int) (displayHeight * 0.7f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
        return true;
    }

    /**
     * onPause() method to retreive last searched airport code
     */
    @Override
    protected void onPause() {

        super.onPause();
        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveName"
        String whatWasTyped = eText.getText().toString();
        editor.putString("ReserveName", whatWasTyped);

        //write it to disk:
        editor.commit();
    }

    /**
     * private class to open connection,read info from the url
     */
    private class FlightAsync extends AsyncTask<String, Integer, List<flight>> {
/**
 * creates connection,reads info and add it to flightsList
 */
        @SuppressLint("WrongThread")
        @Override
        protected List<flight> doInBackground(String... strings) {
            // String url1 = strings[0];


            try {
                String url1 = strings[0];

                URL url = new URL(url1);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setReadTimeout(10000 /* milliseconds */);
                httpConnection.setConnectTimeout(15000 /* milliseconds */);
                httpConnection.setRequestMethod("GET");
                httpConnection.setDoInput(true);
                InputStream inStream = httpConnection.getInputStream();


                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();


                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONArray arr = new JSONArray(result);


                for (int i = 0; i < arr.length(); i++) {
                    JSONObject anObject = arr.getJSONObject(i);
                    JSONObject geog = anObject.getJSONObject("geography");
                    double lat = geog.getDouble("latitude");
                    double lon = geog.getDouble("longitude");
                    double alt = geog.getDouble("altitude");

                    JSONObject spd = anObject.getJSONObject("speed");
                    double hzon = spd.getDouble("horizontal");

                    JSONObject fli = anObject.getJSONObject("flight");
                    String num = fli.getString("number");

                    JSONObject arrival = anObject.getJSONObject("arrival");
                    String a = arrival.getString("iataCode");

                    String stat = String.valueOf(anObject.getString("status"));

                    if (a.equals(eText.getText().toString())) {
                        f = new flight(num, true, lat, lon, hzon, alt, stat, eText.getText().toString());
                    } else {
                        f = new flight(num, false, lat, lon, hzon, alt, stat, eText.getText().toString());

                    }
                    flightList.add(f);

                }
                publishProgress(100);
                Thread.sleep(2000);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return flightList;
        }

        /**
         * This updates the Gui
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTaskExample", "update:" + values[0]);
            //messageBox.setText("At step:" + values[0]);
            pBar.setVisibility(View.VISIBLE);
            pBar.setProgress(values[0]);
        }

        /**
         * Notifies the flightAdapter to update the ListView
         * @param flightList
         */
        @Override
        protected void onPostExecute(List<flight> flightList) {


            flightAdapter.notifyDataSetChanged();

            pBar.setVisibility(View.INVISIBLE);

        }
    }


}
