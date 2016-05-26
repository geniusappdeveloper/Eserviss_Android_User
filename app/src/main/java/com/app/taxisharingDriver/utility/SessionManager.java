package com.app.taxisharingDriver.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("CommitPrefEdits")
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
   

    public static final String REGISTRATION_ID = "registration_id";

    public static final String SESSIONTOKEN= "session_token";
    private String IS_USER_LOGDING="isuserlogdind";
    private static final String LAT_DOW = "lat_dow";
  //  private static final String PREF_NAME = "AndroidHivePref";
    private static final String LON_DOW = "lon_dow";
    public static final String DEVICE_ID="dev_id";
    
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_LOGOUT = "IsLogedOut";
    private static final String MY_ID="my_id";
    
    private static final String PUBNUB_CHANNEL = "channel";
    
    private static final String PUBNUB_EMAIL = "email_pubnub";
    private static final String PUBNUB_LISTNER_CHANNEL = "listner_channel";
    private static final String PUBNUB_SUBSCRIBE_CHANNEL = "subscribe_channel";
    
    private static final String APT_DATE = "DATE";
    private static final String BOOKING_ID = "ID";
    private static final String PASSENGER_EMAIL = "email";
    private static final String TIMEWHILE_PAUSED="pauseTime";
    private static final String ELAPSED_TIME="elapsedTime";
    private static final String TIMEWHILE_PAUSED2="pauseTime2";
    private static final String ELAPSED_TIME2="elapsedTime2";
    private static final String TIMEWHILE_PAUSED3="pauseTime3";
    private static final String ELAPSED_TIME3="elapsedTime3";

    private static final String UserRejectedFromAdmin="false";

    // Constructor
    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(VariableConstants.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }




    public void setBookingIdStatus(String BookingIdStatus) {
        editor.putString("BookingIdStatus", BookingIdStatus);
        editor.commit();
    }
    public String getBookingIdStatus()
    {
        return pref.getString("BookingIdStatus", "");
    }

    public void setIsUserRejectedFromAdmin(String value) {
        editor.putString(UserRejectedFromAdmin, value);
        editor.commit();
    }
    public String getIsUserRejectedFromAdmin()
    {
        return pref.getString(UserRejectedFromAdmin,"false");
    }
    
    public void setElapsedTime(int elapsedTime) {
    	editor.putInt(ELAPSED_TIME, elapsedTime);
    	editor.commit();
	}
    public int getElapsedTime()
    {
    	return pref.getInt(ELAPSED_TIME, -1);
    }
    
    public void setTimeWhile_Paused(String timePaused) 
    {
    	editor.putString(TIMEWHILE_PAUSED, timePaused);
    	editor.commit();
	}
    public String getTimeWhile_Paused()
    {
    	return pref.getString(TIMEWHILE_PAUSED, "-1");
    }
    
    public void setElapsedTime2(int elapsedTime) {
    	editor.putInt(ELAPSED_TIME2, elapsedTime);
    	editor.commit();
	}
    public int getElapsedTime2()
    {
    	return pref.getInt(ELAPSED_TIME2, -1);
    }
    
    public void setTimeWhile_Paused2(String timePaused) 
    {
    	editor.putString(TIMEWHILE_PAUSED2, timePaused);
    	editor.commit();
	}
    public String getTimeWhile_Paused2()
    {
    	return pref.getString(TIMEWHILE_PAUSED2, "-1");
    }
    public void setElapsedTime3(int elapsedTime) {
    	editor.putInt(ELAPSED_TIME3, elapsedTime);
    	editor.commit();
	}
    public int getElapsedTime3()
    {
    	return pref.getInt(ELAPSED_TIME3, -1);
    }
    public void setACTION(String ACTION) {
    	editor.putString("ACTION", ACTION);
    	editor.commit();
	}
    public String getACTION()
    {
    	return pref.getString("ACTION", "");
    }
    public void setTimeWhile_Paused3(String timePaused) 
    {
    	editor.putString(TIMEWHILE_PAUSED3, timePaused);
    	editor.commit();
	}
    public String getTimeWhile_Paused3()
    {
    	return pref.getString(TIMEWHILE_PAUSED3, "-1");
    }
    
    public void setAPT_DATE(String apt_date) {
    	editor.putString(APT_DATE, apt_date);
    	editor.commit();
	}
    public String getAPT_DATE()
    {
    	return pref.getString(APT_DATE, "");
    }
    
    public void setAPPT_DATE(String APPT_DATE) {
    	editor.putString("APPT_DATE", APPT_DATE);
    	editor.commit();
	}
    public String getAPPT_DATE()
    {
    	return pref.getString("APPT_DATE", "");
    }
    
    
    public void setDropAddress(String DropAddress) {
    	editor.putString("DropAddress", DropAddress);
    	editor.commit();
	}
    public String getDropAddress()
    {
    	return pref.getString("DropAddress", "");
    }
    
    public void setBOOKING_ID(String booking_id) {
    	editor.putString(BOOKING_ID, booking_id);
    	editor.commit();
	}
    
    public void setPayload(String Payload) {
    	editor.putString("Payload", Payload);
    	editor.commit();
	}
    public String getPayload()
    {
    	return pref.getString("Payload", "");
    }
    
    public String getBOOKING_ID()
    {
    	return pref.getString(BOOKING_ID, "");
    }
    
    public void setPASSENGER_EMAIL(String email) {
    	editor.putString(PASSENGER_EMAIL, email);
    	editor.commit();
	}
    public String getPASSENGER_EMAIL()
    {
    	return pref.getString(PASSENGER_EMAIL, "");
    }
    
    public void setListnerChannel(String channel) {
    	editor.putString(PUBNUB_LISTNER_CHANNEL, channel);
    	editor.commit();
	}
    public String getListerChannel()
    {
    	return pref.getString(PUBNUB_LISTNER_CHANNEL, null);
    }
    public void setSubscribeChannel(String channel) {
    	editor.putString(PUBNUB_SUBSCRIBE_CHANNEL, channel);
    	editor.commit();
	}
    public String getSubscribeChannel()
    {
    	return pref.getString(PUBNUB_SUBSCRIBE_CHANNEL, null);
    }
    
    public void setDriverName(String driverName)
    {
    	editor.putString("driverName", driverName);
    	editor.commit();
    }
    public String getDriverName()
    {
    	return pref.getString("driverName", "Test");
    }
    
    public void setDriverProfilePic(String driverProfilePic)
    {
    	editor.putString("driverProfilePic", driverProfilePic);
    	editor.commit();
    }
    public String getDriverProfilePic()
    {
    	return pref.getString("driverProfilePic", "");
    }
    
    public void setDriverRating(float driverRating)
    {
    	editor.putFloat("driverRating", driverRating);
    	editor.commit();
    }
    public float getDriverRating()
    {
    	return pref.getFloat("driverRating", 0.0f);
    }
    public void storeUserEmailid(String emailid) 
    {
    	editor.putString("Emailid", emailid);
    	editor.commit();
	}
    public String getUserEmailid() 
    {
		return pref.getString("Emailid", null);
	}
    
    
    
    public void storeVehTypeId(String VehTypeId) 
    {
    	editor.putString("VehTypeId", VehTypeId);
    	editor.commit();
	}
    public String getVehTypeId() 
    {
		return pref.getString("VehTypeId", null);
	}

    public void setIsHomeFragmentisOpened(boolean isHomeFragmentIsOpend)
    {
    	editor.putBoolean("HOMEFRAGMENT_ISOPEND", isHomeFragmentIsOpend);
    	editor.commit();
    }
    
    public boolean isHomeIsOpend()
    {
    	return  pref.getBoolean("HOMEFRAGMENT_ISOPEND", false);
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
    public String getSessionToken() {
		return pref.getString(SESSIONTOKEN,null);
	}
    public void createSession()
    {
    	editor.putBoolean(IS_USER_LOGDING, true);
    	editor.commit();
    }
    public boolean isUserLogdIn()
    {
    	return pref.getBoolean(IS_USER_LOGDING, false);
    }
   
    public void storeLat_DOW(String AptDate) {
    	editor.putString(LAT_DOW, AptDate);
    	editor.commit();
	}
    public String getLat_DOW() {
		return pref.getString(LAT_DOW, null);
	}
    
    public void storeLon_DOW(String AptDate) {
    	editor.putString(LON_DOW, AptDate);
    	editor.commit();
	}
    public String getLon_DOW() {
		return pref.getString(LON_DOW, null);
	}
    
    public void storeLoginId(String LoginId) {
    	editor.putString(MY_ID, LoginId);
    	editor.commit();
	}
    public String getLoginId() {
		return pref.getString(MY_ID, null);
	}
    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);
    }
    
    public void setIsLogin(boolean value)
    {
    	editor.putBoolean(IS_LOGIN, value);
    	editor.commit();
    }
    public boolean isLoggedOut()
    {
        return pref.getBoolean(IS_LOGOUT, false);
    }
    
    public void setIsLogout(boolean value)
    {
    	editor.putBoolean(IS_LOGOUT, value);
    	editor.commit();
    }
     
  public void storeUserEmail(String email) {
    	editor.putString(PUBNUB_EMAIL,email);
    	editor.commit();
	}
    public String getUserEmail() {
		return pref.getString(PUBNUB_EMAIL,null);
	}
    
    public void storeDeviceId(String session) {
    	editor.putString(DEVICE_ID,session);
    	editor.commit();
	}
    public String getDeviceId() {
		return pref.getString(DEVICE_ID,null);
	}
    
    public void logoutUser()
    {

    	editor.putBoolean(IS_USER_LOGDING, false);
    	editor.clear();
    	editor.commit();
    }
    public boolean getIsAppointmentaccepted()
    {
    	return pref.getBoolean("ISAPPOINTMENTACCEPT", false);
    }
    public void setIsAppointmentAccept(boolean isAccept)
    {
    	editor.putBoolean("ISAPPOINTMENTACCEPT", isAccept);
    	editor.commit();
    }
    
    public boolean getIsAppointmentacceptedFromDetail()
    {
    	return pref.getBoolean("ISAPPOINTMENTACCEPTFROMDETAIL", false);
    }
    public void setIsAppointmentAcceptFromDetail(boolean isAccept)
    {
    	editor.putBoolean("ISAPPOINTMENTACCEPTFROMDETAIL", isAccept);
    	editor.commit();
    }
    
    
    
    public boolean getIsPressedImonthewayorihvreached()
    {
    	return pref.getBoolean("ispressedimonthewayoihavereached", false);
    }
    public void setIsPressedImonthewayorihvreached(boolean isAccept)
    {
    	editor.putBoolean("ispressedimonthewayoihavereached", isAccept);
    	editor.commit();
    }
    
    public boolean getBeginJourney()
    {
    	return pref.getBoolean("isBeginJourney", false);
    }
    public void setBeginJourney(boolean isAccept)
    {
    	editor.putBoolean("isBeginJourney", isAccept);
    	editor.commit();
    }
    
    
    public boolean getCancelTrip()
    {
    	return pref.getBoolean("CancelTrip", false);
    }
    public void setCancelTrip(boolean cancelTrip)
    {
    	editor.putBoolean("CancelTrip", cancelTrip);
    	editor.commit();
    }
    
    
    public void setCancelReason(String cancelReason) {
    	editor.putString("cancelReason", cancelReason);
    	editor.commit();
	}
    public String getCancelReason()
    {
    	return pref.getString("cancelReason", "");
    }
    
    
    public boolean getIsPassengerDropped()
    {
    	return pref.getBoolean("IsPassengerDropped", false);
    }
    public void setIsPassengerDropped(boolean IsPassengerDropped)
    {
    	editor.putBoolean("IsPassengerDropped", IsPassengerDropped);
    	editor.commit();
    }
    
    public void setIamOnTheWayChannel(String iamthewayChannel)
    {
    	editor.putString("iamthewayChannel", iamthewayChannel);
    	editor.commit();
    }
    public String getIamOnTheWayChannel()
    {
    	return pref.getString("iamthewayChannel", "");
    }
    
    public void setIsDriverOnIamOntheWay(boolean isDocOnTheway)
    {
    	editor.putBoolean("isDriverOnTheway", isDocOnTheway);
    	editor.commit();
    }

    public boolean getIsDriverOnIamOntheWay()
    {
    	return pref.getBoolean("isDriverOnTheway", false);
    }
    public void seIsDriverReached(boolean isDriverReached)
    {
    	editor.putBoolean("isDriverReached", isDriverReached);
    	editor.commit();
    }
    public boolean getseIsDriverReached()
    {
    	return pref.getBoolean("isDriverReached", false);
    }
    
    public void setFirstScreen(boolean bFlag)
    {
    	editor.putBoolean("isPaddingAppointmentOpeend", bFlag);
    	editor.commit();
	}
    public boolean isFirstScreen()
    {
    	return pref.getBoolean("isPaddingAppointmentOpeend", false);
    }
    
    public boolean isNotificationSend(String PatientEmailid)
    {
    	return pref.getBoolean(PatientEmailid, false);
    	
    }
    public void setIsNotiFicationSend(String PateintEmailid,boolean isNoticationSend)
    {
    	editor.putBoolean(PateintEmailid, isNoticationSend);
    	editor.commit();
    }
    
    public boolean isIhaveReachedNotificationsend(String ihaveReached)
    {
    	return pref.getBoolean(ihaveReached, false);
    }
    public void setIsIhaveReached(String PateintEmailid,boolean isNoticationSend)
    {
    	editor.putBoolean(PateintEmailid, isNoticationSend);
    	editor.commit();
    }
    
    public void setselectedIndexAccepteIndex(int selectedindex)
    {
    	editor.putInt("setselectedIndexAccepteIndex", selectedindex);
    	editor.commit();
    }
    public int getselectedIndexAccepteIndex()
    {
    	return pref.getInt("setselectedIndexAccepteIndex", -1);
    }
    
    public void setindexofSelectedAppointment(int index)
    {
    	editor.putInt("selectedapointmentindex", index);
    	editor.commit();
    }
    public int getSelectedAppointmentIndex()
    {
    	return pref.getInt("selectedapointmentindex", -1);
    }
    
    public void setindexofSelectedList(int index)
    {
    	editor.putInt("selectedlistindex", index);
    	editor.commit();
    }
    public int getSelectedListIndex()
    {
    	return pref.getInt("selectedlistindex", -1);
    }
    
    public void setAppiontmentStatus(int status)
    {
    	editor.putInt("Appointmentstatus", status);
    	editor.commit();
    }
    public int getAppiontmentStatus()
    {
    	return pref.getInt("Appointmentstatus", -1);
    }
    
   /* public void setOnOffStatus(int status)
    {
    	editor.putInt("OnOffStatus", status);
    	editor.commit();
    }
    public int getOnOffStatus()
    {
    	return pref.getInt("OnOffStatus", -1);
    }*/
    
    public void setIsPendingScreenFinish(boolean bFlag)
    {
    	editor.putBoolean("setIsPendingScreenFinish", bFlag);
    	editor.commit();
	}
    public boolean getIsPendingScreenFinish()
    {
    	return pref.getBoolean("setIsPendingScreenFinish", false);
    }
    
    
    public void setIsPendingScreenOnCreateCalled(boolean bFlag)
    {
    	editor.putBoolean("setIsPendingScreenOnCreateCalled", bFlag);
    	editor.commit();
	}
    public boolean getIsPendingScreenonCreateCalled()
    {
    	return pref.getBoolean("setIsPendingScreenOnCreateCalled", false);
    }
    
    
    public void setIsPendingScreenNotificationcame(boolean bFlag)
    {
    	editor.putBoolean("setIsPendingScreenNotificationcame", bFlag);
    	editor.commit();
	}
    public boolean getIsPendingScreenNotificationcame()
    {
    	return pref.getBoolean("setIsPendingScreenNotificationcame", false);
    }
    
    
    public void setIsDriverArrived(boolean isDriverArriived)
    {
    	editor.putBoolean("isDriverArriived", isDriverArriived);
    	editor.commit();
    }
    
    public boolean getIsDriverArrived()
    {
    	return pref.getBoolean("isDriverArriived", false);
    }
    
    
    public void storeServerChannelName(String ChannelName) 
    {
    	editor.putString("serverChannel", ChannelName);
    	editor.commit();
	}
    public String getChannelServerName() 
    {
		return pref.getString("serverChannel", null);
	}

    public void storeChannelName(String channel) {
    	editor.putString(PUBNUB_CHANNEL,channel);
    	editor.commit();
	}
    public String getChannelName() {
		return pref.getString(PUBNUB_CHANNEL,null);
	}
    
    
    public void setDriverCurrentlat(String lat)
    {
    	editor.putString("lat", lat);
    	editor.commit();
    }
    public double getDriverCurrentLat()
    {
    	String driverCurrentLatStr=pref.getString("lat", "0.0");
    	double driverCurrentLat=0.0;
    	try 
    	{
    		driverCurrentLat=Double.parseDouble(driverCurrentLatStr);
		} 
    	catch (NumberFormatException e) 
		{
		}
    	return driverCurrentLat;
    }
    
    public void setDriverCurrentLongi(String longi)
    {
    	editor.putString("longi", longi);
    	editor.commit();
    }
    public double getDriverCurrentLongi()
    {
    	String driverCurrentLongiStr=pref.getString("longi", "0.0");
    	double driverCurrentLongi=0.0;
    	try 
    	{
    		driverCurrentLongi=Double.parseDouble(driverCurrentLongiStr);
		} 
    	catch (NumberFormatException e) 
		{
		}
    	return driverCurrentLongi;
    }
    
    public void setDistanceInDouble(String distance)
    {
    	editor.putString("DistanceDouble", distance);
    	editor.commit();
    }
    public double getDistanceInDouble()
    {
    	String distancestr = pref.getString("DistanceDouble", "0.0");
    	double distanceDouble=0.0;
    	try 
    	{
    		distanceDouble=Double.parseDouble(distancestr);
		} 
    	catch (NumberFormatException e) 
		{
		}
    	return distanceDouble;
    }
    
    
    public void setDistance(String distance)
    {
    	editor.putString("Distance", distance);
    	editor.commit();
    }
    public double getDistance()
    {
    	String distancestr = pref.getString("Distance", "0.0");
    	double distanceDouble=0.0;
    	try 
    	{
    		distanceDouble=Double.parseDouble(distancestr);
		} 
    	catch (NumberFormatException e) 
		{
		}
    	return distanceDouble;
    }
    
    public void setPasChannel(String pasChn)
    {
    	editor.putString("pasChn", pasChn);
    	editor.commit();
    }
    public String getPasChannel()
    {
    	return pref.getString("pasChn", null);
    }
    
    
    public void setBookingid(String Bookingid)
    {
    	editor.putString("Bookingid", Bookingid);
    	editor.commit();
    }
    public String getBookingid()
    {
    	return pref.getString("Bookingid", null);
    }
    
    public void setMobile(String phone)
    {
    	editor.putString("phone", phone);
    	editor.commit();
    }
    public String getMobile()
    {
    	return pref.getString("phone", null);
    }
    
    public void setDate(String date)
    {
    	editor.putString("date", date);
    	editor.commit();
    }
    public String getDate()
    {
    	return pref.getString("date", null);
    }
    
    
    public void setIsRequested(boolean IsRequested)
    {
    	editor.putBoolean("IsRequested", IsRequested);
    	editor.commit();
    }
    
    public boolean isRequested()
    {
    	return  pref.getBoolean("IsRequested", false);
    }
    
    public void setCancelPushFlag(boolean CancelPushFlag)
    {
    	editor.putBoolean("CancelPushFlag", CancelPushFlag);
    	editor.commit();
    }
    
    public boolean isCancelPushFlag()
    {
    	return  pref.getBoolean("CancelPushFlag", false);
    }
    
    
    public void setFlagForPayment(boolean FlagForPayment)
    {
    	editor.putBoolean("FlagForPayment", FlagForPayment);
    	editor.commit();
    }
    
    public boolean isFlagForPayment()
    {
    	return  pref.getBoolean("FlagForPayment", false);
    }
    
    private long apptTime;

	public long getApptTime()
	{
		return apptTime;
	}

	public void setApptTime(long apptTime)
	{
		this.apptTime = apptTime;
	}
	
	public void setIsOnButtonClicked(boolean IsOnButtonClicked)
    {
    	editor.putBoolean("ON_CLICKEDED", IsOnButtonClicked);
    	editor.commit();
    }
    
    public boolean getIsOnButtonClicked()
    {
    	return  pref.getBoolean("ON_CLICKEDED", false);
    }
    public void setIsFlagForOther(boolean IsFlagForOther)
    {
    	editor.putBoolean("IsFlagForOther", IsFlagForOther);
    	editor.commit();
    }
    
    public boolean getIsFlagForOther()
    {
    	return  pref.getBoolean("IsFlagForOther", false);
    }
    
    
    public void setFlagForGPS(boolean bFlag)
    {
    	editor.putBoolean("FlagForGPS", bFlag);
    	editor.commit();
	}
    public boolean getFlagForGPS()
    {
    	return pref.getBoolean("FlagForGPS", false);
    }
    
    public void setDistance_tag(String Distance_tag) {
    	editor.putString("Distance_tag", Distance_tag);
    	editor.commit();
	}
    public String getDistance_tag()
    {
    	return pref.getString("Distance_tag", "");
    }
    public void setAVG_SPEED(String AVG_SPEED) {
    	editor.putString("AVG_SPEED", AVG_SPEED);
    	editor.commit();
	}
    public String getAVG_SPEED()
    {
    	return pref.getString("AVG_SPEED", "");
    }
    
    public void setAPX_AMOUNT(String APX_AMOUNT) {
    	editor.putString("APX_AMOUNT", APX_AMOUNT);
    	editor.commit();
	}
    public String getAPX_AMOUNT()
    {
    	return pref.getString("APX_AMOUNT", "");
    }
    
    public void setBeginTime(String BeginTime) {
    	editor.putString("BeginTime", BeginTime);
    	editor.commit();
	}
    public String getBeginTime()
    {
    	return pref.getString("BeginTime", "");
    }
    public void setIhavarrived(boolean isIhavarrived)
    {
    	editor.putBoolean("isIhavarrived", isIhavarrived);
    	editor.commit();
    }

    public boolean getIhavarrived()
    {
    	return pref.getBoolean("isIhavarrived", false);
    }
    public void setIBeginJourney(boolean BeginJourney)
    {
    	editor.putBoolean("BeginJourney", BeginJourney);
    	editor.commit();
    }
    public boolean getIBeginJourney()
    {
    	return pref.getBoolean("BeginJourney", false);
    }
    public void setPassenger(boolean Passenger)
    {
    	editor.putBoolean("Passenger", Passenger);
    	editor.commit();
    }
    public boolean getPassenger()
    {
    	return pref.getBoolean("Passenger", false);
    }
    
    public void setWaitingTime(String WaitingTime) {
    	editor.putString("WaitingTime", WaitingTime);
    	editor.commit();
	}
    public String getWaitingTime()
    {
    	return pref.getString("WaitingTime", "");
    }
    
    public boolean getFlagForStatusDropped()
    {
    	return pref.getBoolean("FlagForStatusDropped", false);
    }
    public void setFlagForStatusDropped(boolean FlagForStatusDropped)
    {
    	editor.putBoolean("FlagForStatusDropped", FlagForStatusDropped);
    	editor.commit();
    }
    
    public void setFlagNewBooking(boolean FlagNewBooking)
    {
    	editor.putBoolean("FlagNewBooking", FlagNewBooking);
    	editor.commit();
    }
    
    public boolean getFlagNewBooking()
    {
    	return  pref.getBoolean("FlagNewBooking", false);
    }
    
    
    
    public void setAPPT_DATE_HISTORY(String APPT_DATE_HISTORY) {
    	editor.putString("APPT_DATE_HISTORY", APPT_DATE_HISTORY);
    	editor.commit();
	}
    public String getAPPT_DATE_HISTORY()
    {
    	return pref.getString("APPT_DATE_HISTORY", "");
    }
    
    public void setEMAIL_HISTORY(String EMAIL_HISTORY) {
    	editor.putString("EMAIL_HISTORY", EMAIL_HISTORY);
    	editor.commit();
	}
    public String getEMAIL_HISTORY()
    {
    	return pref.getString("EMAIL_HISTORY", "");
    }
    
    
    public void setFlagCalender(boolean FlagCalender)
    {
    	editor.putBoolean("FlagCalender", FlagCalender);
    	editor.commit();
    }
    
    public boolean getFlagCalender()
    {
    	return  pref.getBoolean("FlagCalender", false);
    }


    public void setIsInBooking(boolean IsInBooking)
    {
        editor.putBoolean("IsInBooking", IsInBooking);
        editor.commit();
    }

    public boolean getIsInBooking()
    {
        return  pref.getBoolean("IsInBooking", false);
    }
    public void setIsOnOff(boolean IsOnOff)
    {
        editor.putBoolean("IsOnOff", IsOnOff);
        editor.commit();
    }

    public boolean IsOnOff()
    {
        return  pref.getBoolean("IsOnOff", false);
    }
    public void setPubnubData(String PubnubData) {
        editor.putString("PubnubData", PubnubData);
        editor.commit();
    }
    public String getPubnubData()
    {
        return pref.getString("PubnubData", "");
    }
    public void setBookingIdPublish(long BookingIdPublish) {
        editor.putLong("BookingIdPublish", BookingIdPublish);
        editor.commit();
    }
    public long getBookingIdPublish()
    {
        return pref.getLong("BookingIdPublish", 0);
    }
}
