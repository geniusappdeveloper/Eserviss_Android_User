package com.app.taxisharingDriver.response;

import com.google.gson.annotations.SerializedName;

public class ProfileData 
{
	@SerializedName("data")
	private ProfileDetailsData data;
	
	/*
	{
	    "errNum": "21",
	    "errFlag": "0",
	    "errMsg": "Got the details!",
	    "data": {
	        "fName": "Ãaa",
	        "lName": "Bbb",
	        "email": "sunil@3embed.com",
	        "type": "",
	        "mobile": "9876543218",
	        "status": "3",
	        "pPic": "",
	        "expertise": "",
	        "vehicleType": "",
	        "licNo": "",
	        "licExp": "",
	        "vehMake": " ",
	        "licPlateNum": "",
	        "seatCapacity": "",
	        "vehicleInsuranceNum": "",
	        "vehicleInsuranceExp": "",
	        "avgRate": "0",
	        "totRats": "0",
	        "cmpltApts": "0",
	        "todayAmt": "0",
	        "lastBilledAmt": "0",
	        "weekAmt": "0",
	        "monthAmt": "0",
	        "totalAmt": "0"
	    }
	}
	
	*/
	
	
	
	
	
	public ProfileDetailsData getData() {
		return data;
	}
	public void setData(ProfileDetailsData data) {
		this.data = data;
	}
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
private  int errFlag;
	@SerializedName("errMsg")
 private String errMsg;

	
	

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

	
	
}

/*"fName": "Ãaa",
"lName": "Bbb",
"email": "sunil@3embed.com",
"type": "",
"mobile": "9876543218",
"status": "3",
"pPic": "",
"expertise": "",
"vehicleType": "QuikTripz  Black",
"licNo": "",
"licExp": "",
"vehMake": "BMW M sedan black",
"licPlateNum": "KA 12 1234",
"seatCapacity": "4",
"vehicleInsuranceNum": "PL-124325123",
"vehicleInsuranceExp": "",
"avgRate": "0",
"totRats": "0",
"cmpltApts": "0",
"todayAmt": "0",
"lastBilledAmt": "0",
"weekAmt": "0",
"monthAmt": "0",
"totalAmt": "0"*/

