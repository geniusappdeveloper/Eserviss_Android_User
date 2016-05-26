package com.app.taxisharingDriver.response;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class AppointmentDetailData implements Serializable
{
	/*""data":{
    "code":"",
    "discount":"0.00",
    "tip":"0",
    "waitTime":"0",
    "statCode":"9",
    "meterFee":"25",
    "tollFee":"10",
    "airportFee":"10",
    "parkingFee":"10",
    "fName":"UTKARSH SINGH",
    "lName":"GAHARWAR",
    "mobile":"9980778980",
    "addr1":"RBI Colony, Ganganagar,Bengaluru, Karnataka 560024",
    "addr2":"Ganganagar",
    "dropAddr1":"54, RBI Colony, Hebbal, Bengaluru, Karnataka 560024, India",
    "dropAddr2":"",
    "amount":"55.00",
    "pPic":"PAUTKARSH SINGH201520022023201020072048temp_photo.jpg",
    "apptDis":"0.04",
    "dis":"0",
    "dur":"1",
    "fare":"55",
    "pickLat":"13.0289",
    "pickLong":"77.5897",
    "dropLat":"13.028878232881931",
    "dropLong":"77.58963716002609",
    "apptDt":"2015-03-04 16:19:00",
    "pickupDt":"2015-03-04 16:19:13",
    "dropDt":"2015-03-04 16:20:10",
    "email":"utkarsh000@yahoo.in",
    "apptType":"1",
    "bid":"6693",
    "pasChn":"qp_10CB2CBA-DC57-4691-A887-05A595D5B2D2",
    "payStatus":"1",
    "reportMsg":"",
    "payType":"2",
    "avgSpeed":"0",
    "apprAmount":"25"

}
    */
	
	private static final long serialVersionUID = 1L;
	
	@SerializedName("waitTime")
	private String waitTime;
	@SerializedName("meterFee")
	private String meterFee;
	@SerializedName("tollFee")
	private String tollFee;
	@SerializedName("airportFee")
	private String airportFee;
	@SerializedName("parkingFee")
	private String parkingFee;
	@SerializedName("tip")
	private String tip;
	
	
	
	
	
	
	
	
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	public String getMeterFee() {
		return meterFee;
	}
	public void setMeterFee(String meterFee) {
		this.meterFee = meterFee;
	}
	public String getTollFee() {
		return tollFee;
	}
	public void setTollFee(String tollFee) {
		this.tollFee = tollFee;
	}
	public String getAirportFee() {
		return airportFee;
	}
	public void setAirportFee(String airportFee) {
		this.airportFee = airportFee;
	}
	public String getParkingFee() {
		return parkingFee;
	}
	public void setParkingFee(String parkingFee) {
		this.parkingFee = parkingFee;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getDropDt() {
		return dropDt;
	}
	public void setDropDt(String dropDt) {
		this.dropDt = dropDt;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	@SerializedName("tipPercent")
	private String tipPercent;
	public String getTipPercent() {
		return tipPercent;
	}
	public void setTipPercent(String tipPercent) {
		this.tipPercent = tipPercent;
	}
	@SerializedName("fName")
	private String fName;
	@SerializedName("lName")
	private String lName;
	@SerializedName("mobile")
	private String mobile;
	@SerializedName("type")
	private String type;
	@SerializedName("addr1")
	private String addr1;
	@SerializedName("addr2")
	private String addr2;
	@SerializedName("dropAddr1")
	private String dropAddr1;
	@SerializedName("dropAddr2")
	private String dropAddr2;
	@SerializedName("amount")
	private String amount;
	@SerializedName("pPic")
	private String pPic;
	@SerializedName("apptDis")
	private String apptDis;
	@SerializedName("dis")
	private String dis;
	@SerializedName("dur")
	private String dur;
	@SerializedName("fare")
	private String fare;
	@SerializedName("pickLat")
	private double pickLat;
	@SerializedName("pickLong")
	private double pickLong;
	@SerializedName("dropLat")
	private double dropLat;
	@SerializedName("dropLong")
	private double dropLong;
	@SerializedName("apptDt")
	private String apptDt;
	@SerializedName("pickupDt")
	private String pickupDt;
	@SerializedName("dropDt")
	private String dropDt;
	@SerializedName("email")
	private String email;
	@SerializedName("discount")
	private double discount;
	@SerializedName("apptType")
	private String apptType;
	@SerializedName("bid")
	private String bid;
	@SerializedName("pasChn")
	private String pasChn;
	@SerializedName("payStatus")
	private String payStatus;
	@SerializedName("reportMsg")
	private String reportMsg;
	@SerializedName("status")
	private String status;
	@SerializedName("avgSpeed")
	private String avgSpeed;
	@SerializedName("rating")
	private float rating;
	@SerializedName("payType")
	private String payType;

	public String getExpireSec() {
		return expireSec;
	}

	public void setExpireSec(String expireSec) {
		this.expireSec = expireSec;
	}

	@SerializedName("expireSec")
	private String expireSec;

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReportMsg() {
		return reportMsg;
	}
	public void setReportMsg(String reportMsg) {
		this.reportMsg = reportMsg;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getPasChn() {
		return pasChn;
	}
	public void setPasChn(String pasChn) {
		this.pasChn = pasChn;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getDropAddr1() {
		return dropAddr1;
	}
	public void setDropAddr1(String dropAddr1) {
		this.dropAddr1 = dropAddr1;
	}
	public String getDropAddr2() {
		return dropAddr2;
	}
	public void setDropAddr2(String dropAddr2) {
		this.dropAddr2 = dropAddr2;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getpPic() {
		return pPic;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public String getApptDis() {
		return apptDis;
	}
	public void setApptDis(String apptDis) {
		this.apptDis = apptDis;
	}
	public String getDis() {
		return dis;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public String getDur() {
		return dur;
	}
	public void setDur(String dur) {
		this.dur = dur;
	}
	public String getFare() {
		return fare;
	}
	public void setFare(String fare) {
		this.fare = fare;
	}
	public double getPickLat() {
		return pickLat;
	}
	public void setPickLat(double pickLat) {
		this.pickLat = pickLat;
	}
	public double getPickLong() {
		return pickLong;
	}
	public void setPickLong(double pickLong) {
		this.pickLong = pickLong;
	}
	public double getDropLat() {
		return dropLat;
	}
	public void setDropLat(double dropLat) {
		this.dropLat = dropLat;
	}
	public double getDropLong() {
		return dropLong;
	}
	public void setDropLong(double dropLong) {
		this.dropLong = dropLong;
	}
	public String getApptDt() {
		return apptDt;
	}
	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}
	public String getPickupDt() {
		return pickupDt;
	}
	public void setPickupDt(String pickupDt) {
		this.pickupDt = pickupDt;
	}
	public String getDropoffDt() {
		return dropDt;
	}
	public void setDropoffDt(String pickupDt) {
		this.dropDt = pickupDt;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public String getApptType() {
		return apptType;
	}
	public void setApptType(String apptType) {
		this.apptType = apptType;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}

}
