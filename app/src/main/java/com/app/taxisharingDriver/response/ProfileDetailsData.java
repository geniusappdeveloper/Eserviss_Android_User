package com.app.taxisharingDriver.response;

import com.google.gson.annotations.SerializedName;

public class ProfileDetailsData 
{
	
    /*"fName":"Nihar",
    "lName":"Ranjan",
    "email":"annu@gmail.com",
    "type":"",
    "mobile":"9876543219",
    "status":"3",
    "pPic":"DANihar2014200720282008204320578579temp_pic.jpg",
    "expertise":"",
    "vehicleType":"QuikTripz Black",
    "licNo":"",
    "licExp":"",
    "vehMake":"BMW X1 Black",
    "licPlateNum":"KA-55\/7894",
    "seatCapacity":"4",
    "vehicleInsuranceNum":"PL-54546546",
    "vehicleInsuranceExp":"",
    "avgRate":"5",
    "totRats":"2",
    "cmpltApts":"2",
    "todayAmt":"0",
    "lastBilledAmt":"20",
    "weekAmt":"40",
    "monthAmt":"0",
    "totalAmt":"40"*/
	
   /* "fName":"Nihar",
    "lName":"Ranjan",
    "email":"annu@gmail.com",
    "type":"",
    "mobile":"9876543219",
    "status":"3",
    "pPic":"DANihar2014200720282008204320578579temp_pic.jpg",
    "expertise":"",
    "vehicleType":" QuikTrpz Gold",
    "licNo":"",
    "licExp":"",
    "vehMake":"Audi X7 black",
    "licPlateNum":"KA 12 123412",
    "seatCapacity":"7",
    "vehicleInsuranceNum":"PL-23421412112",
    "vehicleInsuranceExp":"",
    "avgRate":"4.3",
    "totRats":"3",
    "cmpltApts":"3",
    "todayAmt":"0",
    "lastBilledAmt":"31.42",
    "weekAmt":"31.42",
    "monthAmt":"31.42",
    "totalAmt":"71.42"*/

	@SerializedName("vehicleInsuranceExp")
	private String vehicleInsuranceExp;
	@SerializedName("fName")
	 private String fName;
		@SerializedName("lName")
	 private String lName;
		@SerializedName("email")
	 private String email;
		@SerializedName("type")
	 private String type;
		@SerializedName("mobile")
	 private String mobile;
		@SerializedName("status")
	 private String status;
		@SerializedName("about")
	 private String about;
		@SerializedName("zip")
	 private String zip;
		@SerializedName("pPic")
	 private String pPic;
		@SerializedName("vehMake")
	private String vehMake;
		@SerializedName("vehicleType")
	private String vehicleType;
		@SerializedName("expertise")
	private String expertise;
		@SerializedName("licPlateNum")
	private String licPlateNum;
		@SerializedName("seatCapacity")
	private String seatCapacity;
		@SerializedName("vehicleInsuranceNum")
	private String vehicleInsuranceNum;
		@SerializedName("todayAmt")
	private String todayAmt;
		@SerializedName("cmpltApts")
	private String cmpltApts;
		@SerializedName("lastBilledAmt")
	private String lastBilledAmt;
		@SerializedName("weekAmt")
	private String weekAmt;
		@SerializedName("monthAmt")
	private String monthAmt;
		@SerializedName("totalAmt")
	private String totalAmt;
		@SerializedName("avgRate")
	private float avgRate;
		@SerializedName("licNo")
	private String licNo;
		@SerializedName("licExp")
	private String licExp;
		@SerializedName("totRats")
	private String totRats;

	public String getVehicleInsuranceExp() {
		return vehicleInsuranceExp;
	}

	public void setVehicleInsuranceExp(String vehicleInsuranceExp) {
		this.vehicleInsuranceExp = vehicleInsuranceExp;
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
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getAbout() {
			return about;
		}
		public void setAbout(String about) {
			this.about = about;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getpPic() {
			return pPic;
		}
		public void setpPic(String pPic) {
			this.pPic = pPic;
		}
		public String getVehMake() {
			return vehMake;
		}
		public void setVehMake(String vehMake) {
			this.vehMake = vehMake;
		}
		public String getVehicleType() {
			return vehicleType;
		}
		public void setVehicleType(String vehicleType) {
			this.vehicleType = vehicleType;
		}
		public String getExpertise() {
			return expertise;
		}
		public void setExpertise(String expertise) {
			this.expertise = expertise;
		}
		public String getLicPlateNum() {
			return licPlateNum;
		}
		public void setLicPlateNum(String licPlateNum) {
			this.licPlateNum = licPlateNum;
		}
		public String getSeatCapacity() {
			return seatCapacity;
		}
		public void setSeatCapacity(String seatCapacity) {
			this.seatCapacity = seatCapacity;
		}
		public String getVehicleInsuranceNum() {
			return vehicleInsuranceNum;
		}
		public void setVehicleInsuranceNum(String vehicleInsuranceNum) {
			this.vehicleInsuranceNum = vehicleInsuranceNum;
		}
		public String getTodayAmt() {
			return todayAmt;
		}
		public void setTodayAmt(String todayAmt) {
			this.todayAmt = todayAmt;
		}
		public String getCmpltApts() {
			return cmpltApts;
		}
		public void setCmpltApts(String cmpltApts) {
			this.cmpltApts = cmpltApts;
		}
		public String getLastBilledAmt() {
			return lastBilledAmt;
		}
		public void setLastBilledAmt(String lastBilledAmt) {
			this.lastBilledAmt = lastBilledAmt;
		}
		public String getWeekAmt() {
			return weekAmt;
		}
		public void setWeekAmt(String weekAmt) {
			this.weekAmt = weekAmt;
		}
		public String getMonthAmt() {
			return monthAmt;
		}
		public void setMonthAmt(String monthAmt) {
			this.monthAmt = monthAmt;
		}
		public String getTotalAmt() {
			return totalAmt;
		}
		public void setTotalAmt(String totalAmt) {
			this.totalAmt = totalAmt;
		}
		public float getAvgRate() {
			return avgRate;
		}
		public void setAvgRate(float avgRate) {
			this.avgRate = avgRate;
		}
		public String getLicNo() {
			return licNo;
		}
		public void setLicNo(String licNo) {
			this.licNo = licNo;
		}
		public String getLicExp() {
			return licExp;
		}
		public void setLicExp(String licExp) {
			this.licExp = licExp;
		}
		public String getTotRats() {
			return totRats;
		}
		public void setTotRats(String totRats) {
			this.totRats = totRats;
		}
		
		
}
