package com.app.taxisharingDriver.response;

public class MasterStatusResponse 
{

	/*"errNum":"69",
	"errFlag":"0",
	"errMsg":"Status updated.",*/
	private int errNum;
	private int errFlag;
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
