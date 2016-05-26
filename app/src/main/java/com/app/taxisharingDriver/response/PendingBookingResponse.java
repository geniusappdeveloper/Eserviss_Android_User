package com.app.taxisharingDriver.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class PendingBookingResponse 
{
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
	private  int errFlag;
	@SerializedName("errMsg")
	private String errMsg;
	
	@SerializedName("appointments")
	private ArrayList<PendingBookingDetailList>appointments;
	
	
	public ArrayList<PendingBookingDetailList> getAppointments() {
		return appointments;
	}
	public void setAppointments(ArrayList<PendingBookingDetailList> appointments) {
		this.appointments = appointments;
	}
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
	

}
