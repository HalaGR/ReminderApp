package com.example.locationreminder;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;

public class firebasemodel {
    /* model class for an object saved in database
    */
    private String title;
    private String description;
    private String date;
    private String time;
    private int ID;
    private List<Object> reminder=new ArrayList<Object>();

    public  firebasemodel(){

    }
    public firebasemodel compar(firebasemodel a, firebasemodel b){
        if(a.getId()> b.getId()){
            return a;
        }
        return b;
    }
    public firebasemodel(String title, String description){
        this.title = title;
        this.description = description;
    }

    public List<Object> getReminder() {
        return reminder;
    }




    public String getTime() {
        return time;
    }
    public int getId() {
        return ID;
    }





    public void setTime(String time) {
        this.time = time;
    }

    public void setReminder(List<Object> reminder) {
        this.reminder = reminder;
    }


    //constructor with all parameters

   public firebasemodel(String title, String description, String date, List<String> weather, String time, List<Double> location,List<Object> reminder, int ID){


  // public firebasemodel(String title, String description, String date, List<String> weather, String time, List<Double> location){

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.reminder=reminder;
        this.ID=ID;
    }



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