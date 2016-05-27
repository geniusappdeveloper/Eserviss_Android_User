package com.eserviss.passenger.main;

import java.util.List;

import com.egnyt.eserviss.MainActivity;
import com.threembed.utilities.SessionManager;
import com.threembed.utilities.Utility;
import com.threembed.utilities.VariableConstants;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver 
{
	Bundle extras;
	@Override
	public void onReceive(final Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		/*extras = intent.getExtras();
		 Utility.printLog("","GcmBroadcastReceiver Intent in Driver App : "+intent);
		 String action=extras.getString("action");
		 String type = extras.getString("apptType");


		 Utility.printLog("","GcmBroadcastReceiver Action in B.Reciver "+action);
			extras = intent.getExtras();
			Utility.printLog("GcmBroadcastReceiver extras in Reciver ="+extras);
			Utility.printLog("GcmBroadcastReceiver action in Reciver ="+action+" type="+type);*/

		Utility.printLog("Inside onReceive GcmBroadcastReceiver");
		Utility.printLog("GcmBroadcastReceiver Intent in Reciver : "+intent);

		extras = intent.getExtras();
		String action=extras.getString("action");
		String type = extras.getString("t");
		String message=extras.getString("payload");

		Utility.printLog("GcmBroadcastReceiver extras in Reciver ="+extras);
		Utility.printLog("GcmBroadcastReceiver action in Reciver ="+action+" type="+type);
		SessionManager session =new SessionManager(context);
		boolean isbkrnd=isApplicationSentToBackground(context);

		Utility.printLog("isApplicationSentToBackground "+isbkrnd);

		Utility.printLog("NewHomePageFragment visibility="+HomePageFragment.visibleStatus());


		if("10".equals(action))
		{
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 10");
			session.setDriverArrived(false);
			session.setInvoiceRaised(false);
			session.setTripBegin(false);

			session.setDriverCancelledApt(true);

			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				session.setDriverCancelledApt(false);

				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			} 
		}

		else if("12".equals(action))
		{
			session.setPayload(message);
			session.setIsUserRejectedFromAdmin("true");

			/*Intent intent2=new Intent(context, SplahsActivity.class);

			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent2);
			//finish();*/

			if (HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				Utility.printLog("extras=" + extras);

				session.setIsLogin(false);
				homeIntent.putExtras(extras);
				context.sendBroadcast(homeIntent);				
			}
			else if (!isbkrnd)
			{
				session.setIsLogin(false);
				Intent i = new Intent(context,MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i); 
			}
		}
		
		
		/*Bundle[{action=6, payload=Driver on way, d=2015-02-15 12:25:00, e=Guddugaurav@mobifyi.com, r=4.333333333333333, t=2, dt=20150215122500,
		id=5954, ph=123456789, ltg=13.02871,77.589651666667, pic=imageTue02102015181131.jpeg, from=440129443070, 
		sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/
		//Driver on the Way
		// if(action.equals("6") && !session.isDriverOnWay())
		
		if(type!=null)
		if("6".equals(action) && "1".equals(type))
		{
			Utility.printLog("","GcmBroadcastReceiver Inside action = 6: ");
			session.setDriverOnWay(true);
			Utility.printLog("Wallah set as true gcm 6");
			session.setDriverArrived(false);
			session.setTripBegin(false);
			session.setInvoiceRaised(false);


			Utility.printLog("GcmBroadcastReceiver NAME="+extras.getString("sname"));
			Utility.printLog("GcmBroadcastReceiver PHNUM="+extras.getString("ph"));
			Utility.printLog("GcmBroadcastReceiver PIC="+extras.getString("pic"));
			Utility.printLog("GcmBroadcastReceiver EMAIL="+extras.getString("e"));
			Utility.printLog("GcmBroadcastReceiver DATE="+extras.getString("dt"));
			Utility.printLog("GcmBroadcastReceiver rating="+extras.getString("r"));

			String latLon=extras.getString("ltg");
			Utility.printLog("GcmBroadcastReceiver LATLON: "+latLon);
			String[] temp=latLon.split(",");

			Utility.printLog("GcmBroadcastReceiver Temp[0]: "+temp[0]);
			Utility.printLog("GcmBroadcastReceiver Temp[1]: "+temp[1]);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeLat_DOW(temp[0]);
			session.storeLon_DOW(temp[1]);
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("e"));
			session.storeDriverRating(extras.getString("r"));
		}
		else if("6".equals(action) && "2".equals(type))//later booking on the way
		{
			Utility.printLog("","GcmBroadcastReceiver Inside action = 6: ");
			session.setDriverOnWay(true);
			Utility.printLog("Wallah set as true gcm 6 later");
			session.setDriverArrived(false);
			session.setTripBegin(false);
			session.setInvoiceRaised(false);

			Utility.printLog("GcmBroadcastReceiver NAME="+extras.getString("sname"));
			Utility.printLog("GcmBroadcastReceiver PHNUM="+extras.getString("ph"));
			Utility.printLog("GcmBroadcastReceiver PIC="+extras.getString("pic"));
			Utility.printLog("GcmBroadcastReceiver EMAIL="+extras.getString("e"));
			Utility.printLog("GcmBroadcastReceiver DATE="+extras.getString("dt"));
			Utility.printLog("GcmBroadcastReceiver rating="+extras.getString("r"));

			String latLon=extras.getString("ltg");
			Utility.printLog("GcmBroadcastReceiver LATLON: "+latLon);
			String[] temp=latLon.split(",");

			Utility.printLog("GcmBroadcastReceiver Temp[0]: "+temp[0]);
			Utility.printLog("GcmBroadcastReceiver Temp[1]: "+temp[1]);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeLat_DOW(temp[0]);
			session.storeLon_DOW(temp[1]);
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("e"));
			session.storeDriverRating(extras.getString("r"));

			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","ON_THE_WAY");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		}

		//Driver Reached
		else if(("1".equals(type) && "7".equals(action)) && (!session.isDriverOnArrived() && !session.getBookingId().equals("0")))
		{
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 7");
			session.setDriverArrived(true); //Roshani
			session.setTripBegin(false);
			session.setInvoiceRaised(false);
			/*Bundle[{action=7, payload=Driver arrived, t=2, dt=2015-2-15 12:25:00, id=5954, ph=123456789,
					pic=imageTue02102015181131.jpeg, from=440129443070, smail=Guddugaurav@mobifyi.com, 
					sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/


			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));

			Utility.printLog("","GcmBroadcastReceiver Inside action = 7: ");
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","REACHED");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		}
		else if(("2".equals(type) && "7".equals(action)) && (!session.isDriverOnArrived()))
		{
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 7 later");
			session.setDriverArrived(true);//roshani
			session.setTripBegin(false);
			session.setInvoiceRaised(false);
			/*Bundle[{action=7, payload=Driver arrived, t=2, dt=2015-2-15 12:25:00, id=5954, ph=123456789,
					pic=imageTue02102015181131.jpeg, from=440129443070, smail=Guddugaurav@mobifyi.com, 
					sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));

			Utility.printLog("","GcmBroadcastReceiver Inside action = 7: ");
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","REACHED");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		
		}

		//journey begin
		// if(!ResideMenuActivity.DriverInvoiceRaised)
		else if(("1".equals(type) && "8".equals(action)) && (!session.isTripBegin() && !session.getBookingId().equals("0")))
		{

			/*Bundle[{action=8, payload=Journey started, t=2, dt=2015-2-15 12:25:00, id=5954, ph=123456789, 
					pic=imageTue02102015181131.jpeg, from=440129443070, smail=Guddugaurav@mobifyi.com, 
					sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/

			Utility.printLog("","GcmBroadcastReceiver Inside action = 8: ");
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 8");
			session.setDriverArrived(false);
			session.setTripBegin(true);//roshani
			session.setInvoiceRaised(false);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));

			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		}
		else if(("2".equals(type) && "8".equals(action)) && (!session.isTripBegin()))
		{
			/*Bundle[{action=8, payload=Journey started, t=2, dt=2015-2-15 12:25:00, id=5954, ph=123456789, 
					pic=imageTue02102015181131.jpeg, from=440129443070, smail=Guddugaurav@mobifyi.com, 
					sname=Guddu Gaurav, collapse_key=do_not_collapse}]*/

			Utility.printLog("","GcmBroadcastReceiver Inside action = 8: ");
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 8 later");
			session.setDriverArrived(false);
			session.setTripBegin(true);
			session.setInvoiceRaised(false);

			session.storeDocName(extras.getString("sname"));
			session.storeDocPic(VariableConstants.IMAGE_BASE_URL+extras.getString("pic"));
			session.storeDocPH(extras.getString("ph"));

			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			session.storeDriverRating(extras.getString("r"));
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		}

		//Apointment Complete(INVOICE RAISED)
		else if(("1".equals(type) && ("9".equals(action)) && (!session.isInvoiceRaised() && !session.getBookingId().equals("0"))))
		{
			Utility.printLog("","GcmBroadcastReceiver Inside action = 9: ");
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 9");
			session.setDriverArrived(false);
			session.setTripBegin(false);
			session.setInvoiceRaised(true);

			session.storeDocName(extras.getString("sname"));
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			

			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		}
		else if(("2".equals(type) && ("9".equals(action)) && (!session.isInvoiceRaised())))
		{
			Utility.printLog("","GcmBroadcastReceiver Inside action = 9: ");
			session.setDriverOnWay(false);
			Utility.printLog("Wallah set as fasle gcm 9 later");
			session.setDriverArrived(false);
			session.setTripBegin(false);
			session.setInvoiceRaised(true);

			session.storeDocName(extras.getString("sname"));
			session.storeAptDate(extras.getString("dt"));
			session.storeDriverEmail(extras.getString("smail"));
			
			if(HomePageFragment.visibleStatus())
			{
				Intent homeIntent=new Intent("com.threembed.driverapp.activity.push.popup");
				homeIntent.putExtra("ACTION","COMPLETE");
				context.sendBroadcast(homeIntent);
			}
			else if(!isbkrnd)
			{
				Intent i = new Intent();
				i.setClassName("com.egnyt.eserviss","com.eserviss.passenger.main.ResideMenuActivity");
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(i);  
			}
		}

		ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
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

}
