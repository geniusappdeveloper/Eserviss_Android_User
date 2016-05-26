package com.app.taxisharingDriver.pojo;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class GetWholeAppointmentDetail implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*"errNum":"31",
	"errFlag":"0",
	"errMsg":"Got Bookings!",
	"penCount":"0",
	"refIndex":[],
	"appointments":*/
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
	private int errFlag;
	@SerializedName("errMsg")
	private String errMsg;
	@SerializedName("penCount")
	private String penCount;
	
	@SerializedName("refIndex")
	private java.util.ArrayList<String >refIndex;
	@SerializedName("appointments")
	private java.util.ArrayList<GetNumberOffMasterApointment>numberOffAppointmentsList;
	
	
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
	public String getPenCount() {
		return penCount;
	}
	public void setPenCount(String penCount) {
		this.penCount = penCount;
	}
	public java.util.ArrayList<GetNumberOffMasterApointment> getNumberOffAppointmentsList() {
		return numberOffAppointmentsList;
	}
	public void setNumberOffAppointmentsList(
			java.util.ArrayList<GetNumberOffMasterApointment> numberOffAppointmentsList) {
		this.numberOffAppointmentsList = numberOffAppointmentsList;
	}
	public java.util.ArrayList<String> getRefIndex() {
		return refIndex;
	}
	public void setRefIndex(java.util.ArrayList<String> refIndex) {
		this.refIndex = refIndex;
	}
	
	

}
