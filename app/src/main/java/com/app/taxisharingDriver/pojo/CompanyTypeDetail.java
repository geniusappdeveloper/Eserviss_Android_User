package com.app.taxisharingDriver.pojo;

import com.google.gson.annotations.SerializedName;

public class CompanyTypeDetail
{
	@SerializedName("company_id")
	  private String companyId;
	
	@SerializedName("companyname")
	  private String companyName;

	
	
	public String getCompanyId() 
	{
		return companyId;
	}
	public void setCompanyId(String companyId) 
	{
		this.companyId = companyId;
	}
	
	public String getCompanyname() 
		{
		return companyName;
	    }
	public void setCompanyname(String companyname) 
	{
		this.companyName = companyname;
	}
	
}
//[{"company_id":"30","companyname":"3Embed"},{"company_id":"33","companyname":"QuickTripz"}]}