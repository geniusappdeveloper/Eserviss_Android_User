package com.app.taxisharingDriver.calander;

import java.util.ArrayList;

/**
 * Created by anubhootigupta on 19/11/15.
 */
public class MasterAppointmentResponse
{
     /* errNum":"30",
        "errFlag":"1",
        "errMsg":"No appointments on this date.",
        "appointments":[*/

    /*"errNum":"31",
"errFlag":"0",
"errMsg":"Got Bookings!",
"penCount":"0",
"refIndex":[],
"appointments":[]*/

    private int errNum;
    private int errFlag;
    private String errMsg;

    private ArrayList<MasterAptWholeMonth> appointments;

    public int getErrNum() {
        return errNum;
    }

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public int getErrFlag() {
        return errFlag;
    }

    public void setErrFlag(int errFlag) {
        this.errFlag = errFlag;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public ArrayList<MasterAptWholeMonth> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<MasterAptWholeMonth> appointments) {
        this.appointments = appointments;
    }
}
