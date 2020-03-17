package com.example.artemis;

public class TimetableEntry {
    private String name;
    private String date;
    private String startTime;
    private int duration;

    public TimetableEntry() {
        name = "";
        date = "";
        startTime = "";
        duration = 0;
    }

    public TimetableEntry(String name, String date, String startTime, int duration) {
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
