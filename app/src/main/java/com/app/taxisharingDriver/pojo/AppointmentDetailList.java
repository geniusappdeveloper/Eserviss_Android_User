package com.app.taxisharingDriver.pojo;

import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

import com.app.taxisharingDriver.response.AppointmentDetailData;
import com.google.gson.annotations.SerializedName;

public class AppointmentDetailList implements Serializable
{
	private static final long serialVersionUID = 1L;
	private AppointmentDetailData appointmentDetailData;

	public AppointmentDetailData getAppointmentDetailData() {
		return appointmentDetailData;
	}

	public void setAppointmentDetailData(AppointmentDetailData appointmentDetailData)
	{
		this.appointmentDetailData = appointmentDetailData;
	}

	public static boolean ismDebugLog() {
		return mDebugLog;
	}

	public static void setmDebugLog(boolean mDebugLog) {
		AppointmentDetailList.mDebugLog = mDebugLog;
	}

	private static boolean mDebugLog = true;
   	private static String mDebugTag = "AppointmentDetailList";
   	private ArrayList<AppointmentDetailList> liAppointmentDetailLists;
   	
   	
	void logDebug(String msg)
	{
		
		if (mDebugLog) 
		{
			//Log.d(mDebugTag, msg);
		}
	}
	
	public ArrayList<AppointmentDetailList> getLiAppointmentDetailLists() {
		return liAppointmentDetailLists;
	}

	public void setLiAppointmentDetailLists(
			ArrayList<AppointmentDetailList> liAppointmentDetailLists) {
		this.liAppointmentDetailLists = liAppointmentDetailLists;
	}

	void logError(String msg)
	{
		
		if (mDebugLog) 
		{
			Log.e(mDebugTag, msg);
		}
	}
	
	private boolean isImonThewayPressed;
	private boolean isIhaveReachedPressed;
	private boolean isCompletedPressed;
	
	public boolean isImonThewayPressed()
	{
		return isImonThewayPressed;
	}
	public void setImonThewayPressed(boolean isImonThewayPressed) {
		this.isImonThewayPressed = isImonThewayPressed;
	}
	public boolean isIhaveReachedPressed() {
		return isIhaveReachedPressed;
	}
	public void setIhaveReachedPressed(boolean isIhaveReachedPressed) {
		this.isIhaveReachedPressed = isIhaveReachedPressed;
	}
	public boolean isCompletedPressed() {
		return isCompletedPressed;
	}
	public void setCompletedPressed(boolean isCompletedPressed) {
		this.isCompletedPressed = isCompletedPressed;
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
	
	/*@SerializedName("distance")
	private double distance;
	@SerializedName("status")
	private int status;
	@SerializedName("apntDt")
	private String apntDt;
	@SerializedName("pPic")
	private String pPic;
	@SerializedName("fname")
	private String fname;
	@SerializedName("phone")
	private String phone;
	@SerializedName("apntTime")
	private String apntTime;
	@SerializedName("apntDate")
	private String apntDate;
	@SerializedName("apptLat")
	private double apntLat;
	@SerializedName("apptLong")
	private double apntLong;
	@SerializedName("addrLine1")
	private String addrLine1;
	@SerializedName("addrLine2")
	private String addrLine2;
	@SerializedName("notes")
	private String notes;
	@SerializedName("email")
	private String email;
	@SerializedName("bookType")
	private String bookType;

	public String getBookType() {
		return bookType;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status)
	{
		logDebug("setStatus  status "+status);
		switch (status)
		{
		case 2:
			setImonThewayPressed(false);
			setIhaveReachedPressed(false);
			setCompletedPressed(false);
			break;

		case 5:
			setImonThewayPressed(true);
			setIhaveReachedPressed(false);
			setCompletedPressed(false);
			break;

		case 6:
			setImonThewayPressed(false);
			setIhaveReachedPressed(true);
			setCompletedPressed(false);
			break;
			
		case 7:
			setImonThewayPressed(false);
			setIhaveReachedPressed(false);
			setCompletedPressed(true);
		break;
		}
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddrLine1() {
		return addrLine1;
	}
	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}
	public String getAddrLine2() {
		return addrLine2;
	}
	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public double getApntLat() {
		return apntLat;
	}
	public void setApntLat(double apntLat) {
		this.apntLat = apntLat;
	}
	public double getApntLong() {
		return apntLong;
	}
	public void setApntLong(double apntLong) {
		this.apntLong = apntLong;
	}
	public String getApntDt()
	{
		return apntDt;
	}
	public void setApntDt(String apntDt) {
		this.apntDt = apntDt;
	}
	public String getpPic() {
		return pPic;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getApntTime() {
		return apntTime;
	}
	public void setApntTime(String apntTime) {
		this.apntTime = apntTime;
	}
	public String getApntDate() {
		return apntDate;
	}
	public void setApntDate(String apntDate) {
		this.apntDate = apntDate;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
	}*/
	
	
   /*"pPic":"imageThu09252014224050.jpeg",
    "email":"rahul@3embed.com",
    "statCode":"9",
    "status":"Booking completed.",
    "fname":"rahul",
    "apntTime":"08:41 pm",
    "bid":"6619",
    "apptDt":"2015-03-02 20:41:48",
    "addrLine1":"10th Cross St, RBI Colony, Ganganagar,Bengaluru, Karnataka 560024",
    "payStatus":"1",
    "dropLine1":"46, 1st Main Road, RBI Colony, Hebbal, Bengaluru, Karnataka 560024, India",
    "duration":"5.17",
    "distance":"0.29",
    "amount":"50"*/
	
	@SerializedName("pPic")
    private String pPic;
	@SerializedName("email")
    private String email;
	@SerializedName("statCode")
    private int statCode;
	@SerializedName("status")
    private String status;
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
	@SerializedName("payStatus")
	private int payStatus;
	@SerializedName("bid")
	private String bid;
	
	@SerializedName("apptDt")
	private String apptDt;

	public String getApptDt() {
		return apptDt;
	}

	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
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

	public int getStatCode() {
		return statCode;
	}

	public void setStatCode(int statCode) {
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
