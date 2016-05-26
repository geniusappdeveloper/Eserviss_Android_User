package com.app.taxisharingDriver.response;

public class UpdateAppointmentDetail 
{
	
	/*"errNum":"88",
    "errFlag":"0",
    "errMsg":"Details updated!",
    "calculatedAmount":"0.035",
    "apprAmount":"20"*/
	
	private int errNum;
	private int errFlag;
	private String errMsg;
	private String apprAmount;
	private String dis;
	private String avgSpeed;
	
	public String getDis() {
		return dis;
	}
	public void setDis(String dis) {
		this.dis = dis;
	}
	public String getAvgSpeed() {
		return avgSpeed;
	}
	public void setAvgSpeed(String avgSpeed) {
		this.avgSpeed = avgSpeed;
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
	
	public String getApprAmount() {
		return apprAmount;
	}
	public void setApprAmount(String apprAmount) {
		this.apprAmount = apprAmount;
	}

}
