package com.threembed.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager 
{
	 // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    
    public static final String LAT_DOW = "roadyo_lat_dow";
    
    public static final String LON_DOW = "roadyo_lon_dow";
    
    public static final String PREF_NAME = "roadyo_AndroidHivePref";

    public static final String REGISTRATION_ID = "roadyo_registration_id";

    public static final String SESSIONTOKEN= "roadyo_session_token";
    
    public static final String DEVICE_ID="roadyo_dev_id";
    
    public static final String IS_LOGIN = "roadyo_IsLoggedIn";
    
    public static final String MY_ID="roadyo_my_id";
    
    public static final String PAYLOAD="rejected";
    
    public static final String PUBNUB_CHANNEL = "roadyo_channel";

    public static final String IS_Driver_ON_WAY = "roadyo_IsDriverOnTheWay";
    
    public static final String IS_TRIP_BEGIN = "roadyo_IsDriverTripBegin";
    
    public static final String INVOICE_RAISED = "roadyo_invoice_raised";
    
    public static final String IS_Driver_ARRIVED = "roadyo_is_Driver_arrived";
    
    public static final String IS_Driver_Cancelled_apt = "roadyo_driver_cancelled_apt";
    
    public static final String Booking_Id = "roadyo_book_id";
    
    public static final String Appointment_Channel = "roadyo_apt_channel";
    
    public static final String Server_Channel="roadyo_server_channel";
    
    public static final String DOC_NAME_DOW = "roadyo_name";
    
    public static final String DOC_PH_DOW = "roadyo_ph_num";
    
    public static final String REJECTEDFROMADMIN = "admin";
    
    public static final String DOC_DIST_DOW = "roadyo_dist";

    public static final String DOC_PIC_DOW = "roadyo_pic_url";
    
    public static final String DOC_EMAIL = "roadyo_doc_email";
    
    public static final String APT_DATE = "roadyo_apt_date";
    
    public static final String Pickuplat="roadyo_pickuplat";
    
    public static final String PickupLng="roadyo_pickuplng";
    
    public static final String Dropofflat="roadyo_dropofflat";
    
    public static final String Dropofflng="roadyo_dropofflng";
    
    public static final String BOOKING_CANCELLED="roadyo_booking_cancelled";
    
    public static final String Car_Types = "roadyo_car_types";
    
    public static final String PlateNo="roadyo_plate_no";
    
    public static final String CarModel="roadyo_car_model";

    public static final String Driver_Rating="roadyo_rating";

    public static final String Currency_Symbol="roadyo_currency_symbol";
    
   public static final String Coupon_Code="roadyo_coupon";
   
   public static final String Share_Link="roadyo_share_link";
   

	// Constructor
    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }

    
    // rahul : comment
    
    public void storeShareLink(String model) 
    {
    	editor.putString(Share_Link, model);
    	editor.commit();
	}
    public String getShareLink() 
    {
		return pref.getString(Share_Link, null);
	}
    
    public void storeCouponCode(String model) 
    {
    	editor.putString(Coupon_Code, model);
    	editor.commit();
	}
    public String getCouponCode() 
    {
		return pref.getString(Coupon_Code, null);
	}
    
    public void storeCurrencySymbol(String model) 
    {
    	editor.putString(Currency_Symbol, model);
    	editor.commit();
	}
    
    public String getRejectedFromAdmin() 
    {
		return pref.getString(REJECTEDFROMADMIN, null);
	}
    
    public void setIsUserRejectedFromAdmin(String model) 
    {
    	editor.putString(REJECTEDFROMADMIN, model);
    	editor.commit();
	}
    
    public String getCurrencySymbol() 
    {
		return pref.getString(Currency_Symbol, null);
	} 
    public void storeDriverRating(String model) 
    {
    	editor.putString(Driver_Rating, model);
    	editor.commit();
	}




    public void setPayload(String model) 
    {
    	editor.putString(PAYLOAD, model);
    	editor.commit();
	}
    
    
    public String getDriverRating() 
    {
		return pref.getString(Driver_Rating, null);
	} 
    public void storeCarModel(String model) 
    {
    	editor.putString(CarModel, model);
    	editor.commit();
	}
    public void storePlateNO(String plateno)
    {
    	editor.putString(PlateNo, plateno);
    	editor.commit();
	}
    public String getPlateNO() {
		return pref.getString(PlateNo, null);
	}
	public void storeLat_DOW(String AptDate) 
    {
    	editor.putString(LAT_DOW, AptDate);
    	editor.commit();
	}
    public String getLat_DOW() {
		return pref.getString(LAT_DOW, null);
	}
    
    public void storeLon_DOW(String AptDate) 
    {
    	editor.putString(LON_DOW, AptDate);
    	editor.commit();
	}
    public String getLon_DOW() 
    {
		return pref.getString(LON_DOW, null);
	}
    public void storePickuplat(String pickuplat)
    {
    	Utility.printLog("Pickuplat in storePickuplat="+pickuplat);
    	editor.putString(Pickuplat, pickuplat);
    	editor.commit();
	}
    public String getPickuplat()
    {
    	Utility.printLog("Pickuplat in getPickuplat= "+pref.getString(Pickuplat, null));
		return pref.getString(Pickuplat, null);
	}
    public void storePickuplng(String pickupLng)
    {
    	Utility.printLog("pickupLng in storePickuplng="+pickupLng);
    	editor.putString(PickupLng, pickupLng);
    	editor.commit();
	}
    public String getPickuplng()
    {
    	Utility.printLog("pickupLng in getPickuplng= "+pref.getString(PickupLng, null));
		return pref.getString(PickupLng, null);
	}
    
    public void storeDropofflat(String DropofflatLng)
    {
    	editor.putString(Dropofflat, DropofflatLng);
    	editor.commit();
	}
    public String getDropofflat() 
    {
		return pref.getString(Dropofflat, null);
	}
    public void storeDropofflng(String DropofflatLng)
    {
    	editor.putString(Dropofflng, DropofflatLng);
    	editor.commit();
	}
    public String getDropofflng() 
    {
		return pref.getString(Dropofflng, null);
	}
    public void storeAptDate(String AptDate) 
    {
    	editor.putString(APT_DATE, AptDate);
    	editor.commit();
	}
    public String getAptDate() 
    {
		return pref.getString(APT_DATE, null);
	}
    
    public String getBookingId()
    {
        return pref.getString(Booking_Id, null);
    }
    
    public void storeBookingId(String value)
    {
    	editor.putString(Booking_Id, value);
    	editor.commit();
    }
    
    public String getCurrentAptChannel()
    {
        return pref.getString(Appointment_Channel, null);
    }
    
   public void storeCurrentAptChannel(String value)
    {
    	editor.putString(Appointment_Channel, value);
    	editor.commit();
    }
    
    public void storeDriverEmail(String EmailDoc) 
    {
    	editor.putString(DOC_EMAIL, EmailDoc);
    	editor.commit();
	}
    public String getDriverEmail() 
    {
		return pref.getString(DOC_EMAIL, null);
	}
    
    public void storeLoginId(String LoginId) 
    {
    	editor.putString(MY_ID, LoginId);
    	editor.commit();
	}
    public String getLoginId()
    {
		return pref.getString(MY_ID, null);
	}
    
    public void storeRegistrationId(String registrationId) 
    {
    	editor.putString(REGISTRATION_ID, registrationId);
    	editor.commit();
	}
    public String getRegistrationId() 
    {
		return pref.getString(REGISTRATION_ID, null);
	}
    
    public void storeSessionToken(String session) 
    {
    	editor.putString(SESSIONTOKEN,session);
    	editor.commit();
	}
    public String getSessionToken()
    {
		return pref.getString(SESSIONTOKEN,null);
	}
    
    public void storeDeviceId(String session) 
    {
    	editor.putString(DEVICE_ID,session);
    	editor.commit();
	}
    public String getDeviceId()
    {
		return pref.getString(DEVICE_ID,null);
	}


    public void storeServerChannel(String session) 
    {
    	editor.putString(Server_Channel,session);
    	editor.commit();
	}
    public String getServerChannel()
    {
		return pref.getString(Server_Channel,null);
	}
    
    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }
    
    public void setIsLogin(boolean value2)
    {
    	editor.putBoolean(IS_LOGIN, value2);
    	editor.commit();
    }
    
    public boolean isDriverCancelledApt()
    {
        return pref.getBoolean(IS_Driver_Cancelled_apt, false);
    }
    
    public void setDriverCancelledApt(boolean value2)
    {
    	editor.putBoolean(IS_Driver_Cancelled_apt, value2);
    	editor.commit();
    }
    
    public boolean isDriverOnWay()
    {
    	 Utility.printLog("Wallah is");
    	 boolean flag =pref.getBoolean(IS_Driver_ON_WAY, false);
    	 Utility.printLog("Wallah is"+ flag);
        return flag;
    }
    
    public void setDriverOnWay(boolean value1)
    {
    	Utility.printLog("Wallah set");
    	editor.putBoolean(IS_Driver_ON_WAY, value1);
    	editor.commit();
    	 boolean flag =pref.getBoolean(IS_Driver_ON_WAY, false);
    	 Utility.printLog("Wallah set as "+ flag);
    }
    
    
    public boolean isTripBegin()
    {
        return pref.getBoolean(IS_TRIP_BEGIN, false);
    }
    
    public void setTripBegin(boolean trip)
    {
    	editor.putBoolean(IS_TRIP_BEGIN, trip);
    	editor.commit();
    }
    
    public boolean isDriverOnArrived()
    {
        return pref.getBoolean(IS_Driver_ARRIVED, false);
    }
    
    public void setDriverArrived(boolean value3)
    {
    	editor.putBoolean(IS_Driver_ARRIVED, value3);
    	editor.commit();
    }
    
    public boolean isInvoiceRaised()
    {
        return pref.getBoolean(INVOICE_RAISED, false);
    }
    
    public void setInvoiceRaised(boolean value4)
    {
    	editor.putBoolean(INVOICE_RAISED, value4);
    	editor.commit();
    }
    public boolean isBookingCancelled()
    {
        return pref.getBoolean(BOOKING_CANCELLED, false);
    }
    
    public void setBookingCancelled(boolean cancel)
    {
    	editor.putBoolean(BOOKING_CANCELLED, cancel);
    	editor.commit();
    }
    public void storeDocName(String name) 
    {
    	editor.putString(DOC_NAME_DOW,name);
    	editor.commit();
	}
    public String getDocName() 
    {
		return pref.getString(DOC_NAME_DOW,null);
	}
    
    ////
    public void storeDocPH(String name) {
    	editor.putString(DOC_PH_DOW,name);
    	editor.commit();
	}
    public String getDocPH() {
		return pref.getString(DOC_PH_DOW,null);
	}
    
    ////
    public void storeDocDist(String name) {
    	editor.putString(DOC_DIST_DOW,name);
    	editor.commit();
	}

    
    ////
    public void storeDocPic(String name) {
    	editor.putString(DOC_PIC_DOW,name);
    	editor.commit();
	}
    public String getDocPic() {
		return pref.getString(DOC_PIC_DOW,null);
	}
    
    public void storeChannelName(String channel) {
    	editor.putString(PUBNUB_CHANNEL,channel);
    	editor.commit();
	}
    public String getChannelName() {
		return pref.getString(PUBNUB_CHANNEL,null);
	}

    public void storeCarTypes(String name)
    {
    	editor.putString(Car_Types,name);
    	editor.commit();
	}
    public String getCarTypes() 
    {
		return pref.getString(Car_Types,null);
	}

	public void clearSession()
	{
		editor.clear();
		editor.commit();
	}

    public String getLatitude()
    {
		return pref.getString(SESSIONTOKEN,null);
	}
	
}
