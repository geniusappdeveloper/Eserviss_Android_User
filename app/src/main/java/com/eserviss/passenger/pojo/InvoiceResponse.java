package com.eserviss.passenger.pojo;

import com.threembed.utilities.Utility;

public class InvoiceResponse {
	
		
/*	{

    "errNum":"21",
    "errFlag":"0",
    "errMsg":"Got the details!",
    "code":"",
"discount":"0",
"tip":"0",
"tipPercent":"10",
"waitTime":"0",
"statCode":"9",
"meterFee":"20",
"tollFee":"0",
"airportFee":"0",
"parkingFee":"0",
    "fName":"Varun",
    "lName":"Driver",
    "mobile":"8123868425",
    "addr1":"54 RBI Colony",
    "addr2":"Hebbal",
    "dropAddr1":"54RBI Colony, HebbalBengaluru, Karnataka 560024",
    "dropAddr2":"",
    "amount":"563.00",
    "pPic":"imageSat08302014201828.jpeg",
    "dis":"0",
    "dur":"0.32",
    "fare":"563.00",
    "pickLat":"13.0208",
    "pickLong":"77.5897",
    "ltg":"13.02884374,77.58960277",
    "dropLat":"0",
    "dropLong":"0",
    "apptDt":"2014-09-04 14:44:57",
    "pickupDt":"2014-09-04 15:03:49",
    "dropDt":"2014-09-04 15:04:20",
    "discount":"0.00",
    "email":"Varun@driver.com",
    "dt":"20140904144457",
    "bid":"660",
    "apptType":"1",
    "chn":"qd_352136062663873",
    "plateNo":"ka04ev1966",
    "model":"BMW X1",
    "payStatus":"0",
    "reportMsg":"",
    "payType":"2",
    "avgSpeed":"0"
}
	*/
	
	String code,discount,tip,waitTime,statCode,meterFee,tollFee,airportFee,parkingFee,tipPercent;//,cc_fee
	String errNum;
	String errFlag;
	String errMsg;
	String fName;
	String lName;
	String mobile;
	String addr1;
	String addr2;
	String amount,pickupDt,dropDt,fare;
	String pPic,dropAddr1,dropAddr2,dis,dur,bid,avgSpeed,email,apptDt;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
/*	public String getCc_fee() {
		return cc_fee;
	}
	public void setCc_fee(String cc_fee) {
		this.cc_fee = cc_fee;
	}*/
	public String getApptDt() {
		return apptDt;
	}
	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}
	public String getTipPercent() {
		return tipPercent;
	}
	public void setTipPercent(String tipPercent) {
		this.tipPercent = tipPercent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}
	public String getStatCode() {
		return statCode;
	}
	public void setStatCode(String statCode) {
		this.statCode = statCode;
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
	public String getPickupDt() {
		return pickupDt;
	}
	public String getDropDt() {
		return dropDt;
	}
	public void setPickupDt(String pickupDt) {
		this.pickupDt = pickupDt;
	}
	public void setDropDt(String dropDt) {
		this.dropDt = dropDt;
	}
	public String getDropAddr1() {
		return dropAddr1;
	}
	public String getDropAddr2() {
		return dropAddr2;
	}
	public String getDis() {
		return dis;
	}
	public String getDur() {
		return dur;
	}
	public void setDropAddr1(String dropAddr1) {
		this.dropAddr1 = dropAddr1;
	}
	public void setDropAddr2(String dropAddr2) {
		this.dropAddr2 = dropAddr2;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public void setDur(String dur) {
		this.dur = dur;
	}
	public String getErrNum() {
		return errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public String getfName() {
		return fName;
	}
	public String getlName() {
		return lName;
	}
	public String getMobile() {
		return mobile;
	}
	public String getAddr1() {
		return addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public String getAmount() {
		return amount;
	}
	public String getpPic() {
		return pPic;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public void setAmount(String amount) {
		Utility.printLog("InvoiceResponse amount in setter="+amount);
		this.amount = amount;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public String getFare() {
		return fare;
	}
	public void setFare(String fare) {
		this.fare = fare;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
}
