package com.example.locationreminder;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;

public class firebasemodel {
    private String title;
    private String description;
    private String date;
    private String time;
    private List<String> weather=new ArrayList<String>();
    private List<Double> location = new ArrayList<Double>();

    public  firebasemodel(){

    }
    public firebasemodel(String title, String description){
        this.title = title;
        this.description = description;
    }

    public List<Double> getLocation() {
        return location;
    }



    public List<String> getWeather() {
        return weather;
    }

    public String getTime() {
        return time;
    }

    public void setLocation(List<Double> location) {
        //this.location.set(location);
        this.location = location;
    }


    public void setWeather(List<String> weather) {
        this.weather = weather;
    }

    public void setTime(String time) {
        this.time = time;
    }




    //constructor with all parameters

  /* public firebasemodel(String title, String description, String date, List<String> weather, String time, List<Double> location){
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
       //this.location.set(location);
        this.weather = weather;
    }*/
    //constructor with time only
    public firebasemodel(String title, String description, String time){
        this.title = title;
        this.description = description;
        this.time = time;
    }



    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {return  date;}
    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }      
}