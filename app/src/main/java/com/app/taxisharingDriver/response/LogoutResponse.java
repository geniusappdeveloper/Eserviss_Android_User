package com.app.taxisharingDriver.response;

public class LogoutResponse 
{
	
	
/*	    "errNum": "29",
	    "errFlag": "0",
	    "errMsg": "Logged out!",
	    "test": "55"*/
	    	
	private int errNum;
	private int errFlag;
	private int test;
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
	public int getTest() {
		return test;
	}
	public void setTest(int test) {
		this.test = test;
	}
	
	
}
