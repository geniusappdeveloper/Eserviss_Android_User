package com.app.taxisharingDriver.response;

import com.google.gson.annotations.SerializedName;

public class UpdateAppointMentstatus
{

	/* "errNum": "57",
	    "errFlag": "0",
	    "errMsg": "Status updated as on the way.",
	    "test": {
	        "push": {
	            "nt": "3",
	            "d": "2014-07-15 17:50:00",
	            "n": "Ãaa Bbb",
	            "dt": "20140715175000",
	            "pic": "aa_default_profile_pic.gif",
	            "ph": "9876543218",
	            "e": "sunil@3embed.com",
	            "ltg": "13.028800,77.589600",
	            "r": "0.00",
	            "sound": "default",
	            "id": "1667"
	        },
	        "mail": [],
	        "charge"UpdateAppointmentPush: "",
	        "transfer": ""*/
	@SerializedName("errNum")
	private int errNum;
	@SerializedName("errFlag")
	private int errFlag;
	@SerializedName("errMsg")
	private String errMsg;
	
	public int getErrNum() 
	{
		return errNum;
	}
	public void setErrNum(int errNum) 
	{
		this.errNum = errNum;
	}
	public int getErrFlag() 
	{
		return errFlag;
	}
	public void setErrFlag(int errFlag) 
	{
		this.errFlag = errFlag;
	}
	public String getErrMsg() 
	{
		return errMsg;
	}
	public void setErrMsg(String errMsg) 
	{
		this.errMsg = errMsg;
	}
	
	
}
