package com.app.taxisharingDriver.calander;

import java.io.Serializable;

public class AppointmentDtlsData implements Serializable
{
	/*"appt":[
{
"pPic":"aa_default_profile_pic.gif",
"email":"akbar@gmail.com",
"statCode":"9",
"status":"Booking completed.",
"fname":"Hshdh",
"apntTime":"12:03 pm",
"bid":"1454",
"apptDt":"2016-03-07 12:03:27",
"addrLine1":"47, 1st Main Rd, RBI Colony, Hebbal",
"payStatus":"1",
"dropLine1":"47, 1st Main Road, RBI Colony, Hebbal, Bengaluru, Karnataka 560024, India",
"duration":"2.42",
"distance":"0",
"amount":"25"
}
]*/


	private String pPic, email, statCode, status, fname;
	private String apntTime, bid, apptDt, addrLine1, payStatus;
	private String dropLine1, duration, distance, amount;

	public String getpPic() {
		return pPic;
	}

	public void setpPic(String pPic) {
		this.pPic = pPic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatCode() {
		return statCode;
	}

	public void setStatCode(String statCode) {
		this.statCode = statCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getApntTime() {
		return apntTime;
	}

	public void setApntTime(String apntTime) {
		this.apntTime = apntTime;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getApptDt() {
		return apptDt;
	}

	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getDropLine1() {
		return dropLine1;
	}

	public void setDropLine1(String dropLine1) {
		this.dropLine1 = dropLine1;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
