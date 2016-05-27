package com.eserviss.passenger.pojo;

import java.util.ArrayList;

public class AppointmentResponse {
	
 /*   "errNum": "31",
    "errFlag": "0",
    "errMsg": "Got Appointments!",
    "penCount": "3",
    "refIndex": [17,27,28],
    */
	
	String errNum;
	String errFlag;
	String errMsg;
	String penCount;
	ArrayList<String> refIndex;
	ArrayList<AppointmentList> appointments;
	
	public String getErrNum() {
		return errNum;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getPenCount() 
	{
		return penCount;
	}
	public void setPenCount(String penCount) {
		this.penCount = penCount;
	}
	public ArrayList<String> getRefIndex() {
		return refIndex;
	}
	public void setRefIndex(ArrayList<String> refIndex) {
		this.refIndex = refIndex;
	}
	public ArrayList<AppointmentList> getAppointments() {
		return appointments;
	}
	public void setAppointments(ArrayList<AppointmentList> appointments) {
		this.appointments = appointments;
	}
	
	
	

}
