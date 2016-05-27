package com.eserviss.passenger.pojo;

import java.util.ArrayList;

public class AppointmentList {
	
/*	 "date": "2014-03-27",
         {
             "apntDt": "2014-03-27 14:30:00",
             "pPic": "myths_busted_doctor_tell_something_wrong_1850die-1850e30.jpg",
             "email": "logan@yahoo.com",
             "status": "2",
             "fname": "Logan",
             "phone": "",
             "apntTime": "14:30",
             "apntDate": "2014-03-27",
             "apptLat": 0,
             "apptLong": 0,
             "addrLine1": "hi",
             "addrLine2": "",
             "notes": ""
         }
     ]
 },*/
	
	String date;
	ArrayList<AppointmentDetails> appt;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public ArrayList<AppointmentDetails> getAppt() {
		return appt;
	}
	public void setAppt(ArrayList<AppointmentDetails> appt) {
		this.appt = appt;
	}
	
	
	

}
