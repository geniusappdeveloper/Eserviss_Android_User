package com.app.taxisharingDriver.response;

import com.google.gson.annotations.SerializedName;

public class PassengerAppointmentHistory {
	
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
	private int errFlag;
	@SerializedName("errMsg")
	private String  errMsg;
	@SerializedName("history")
	private java.util.ArrayList<PassengerAppointmentHistoryDetail> appointmentHisttoryDetail;
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
	public java.util.ArrayList<PassengerAppointmentHistoryDetail> getAppointmentHisttoryDetail() {
		return appointmentHisttoryDetail;
	}
	public void setAppointmentHisttoryDetail(
			java.util.ArrayList<PassengerAppointmentHistoryDetail> appointmentHisttoryDetail) {
		this.appointmentHisttoryDetail = appointmentHisttoryDetail;
	}

}
