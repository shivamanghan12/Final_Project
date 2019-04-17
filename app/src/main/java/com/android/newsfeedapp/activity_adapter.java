package com.android.newsfeedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**This class extends BaseAdapter
 * Used for setting arraylist of read flight object and displaying it on the listView
 *  @author Ashish Kumar
 */

public class activity_adapter extends BaseAdapter {

    /**
     * List to add flight object
     */
    List<flight> flights;
    /**
     * context
     */
    Context ctx;
    /**
     * inflater to inflate layout
     */
    LayoutInflater inflater;

    /**
     * constructor to make new activity_adapter with given context and List
     * @param flights
     * @param ctx
     */

    public activity_adapter(List<flight> flights, Context ctx ){
        this.ctx = ctx;
        this.flights=flights;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * Returns the size of flights arraylist
     * @return
     */
    @Override
    public int getCount() {
        return flights.size();
    }

    /**
     * Returns the particular flight object on given position of activity_adapter
     * @param position
     * @return
     */
    @Override
    public flight getItem(int position) {
        return flights.get(position);
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    /**
     * Returns the inflated view for ListView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result = convertView;

        if (flights.get(position).getArriving()) {

            result = inflater.inflate(R.layout.arriving, null);

        } else {
            result = inflater.inflate(R.layout.departing, null);
        }

        TextView flightNumber = (TextView)result.findViewById(R.id.textView_saved);
        String fNumber= String.valueOf(flights.get(position).getfNumber());
        if (flights.get(position).getArriving()) {

            flightNumber.setText(position+"\t"+flights.get(position).getAirportCode()+"\t"+ctx.getResources().getString(R.string.arriving)+"\t"+fNumber); // get the string at position

        } else {
            flightNumber.setText(position+"\t"+flights.get(position).getAirportCode()+ctx.getResources().getString(R.string.departing)+"\t"+fNumber); // get the string at position
        }
        return result;
    }
}
