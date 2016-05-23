package com.eserviss.passenger.pojo;

public class GetAppointmentDetails {
	/*{

	    "errNum":"21",
	    "errFlag":"0",
	    "errMsg":"Got the details!",
	    "fName":"Chetan",
	    "lName":"P",
	    "mobile":"8050815365",
	    "addr1":"46 1st Main Rd",
	    "addr2":"RBI Colony Hebbal Bengaluru Karnataka 560024",
	    "dropAddr1":"1327-1329, 14th Main Rd, Sunshine Colony, NS Palya, Stage 2, BTM Layout, Bengaluru, Karnataka 560076",
	    "dropAddr2":"",
	    "amount":"20.00",
	    "pPic":"aa_default_profile_pic.gif",
	    "dis":"0",
	    "dur":"",
	    "fare":"20.00",
	    "pickLat":"13.0288",
	    "pickLong":"77.5896",
	    "ltg":"13.028866741087,77.589632739063",
	    "dropLat":"12.912821680692117",
	    "dropLong":"77.60921861976385",
	    "apptDt":"20140823173322",
	    "pickupDt":"2014-08-23 17:34:46",
	    "dropDt":"",
	    "discount":"0.00",
	    "email":"chetan2@roadyo.net",
	    "dt":"20140823173322",
	    "bid":"1092",
	    "apptType":"1",
	    "chn":"qd_355798054645866",
	    "plateNo":"466",
	    "model":"BMW M sedan",
	    "payStatus":"1",
	    "reportMsg":""
	    "share":"http:\/\/107.170.66.211\/roadyo_live\/Wko8TuOH\/track.php?id=6103"

	}*/
	
	String errNum,errFlag,errMsg,fName,lName,mobile,addr1,addr2,dropAddr1,dropAddr2,amount,bid,chn,plateNo,model,payStatus,reportMsg;
	String pPic,dis,dur,fare,pickLat,pickLong,dropLat,dropLong,apptDt,pickupDt,dropDt,discount,email,dt,id,apptType,share;
	
	
	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrNum() {
		return errNum;
	}
	public String getErrFlag() {
		return errFlag;
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
	public String getDropAddr1() {
		return dropAddr1;
	}
	public String getDropAddr2() {
		return dropAddr2;
	}
	public String getAmount() {
		return amount;
	}
	public String getpPic() {
		return pPic;
	}
	public String getDis() {
		return dis;
	}
	public String getDur() {
		return dur;
	}
	public String getFare() {
		return fare;
	}
	public String getPickLat() {
		return pickLat;
	}
	public String getPickLong() {
		return pickLong;
	}
	public String getDropLat() {
		return dropLat;
	}
	public String getDropLong() {
		return dropLong;
	}
	public String getApptDt() {
		return apptDt;
	}
	public String getPickupDt() {
		return pickupDt;
	}
	public String getDropDt() {
		return dropDt;
	}
	public String getDiscount() {
		return discount;
	}
	public String getDt() {
		return dt;
	}
	public String getId() {
		return id;
	}
	public String getApptType() {
		return apptType;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
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
	public void setDropAddr1(String dropAddr1) {
		this.dropAddr1 = dropAddr1;
	}
	public void setDropAddr2(String dropAddr2) {
		this.dropAddr2 = dropAddr2;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public void setpPic(String pPic) {
		this.pPic = pPic;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public void setDur(String dur) {
		this.dur = dur;
	}
	public void setFare(String fare) {
		this.fare = fare;
	}
	public void setPickLat(String pickLat) {
		this.pickLat = pickLat;
	}
	public void setPickLong(String pickLong) {
		this.pickLong = pickLong;
	}
	public void setDropLat(String dropLat) {
		this.dropLat = dropLat;
	}
	public void setDropLong(String dropLong) {
		this.dropLong = dropLong;
	}
	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}
	public void setPickupDt(String pickupDt) {
		this.pickupDt = pickupDt;
	}
	public void setDropDt(String dropDt) {
		this.dropDt = dropDt;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setApptType(String apptType) {
		this.apptType = apptType;
	}
	public String getBid() {
		return bid;
	}
	public String getChn() {
		return chn;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public String getModel() {
		return model;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public String getReportMsg() {
		return reportMsg;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public void setChn(String chn) {
		this.chn = chn;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public void setReportMsg(String reportMsg) {
		this.reportMsg = reportMsg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
