package com.eserviss.passenger.pojo;

public class VerificationCodeResponse
{
/*
{"errNum":"107","errFlag":"0","errMsg":"Code sent.","test":"1"}
 */
	
	
	String errNum,errFlag,errMsg,test;

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

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
	
}
