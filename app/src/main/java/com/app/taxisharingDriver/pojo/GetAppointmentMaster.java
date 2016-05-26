package com.app.taxisharingDriver.pojo;

import com.google.gson.annotations.SerializedName;

public class GetAppointmentMaster
{	
	@SerializedName("penCount")
	private int penCount;
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
	private  int errFlag;
	@SerializedName("errMsg")
	private String errMsg;
	@SerializedName("refIndex")
	private String refIndex;
	@SerializedName("appointments")
	private java.util.ArrayList<GetNumberOffMasterApointment>numberOffAppointmentsList;
	
	public String getRefIndex() {
		return refIndex;
	}
	public void setRefIndex(String refIndex) {
		this.refIndex = refIndex;
	}
	public int getPenCount() 
	{
		return penCount;
	}
	public void setPenCount(int penCount) {
		this.penCount = penCount;
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
	public java.util.ArrayList<GetNumberOffMasterApointment> getNumberOffAppointmentsList() {
		return numberOffAppointmentsList;
	}
	public void setNumberOffAppointmentsList(
			java.util.ArrayList<GetNumberOffMasterApointment> numberOffAppointmentsList) {
		this.numberOffAppointmentsList = numberOffAppointmentsList;
	}
	
	
	
	
}
