package com.app.taxisharingDriver.response;

import com.google.gson.annotations.SerializedName;

public class AppointmentData 
{
	@SerializedName("data")
	private AppointmentDetailData data;
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
    private  int errFlag;
	@SerializedName("errMsg")
    private String errMsg;
	public AppointmentDetailData getData() {
		return data;
	}
	public void setData(AppointmentDetailData data) {
		this.data = data;
	}
	public int getErrNum() {
		return errNum;
	}
	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}
	public int getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(int errFlag) {
		this.errFlag = errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
	
	
	/*"errNum": "21",
    "errFlag": "0",
    "errMsg": "Got the details!",
    "data": {
        "fName": "Abhishek",
        "lName": "Gupta",
        "mobile": "9738929033",
        "addr1": "RBI Colony, Jayanagar,Bangalore, Karnataka",
        "addr2": "Jayanagar",
        "dropAddr1": "31, 2nd Main Rd, Ganga Nagar Extension, Ganga Nagar,Bangalore, Karnataka 560032",
        "dropAddr2": "Ganga Nagar",
        "amount": "7.00",
        "pPic": "imageSat07122014161223.jpeg",
        "apptDis": "0",
        "dis": "14.05",
        "dur": "",
        "fare": "42.14",
        "pickLat": "12.9297",
        "pickLong": "77.5942",
        "dropLat": "13.020555",
        "dropLong": "77.58773",
        "apptDt": "2014-07-15 18:55:19",
        "pickupDt": "",
        "dropDt": "",
        "email": "abhishek@mobifyi.com",
        "discount": "0.00",
        "apptType": "1",
        "bid": "1236"*/
    


}
