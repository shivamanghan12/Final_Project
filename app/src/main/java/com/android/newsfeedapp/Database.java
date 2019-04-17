package com.android.newsfeedapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class extends SQLiteOpenHelper
 * Used to create table,insert data,delete flight object
 * @author Ashish Kumar
 */
public class Database extends SQLiteOpenHelper {

    /**
     * Static fields so that no typo occurs
     */
    private static final String DB_NAME = "FlightsDB";
    private static final String DB_TABLE = "Flights_Table";

    public static final String COL_FLIGHTID = "FlightID";
    public static final String COL_FNUMBER = "Fligt_Number";
    public static final String COL_ISARRIVING = "IsArriving";
    public static final String COL_LATITUDE="Latitude";
    public static final String COL_LONGITUDE="Longitude";
    public static final String COL_SPEED="Speed";
    public static final String COL_ALTITUDE="Altitude";
    public static final String COL_STATUS="Status";
    public static final String COL_AIRPORT="Airport";
    /**
     * Query to create table
     */
    private static final String CREATE_TABLE = "CREATE TABLE "+DB_TABLE+" ("+COL_FLIGHTID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_FNUMBER+" TEXT, "+COL_ISARRIVING+" BIT, "+COL_LATITUDE+" INTEGER, "+COL_LONGITUDE+" INTEGER, "+COL_SPEED+" INTEGER, "+COL_ALTITUDE+" INTEGER, "+COL_STATUS+" TEXT, "+COL_AIRPORT+" TEXT);";
/**
get a readable database
 */
    SQLiteDatabase db = this.getWritableDatabase();

    /**
     * Overloaded constructor to create a Database object with given Context
     * @param context
     */
    public Database(Context context) {

        super(context, DB_NAME, null, 4);

    }


    /**
     * Creates table to insert data
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Drops prevous table and create new table
     * @param db: database
     * @param oldVersion: old version of database
     * @param newVersion: new version of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }


//    public boolean insertData(String fNumber,boolean isArriving,double latitude,double longitude,double speed,double altitude,String status,String airport) {
//
//
//
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(COL_FNUMBER, fNumber);
//        contentValues.put(COL_LATITUDE, latitude);
//        contentValues.put(COL_LONGITUDE, longitude);
//        contentValues.put(COL_SPEED, speed);
//        contentValues.put(COL_ALTITUDE, altitude);
//        contentValues.put(COL_STATUS, status);
//
//
//        if (isArriving)
//
//            contentValues.put(COL_ISARRIVING, 0);
//
//        else
//
//            contentValues.put(COL_ISARRIVING, 1);
//
//        long result = db.insert(DB_TABLE, null, contentValues);
//
//
//        return result != -1;
//
//    }

    /**
     * Used to insert data into databse by using content values
     * @param f: flight object
     * @return long id from inserting data in database
     */
    public long insertFlight(flight f) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_FNUMBER, f.getfNumber());
        contentValues.put(COL_LATITUDE, f.getLatitude());
        contentValues.put(COL_LONGITUDE, f.getLongitude());
        contentValues.put(COL_SPEED, f.getSpeed());
        contentValues.put(COL_ALTITUDE, f.getSpeed());
        contentValues.put(COL_STATUS, f.getSpeed());
        contentValues.put(COL_AIRPORT,f.getAirportCode());
        if (f.getArriving())
            contentValues.put(COL_ISARRIVING, 0);
        else
            contentValues.put(COL_ISARRIVING, 1);

        long result = db.insert(DB_TABLE, null, contentValues);


        return result;
    }

    /**
     * Used to view data insertedd into the database
     * @return cursor object
     */
    public Cursor viewData(){

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from "+DB_TABLE;

        Cursor cursor = db.rawQuery(query, null);

//        Log.e("Database Version Number", Integer.toString(db.getVersion()));
//
//        Log.e("Column Count", Integer.toString(cursor.getColumnCount()));
//
//
//
//        Log.e("Column Names", Arrays.toString(cursor.getColumnNames()));
//
//        Log.e("Row Count", Integer.toString(cursor.getCount()));
//
//        Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

        return cursor;

    }

    /**
     * Used to delete flight object from the object
     * @param id: id of the flight object in the database
     */
    public void delete(Long id){
        db.execSQL("DELETE FROM " +DB_TABLE+" WHERE "+COL_FLIGHTID+"="+id+";");
    }



}
