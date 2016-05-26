package com.app.taxisharingDriver.pojo;

import com.google.gson.annotations.SerializedName;

public class GetNumberOffMasterApointment 
{
	@SerializedName("date")
     private String date;
	@SerializedName("appt")
     private java.util.ArrayList<AppointmentDetailList>numberOfAppointmentOftheDay;
	public String getDate() 
	{
		return date;
	}
	public void setDate(String date) 
	{
		this.date = date;
	}
	public java.util.ArrayList<AppointmentDetailList> getNumberOfAppointmentOftheDay() 
	{
		return numberOfAppointmentOftheDay;
	}
	public void setNumberOfAppointmentOftheDay(
			java.util.ArrayList<AppointmentDetailList> numberOfAppointmentOftheDay) 
	{
		this.numberOfAppointmentOftheDay = numberOfAppointmentOftheDay;
	}
	
	
	
	
}
