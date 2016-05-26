package com.app.taxisharingDriver.response;

public class LoginResponse 
{
	

	private String errNum;
	private String errFlag;
	private String errMsg;
	
	private LoginResponseDetails data;
	
	
	public LoginResponseDetails getData() {
		return data;
	}
	public void setData(LoginResponseDetails data) {
		this.data = data;
	}
	
	
	public String getErrNum() {
		return errNum;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public String getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	
	

}
