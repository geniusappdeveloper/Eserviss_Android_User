package com.app.taxisharingDriver.pojo;

import com.google.gson.annotations.SerializedName;

public class CompanyType 
{
	@SerializedName("types")
	java.util.ArrayList<CompanyTypeDetail>companyTypeDetailList;
	public java.util.ArrayList<CompanyTypeDetail> getCompanyTypeDeataiList() {
		return companyTypeDetailList;
	}

	public void setCompanyTypeDeataiList(
		java.util.ArrayList<CompanyTypeDetail> companyTypeDetailsList)
	{
		this.companyTypeDetailList = companyTypeDetailsList;
	}
	
	private String errFlag;
	private String errNum;
	private String errMsg;
	
	public String getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(String errFlag) {
		this.errFlag = errFlag;
	}
	public String getErrNum() {
		return errNum;
	}
	public void setErrNum(String errNum) {
		this.errNum = errNum;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
}
