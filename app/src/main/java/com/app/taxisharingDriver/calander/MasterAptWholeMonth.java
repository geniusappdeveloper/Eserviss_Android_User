package com.app.taxisharingDriver.calander;

import java.util.ArrayList;

/**
 * Created by anubhootigupta on 19/11/15.
 */
public class MasterAptWholeMonth
{
    /* "date":"2015-08-07",
           "mmddyy":"08\/07\/15",
           "appt":[
           ]*/
    private String date;
    private ArrayList<AppointmentDtlsData> appt;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<AppointmentDtlsData> getAppt() {
        return appt;
    }

    public void setAppt(ArrayList<AppointmentDtlsData> appt) {
        this.appt = appt;
    }
}
