package com.app.taxisharingDriver.response;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class AppointmentStatusResponse implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*"errNum":"49",
    "errFlag":"1",
    "errMsg":"Booking date or time not found.",
    "data":[
    ],
    "t":""*/
	private int errNum;
	private int errFlag;
	private String errMsg;
	private String masStatus;

	public String getMasStatus() {
		return masStatus;
	}

	public void setMasStatus(String masStatus) {
		this.masStatus = masStatus;
	}
	
	@SerializedName ("data")
	private ArrayList<AppointmentDetailData>data;

	public ArrayList<AppointmentDetailData> getData() {
		return data;
	}
	public void setData(ArrayList<AppointmentDetailData> data) {
		this.data = data;
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
