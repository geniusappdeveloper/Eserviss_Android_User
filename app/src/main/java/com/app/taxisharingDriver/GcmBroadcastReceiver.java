package com.app.taxisharingDriver;

import java.util.List;

import com.app.taxisharingDriver.utility.PublishUtility;
import com.app.taxisharingDriver.utility.SessionManager;
import com.app.taxisharingDriver.utility.Utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.app.taxisharingDriver.utility.VariableConstants;
import com.pubnub.api.Pubnub;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver
{
	private String message,action;
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	SessionManager session;
	public static Pubnub PUBNUB_SERVICE = new Pubnub(VariableConstants.PUB_KEY,VariableConstants.SUB_KEY,"", false);
	@Override
	public void onReceive(Context context, Intent intent)
	{
		session = new SessionManager(context);
		boolean bFlagForCurrent = session.isHomeIsOpend();
		Bundle extras = intent.getExtras();
		boolean isbackground = isApplicationSentToBackground(context);

		if (extras!=null) 
		{
			String str="";
			for(String key:intent.getExtras().keySet())
			{
				str=str+" "+key+"=>"+intent.getExtras().get(key) + ";";

			}
			Utility.printLog("Data is"+str);
		}

		if (extras!=null) 
		{
			if (extras!=null&&extras.toString().length()>0)
			{
				message = extras.getString("payload");
				action = extras.getString("action");
				
				session.setACTION(action);

				Utility.printLog("AAA action "+action);
				Utility.printLog("AAA extras "+extras);
			}
		}

		if (action==null||"com.google.android.c2dm.intent.REGISTRATION".equals(action))
		{
			Utility.printLog("C2DM", "Received registration ID");
			final String registrationId = intent.getStringExtra("registration_id");
			String error = intent.getStringExtra("error");

			Utility.printLog("C2DM", "dmControl: registrationId = " + registrationId + ", error = " + error);
			storeRegistrationId(context, registrationId) ;
		}


		//getting live appointment

		else if ("7".equals(action)) 
		{
			Utility.printLog("Animation action inside GCM action 7");
			String aptDateTime = extras.getString("dt");
			String email=extras.getString("e");
			String bookingIdPush = extras.getString("bid");
			session.setAPT_DATE(aptDateTime);
			session.setPASSENGER_EMAIL(email);
			session.setBOOKING_ID(bookingIdPush);
			session.setFlagNewBooking(true);

			if (session.getBookingIdPublish() < Long.valueOf(bookingIdPush))
			{
				session.setBookingIdPublish(Long.valueOf(bookingIdPush));
				//Pubnub Change 17/5/2016
				publishForNotify(bookingIdPush,context);
			}
			if (bFlagForCurrent)
			{
				Utility.printLog("Animation action inside GCM action 7 RACING");
				if (MainActivity.isResponse)
				{	
					Utility.printLog("Animation action inside GCM action 7 GCM GOT FIRST");
					MainActivity.isResponse= false;
					Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");
					Utility.printLog("extras="+extras);
					homeIntent.putExtras(extras);
					context.sendBroadcast(homeIntent);
				}
			}
			else if (!session.getIsInBooking() && !isbackground)
			{
				session.setIsRequested(true);
				Intent i = new Intent(context,MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
			else
			{
				session.setIsRequested(true);
			}

		}
		else if ("51".equals(action)) 
		{
			Utility.printLog("Animation action inside GCM action 7");
			String aptDateTime = extras.getString("dt");
			String email=extras.getString("e");
			String bookingIdPush = extras.getString("bid");
			session.setAPT_DATE(aptDateTime);
			session.setPASSENGER_EMAIL(email);
			session.setBOOKING_ID(bookingIdPush);
			session.setFlagNewBooking(true);

			if (session.getBookingIdPublish() < Long.valueOf(bookingIdPush))
			{
				session.setBookingIdPublish(Long.valueOf(bookingIdPush));
				//Pubnub Change 17/5/2016
				publishForNotify(bookingIdPush,context);
			}
			if (bFlagForCurrent)
			{
				Utility.printLog("Animation action inside GCM action 7 RACING");
				if (MainActivity.isResponse)
				{	
					Utility.printLog("Animation action inside GCM action 7 GCM GOT FIRST");
					MainActivity.isResponse= false;
					Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");
					Utility.printLog("extras="+extras);
					homeIntent.putExtras(extras);
					context.sendBroadcast(homeIntent);
				}
			}
			else if (!session.getIsInBooking() && !isbackground )
			{
				session.setIsRequested(true);
				Intent i = new Intent(context,MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
			else
			{
				session.setIsRequested(true);
			}

		}

		//For cancel booking from passenger side
		/*else if ("10".equals(action))
		{
			session.setCancelPushFlag(true);
			if (!isbackground)
			{
				MainActivity.isResponse = true;
				Intent i = new Intent(context,MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
		}*/
		else if ("10".equals(action))
		{
			session.setCancelPushFlag(true);
			Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");
			Utility.printLog("extras="+extras);
			homeIntent.putExtras(extras);
			context.sendBroadcast(homeIntent);
		}

		else if("22".equals(action))
		{
			Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");
			Utility.printLog("extras="+extras);
			homeIntent.putExtras(extras);
			context.sendBroadcast(homeIntent);
		}
		//For payment confirmation
		else if("11".equals(action))
		{
			session.setFlagForPayment(true);
			session.setPayload(message);
			if (bFlagForCurrent) 
			{
				Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");
				Utility.printLog("extras=" + extras);
				homeIntent.putExtras(extras);
				context.sendBroadcast(homeIntent);				
			}
			else if (!isbackground)
			{
				Intent i = new Intent(context,MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i); 
			}
		}

		else if("12".equals(action))
		{
			session.setFlagForPayment(true);
			session.setPayload(message);
			session.setIsUserRejectedFromAdmin("true");

			/*Intent intent2=new Intent(context, SplahsActivity.class);

			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent2);
			//finish();*/

			SessionManager sessionManager=new SessionManager(context);
			if (bFlagForCurrent)
			{
				Intent homeIntent=new Intent("com.embed.anddroidpushntificationdemo11.push");
				Utility.printLog("extras=" + extras);

				sessionManager.logoutUser();
				homeIntent.putExtras(extras);
				context.sendBroadcast(homeIntent);				
			}
			else if (!isbackground)
			{
				sessionManager.logoutUser();
				Intent i = new Intent(context,SplahsActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i); 
			}
		}

		if (action != null)
		{
			if("7".equals(action) ||"51".equals(action))
			{
				if (!(session.getBOOKING_ID().equals(session.getBookingIdStatus())) && !session.getIsInBooking())
				{
					ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
					startWakefulService(context, (intent.setComponent(comp)));
					setResultCode(Activity.RESULT_OK);
				}
			}
			else
			{
				ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
				startWakefulService(context, (intent.setComponent(comp)));
				setResultCode(Activity.RESULT_OK);
			}
		}

	}

	public static boolean isApplicationSentToBackground(final Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty())
		{
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) 
			{
				return true;
			}
		}
		return false;
	}

	private void storeRegistrationId(Context context, String regId) 
	{
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = getAppVersion(context);

		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	private SharedPreferences getGCMPreferences(Context context) 
	{
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return context.getSharedPreferences(LoginActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}
	public static int getAppVersion(Context context) 
	{
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
	/**
	 * Method for publish current location to passenger.
	 */
	public void publishForNotify(String bid,Context context)
	{
		Utility utility = new Utility();
		String message ;
		//{“a”:11,”bid”:123,”receiveDt”:”2015-10-21 15:26:00”}
		message="{\"a\" :\""+11+"\",\"bid\" :\""+bid+"\",\"receiveDt\" :\""+utility.getCurrentGmtTime()+"\"}";
		Utility.printLog("Publish NotifyChannel gcm push= "+session.getChannelName());
		Utility.printLog("Publish NotifyChannel gcm push="+message);
		PublishUtility.publish(session.getChannelName(), message, PUBNUB_SERVICE);
	}
}
