package com.example.locationreminder;

public class firebasemodel {
    private String title;
    private String description;
    private String date;

    public firebasemodel(){

    }
    public firebasemodel(String title, String description, String date){
        this.title = title;
        this.description = description;
        this.date = date;
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