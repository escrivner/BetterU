package com.scrivner.healthhelper.Activities;

public class EntryObject {
    //The object type that helps me store the time and amount of calories entered
    //every time the user inputs calories.


    private int entry;
    private String time;

    public EntryObject(int entry, String time){
        this.entry = entry;
        this.time = time;
    }

    public int getEntry() {
        return entry;
    }

    public String getTime() {
        return time;
    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
