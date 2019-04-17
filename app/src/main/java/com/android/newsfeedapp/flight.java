package com.android.newsfeedapp;

import java.io.Serializable;

public class flight implements Serializable {
    private long id;
    private String fNumber;
    private boolean isArriving;
    private double latitude;
    private double longitude;
    private double speed;
    private double altitude;
    private String status;
    private String airportCode;



    public flight(String fNumber, boolean isArriving, double latitude, double longitude, double speed, double altitude, String status, String airportCode){
        this.fNumber=fNumber;
        this.isArriving=isArriving;
        this.latitude=latitude;
        this.longitude=longitude;
        this.speed=speed;
        this.altitude=altitude;
        this.status=status;
        this.airportCode=airportCode;
    }
    public void setId(long id){
        this.id=id;
    }

    public long getId(){
        return id;
    }
    public flight(){
        this("unknown",false,0,0,-1,-1,"unknown","unknown");
    }

    public void setfNumber(String fNumber){
        this.fNumber=fNumber;
    }

    public String getfNumber(){
        return fNumber;
    }

    public void setArriving(boolean isArriving){
        this.isArriving=isArriving;
    }

    public boolean getArriving(){
        return isArriving;
    }

    public void setLatitude(double latitude){
        this.latitude=latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude=longitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setSpeed(double speed){
        this.speed=speed;
    }
    public double getSpeed(){
        return speed;
    }

    public void setAltitude(double altitude){
        this.altitude=altitude;
    }

    public double getAltitude(){
        return altitude;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public String getStatus(){
        return status;
    }

    public void setAirportCode(){
       this.setAirportCode();

    }
    public String getAirportCode(){
        return airportCode;
    }

    public boolean equals(flight f){
        Boolean isEqual=false;
        if(this.getfNumber()==f.getfNumber()&& String.valueOf(this.getArriving()).equals(String.valueOf(f.getArriving()))&& this.getLatitude()==f.getLatitude()&& this.getLongitude()==f.getLongitude()&&this.getSpeed()==f.getSpeed()&&this.getAltitude()==f.getAltitude()&&this.getStatus().equals(f.getStatus()))
        return true;
        else
            return false;
    }


}
