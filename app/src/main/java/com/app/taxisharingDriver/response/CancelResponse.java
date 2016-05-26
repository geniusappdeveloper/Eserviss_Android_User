package com.app.taxisharingDriver.response;

public class CancelResponse
{
    /*"errNum":"75",
    "errFlag":"1",
    "errMsg":"Booking already completed!",
    "test":"75"*/
	private int errFlag;
	private int errNum;
	private String errMsg;
	
	public int getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(int errFlag) {
		this.errFlag = errFlag;
	}
	public int getErrNum() {
		return errNum;
	}
	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
}
