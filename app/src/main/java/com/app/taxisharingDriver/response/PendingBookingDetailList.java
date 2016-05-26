package com.app.taxisharingDriver.response;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PendingBookingDetailList implements Serializable
{

	/**
	 "bid":"3254",
    "pPic":"PAASHISH201420112015201220272053temp_photo.jpg",
    "email":"ashish23@gmail.com",
    "statCode":"1",
    "status":"Booking requested",
    "fname":"ASHISH",
    "apntTime":"06:02 pm",
    "apntDt":"2014-11-15 18:02:00",
    "mobile":"9781653201",
    "addrLine1":"Vishveshvaraiah Nagar, Ganganagar, Bengaluru, Karnataka, India",
    "payStatus":"0",
    "apptLat":"13.0273",
    "apptLong":"77.5893",
    "dropLine1":"Koramangala Layout, Bangalore, Karnataka, India",
    "duration":"0",
    "distance":"0",
    "amount":"40"
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("bid")
	private String bid;
	@SerializedName("pPic")
	private String pPic;
	@SerializedName("email")
	private String email;
	@SerializedName("statCode")
	private String statCode;
	@SerializedName("status")
	private String status;
	@SerializedName("fname")
	private String fname;
	@SerializedName("apntTime")
	private String apntTime;
	@SerializedName("apntDt")
	private String apntDt;
	@SerializedName("mobile")
	private String mobile;
	@SerializedName("addrLine1")
	private String addrLine1;
	@SerializedName("payStatus")
	private String payStatus;
	@SerializedName("apptLat")
	private String apptLat;
	@SerializedName("apptLong")
	private String apptLong;
	@SerializedName("dropLine1")
	private String dropLine1;
	@SerializedName("duration")
	private String duration;
	@SerializedName("distance")
	private String distance;
	@SerializedName("amount")
	private String amount;
	
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
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
	public String getApntDt() {
		return apntDt;
	}
	public void setApntDt(String apntDt) {
		this.apntDt = apntDt;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getApptLat() {
		return apptLat;
	}
	public void setApptLat(String apptLat) {
		this.apptLat = apptLat;
	}
	public String getApptLong() {
		return apptLong;
	}
	public void setApptLong(String apptLong) {
		this.apptLong = apptLong;
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
