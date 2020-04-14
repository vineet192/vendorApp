package com.example.vendor;

public class schedule_dataholder {

    String date,time;

    public schedule_dataholder(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
