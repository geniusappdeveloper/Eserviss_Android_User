package com.app.taxisharingDriver.response;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PassengerAppointmentHistoryDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("apptDt")
	private String apptDt;
	@SerializedName("remarks")
	private String remarks;
	public String getApptDt() {
		return apptDt;
	}
	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
