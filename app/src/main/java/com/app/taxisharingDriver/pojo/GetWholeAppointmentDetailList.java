package com.app.taxisharingDriver.pojo;

import java.io.Serializable;
import java.util.ArrayList;

import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.google.gson.annotations.SerializedName;

public class GetWholeAppointmentDetailList implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<GetWholeAppointmentDetailList> liAppointmentDetailLists;
	
	private AppointmentDetailData appointmentDetailData;
   	
	
	public AppointmentDetailData getAppointmentDetailData() {
		return appointmentDetailData;
	}

	public void setAppointmentDetailData(AppointmentDetailData appointmentDetailData) {
		this.appointmentDetailData = appointmentDetailData;
	}
	
	public ArrayList<GetWholeAppointmentDetailList> getLiAppointmentDetailLists() {
		return liAppointmentDetailLists;
	}

	public void setLiAppointmentDetailLists(
			ArrayList<GetWholeAppointmentDetailList> liAppointmentDetailLists) {
		this.liAppointmentDetailLists = liAppointmentDetailLists;
	}
	
   private boolean isCalendershow;
	
	
	public boolean isCalendershow()
	{
		return isCalendershow;
	}
	public void setCalendershow(boolean isCalendershow)
	{
		this.isCalendershow = isCalendershow;
	}	
	
    /*"pPic":"imageThu07032014125142.jpeg",
    "email":"Test@roadyo.net",
    "statCode":"5",
    "status":"Driver is on the way.",
    "fname":"Test",
    "apntTime":"05:50 pm",
    "addrLine1":"49, Bellary Rd, Lakshmayya Layout, Vishveshvaraiah Nagar, Hebbal",
    "dropLine1":"730, 6th B Cross Rd, Koramangala 3 Block, Koramangala",
    "duration":"0",
    "distance":"0",
    "amount":"7"*/
	
	@SerializedName("pPic")
	private String pPic;
	@SerializedName("email")
	private String email;
	@SerializedName("statCode")
	private int statCode;
	@SerializedName("fname")
	private String fname;
	@SerializedName("apntTime")
	private String apntTime;
	@SerializedName("addrLine1")
	private String addrLine1;
	@SerializedName("dropLine1")
	private String dropLine1;
	@SerializedName("duration")
	private String duration;
	@SerializedName("distance")
	private String distance;
	@SerializedName("amount")
	private String amount;
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
	public int getStatCode() {
		return statCode;
	}
	public void setStatCode(int statCode) {
		this.statCode = statCode;
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
	public String getAddrLine1() {
		return addrLine1;
	}
	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
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
