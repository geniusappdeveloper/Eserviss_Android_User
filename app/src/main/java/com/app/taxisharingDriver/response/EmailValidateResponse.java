package com.app.taxisharingDriver.response;

public class EmailValidateResponse {
	private String errNum;
	private String errFlag;
	private String errMsg;
    private SignupResponse data;
    
	public SignupResponse getData() {
		return data;
	}

	public void setData(SignupResponse data) {
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
