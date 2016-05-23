package com.eserviss.passenger.pojo;

import java.util.ArrayList;

public class StatusInformation 
{
	//{"errNum":"21","errFlag":"0","errMsg":"Got the details!","status":"8","bid":"1092","rateStatus":"1","payStatus":"0"}
	
	/*"data":[

	        {
	            "ltg":"13.028851345302,77.589615809125",
	            "bid":"1092",
	            "status":"8",
	            "fName":"Chetan",
	            "lName":"P",
	            "mobile":"8050815365",
	            "addr1":"46 1st Main Rd",
	            "addr2":"RBI Colony Hebbal Bengaluru Karnataka 560024",
	            "dropAddr1":"1327-1329, 14th Main Rd, Sunshine Colony, NS Palya, Stage 2, BTM Layout, Bengaluru, Karnataka 560076",
	            "dropAddr2":"",
	            "amount":"20.00",
	            "pPic":"",
	            "dur":"",
	            "pickLat":"13.0288",
	            "pickLong":"77.5896",
	            "dropLat":"12.912821680692117",
	            "dropLong":"77.60921861976385",
	            "apptDt":"2014-08-23 17:33:22",
	            "pickupDt":"2014-08-23 17:34:46",
	            "dropDt":"",
	            "email":"chetan2@roadyo.net",
	            "discount":"0.00",
	            "rateStatus":"2",
	            "payStatus":"0",
	            "reportMsg":""
	        }

	    ],*/
	
	/*{
    "errNum":"21",
    "errFlag":"0",
    "errMsg":"Got the details!",
    "status":"8",
    "bid":"757",
    "rateStatus":"1",
    "payStatus":"0",
    "pPic":"DASolo2014200920052011202420229633temp_pic.jpg",
    "mobile":"9784653120",
    "email":"solo@soloo.om",
    "fName":"Solo",
    "apptDt":"2014-09-05 20:16:29"
}*/
	
	String errNum,errFlag,errMsg,status,bid,rateStatus,payStatus,pPic,mobile,email,fName,apptDt;//status,bid,rateStatus,payStatus;
	ArrayList<StatusData> data;

	public String getErrNum() {
		return errNum;
	}

	public String getErrFlag() {
		return errFlag;
	}

	public String getErrMsg() {
		return errMsg;
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
	public ArrayList<StatusData> getData() {
		return data;
	}

	public void setData(ArrayList<StatusData> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getRateStatus() {
		return rateStatus;
	}

	public void setRateStatus(String rateStatus) {
		this.rateStatus = rateStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getpPic() {
		return pPic;
	}

	public void setpPic(String pPic) {
		this.pPic = pPic;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getApptDt() {
		return apptDt;
	}

	public void setApptDt(String apptDt) {
		this.apptDt = apptDt;
	}

	
}
